package springBootPickers.service.orders;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inicis.std.util.HttpUtil;
import com.inicis.std.util.ParseUtil;
import com.inicis.std.util.SignatureUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import springBootPickers.domain.OrdersDTO;
import springBootPickers.domain.PaymentDTO;
import springBootPickers.repository.OrderRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
@RequiredArgsConstructor
public class INIstdpayPcReturn {

 private static final Logger log = LoggerFactory.getLogger(INIstdpayPcReturn.class);
 private final OrderRepository orderRepository;

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

         savePaymentInfo(resultMap);

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
         
         String parsedResult = authResultString.replace(",", "&")
                 .replace(":", "=")
                 .replace("\"", "")
                 .replace(" ", "")
                 .replace("\n", "")
                 .replace("}", "")
                 .replace("{", "");
         
         return ParseUtil.parseStringToMap(parsedResult);
         
     } catch (Exception ex) {
         // 망취소 API 호출
         String netCancelResultString = httpUtil.processHTTP(authMap, netCancel);
         log.debug("망취소 API 결과: {}", netCancelResultString);
         throw ex;
     }
 }

 private void savePaymentInfo(Map<String, String> resultMap) {
	    String orderNum = resultMap.get("MOID");
	    
	    PaymentDTO existingPayment = orderRepository.getPayment(orderNum);
	    if (existingPayment != null) {
	        log.warn("중복 결제 시도 감지: {}", orderNum);
	        throw new RuntimeException("이미 결제 완료된 주문입니다");
	    }
	    
	    OrdersDTO order = orderRepository.orderSelectOne(orderNum);
	    if (order == null) {
	        log.error("존재하지 않는 주문: {}", orderNum);
	        throw new RuntimeException("주문을 찾을 수 없습니다");
	    }
	    
	    if (!"입금대기중".equals(order.getPayStatus())) {
	        log.warn("잘못된 주문 상태: {}, status: {}", orderNum, order.getPayStatus());
	        throw new RuntimeException("결제 가능한 상태가 아닙니다");
	    }
	    
	    int expectedPrice = order.getOrderPrice();
	    int paidPrice = Integer.parseInt(resultMap.get("TotPrice"));
	    
	    if (expectedPrice != paidPrice) {
	        log.error("금액 불일치! 주문번호: {}, 주문금액: {}, 결제금액: {}", 
	            orderNum, expectedPrice, paidPrice);
	        throw new RuntimeException("결제 금액이 일치하지 않습니다");
	    }
	    
	    PaymentDTO dto = new PaymentDTO();
	    dto.setOrderNum(orderNum);
	    dto.setApplDate(resultMap.get("applDate"));
	    dto.setApplTime(resultMap.get("applTime"));
	    dto.setCardNum(resultMap.get("CARD_Num"));
	    dto.setConfirmNum(resultMap.get("applNum"));
	    dto.setPayMethod(resultMap.get("payMethod"));
	    dto.setTid(resultMap.get("tid"));
	    dto.setTotalPrice(resultMap.get("TotPrice"));
	    dto.setResultMessage(resultMap.get("resultMsg"));

	    log.debug("결제 데이터 저장: {}", dto);

	    orderRepository.paymentInsert(dto);

	    int updateResult = orderRepository.updatePayStatus(orderNum);
	    if (updateResult == 0) {
	        log.error("주문 상태 업데이트 실패: {}", orderNum);
	        throw new RuntimeException("주문 상태 업데이트 실패");
	    }
	    
	    log.info("결제 완료: {}, 금액: {}", orderNum, paidPrice);
	}
}