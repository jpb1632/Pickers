package springBootPickers.service.orders;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inicis.std.util.HttpUtil;
import com.inicis.std.util.SignatureUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class INIstdpayPcReturn {

 private static final Logger log = LoggerFactory.getLogger(INIstdpayPcReturn.class);
 private final PaymentFinalizeService paymentFinalizeService;
 private final ObjectMapper objectMapper;

 public void execute(HttpServletRequest request) {
     try {
          request.setCharacterEncoding("UTF-8");

         Map<String, String> paramMap = new Hashtable<>();
         Enumeration elems = request.getParameterNames();
         while (elems.hasMoreElements()) {
             String temp = (String) elems.nextElement();
             paramMap.put(temp, request.getParameter(temp));
         }

         log.debug("paramMap : {}", paramMap);

         if (!"0000".equals(paramMap.get("resultCode"))) {
             log.warn("결제 인증 실패: {}", paramMap.get("resultMsg"));
             throw new RuntimeException("결제 인증 실패: " + paramMap.get("resultMsg"));
         }

         log.info("#### 인증 성공/승인 요청 ####");

         // API 호출 및 결과 처리
         Map<String, String> resultMap = processPaymentAuth(paramMap);

         if (!"0000".equals(resultMap.get("resultCode"))) {
             throw new RuntimeException("결제 승인 실패: " + resultMap.get("resultMsg"));
         }

         paymentFinalizeService.saveApprovedPayment(resultMap);

     } catch (Exception e) {
         log.error("결제 처리 중 오류", e);
         throw new RuntimeException("결제 처리 실패", e);
     }
 }

 private Map<String, String> processPaymentAuth(Map<String, String> paramMap) throws Exception {
     String mid = paramMap.get("mid");
     String timestamp = SignatureUtil.getTimestamp();
     String authToken = paramMap.get("authToken");
     String authUrl = paramMap.get("authUrl");
     String netCancel = paramMap.get("netCancelUrl");

     // signature 생성
     Map<String, String> signParam = new HashMap<>();
     signParam.put("authToken", authToken);
     signParam.put("timestamp", timestamp);
     String signature = SignatureUtil.makeSignature(signParam);

     // API 요청
     Map<String, String> authMap = new Hashtable<>();
     authMap.put("mid", mid);
     authMap.put("authToken", authToken);
     authMap.put("signature", signature);
     authMap.put("timestamp", timestamp);
     authMap.put("charset", "UTF-8");
     authMap.put("format", "JSON");

     HttpUtil httpUtil = new HttpUtil();
     
     try {
         String authResultString = httpUtil.processHTTP(authMap, authUrl);
         return parseJsonResult(authResultString);
         
     } catch (Exception ex) {
         // 망취소 API 호출
         String netCancelResultString = httpUtil.processHTTP(authMap, netCancel);
         log.debug("망취소 API 결과: {}", netCancelResultString);
         throw ex;
     }
 }

 private Map<String, String> parseJsonResult(String responseBody) throws Exception {
     Map<String, Object> rawResult = objectMapper.readValue(
             responseBody,
             new TypeReference<Map<String, Object>>() {}
     );

     Map<String, String> parsedResult = new HashMap<>();
     rawResult.forEach((key, value) -> parsedResult.put(key, value == null ? null : String.valueOf(value)));
     return parsedResult;
 }
}
