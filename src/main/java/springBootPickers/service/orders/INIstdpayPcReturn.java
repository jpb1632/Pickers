package springBootPickers.service.orders;

import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inicis.std.util.HttpUtil;
import com.inicis.std.util.SignatureUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class INIstdpayPcReturn {

    private static final Logger log = LoggerFactory.getLogger(INIstdpayPcReturn.class);

    private final PaymentFinalizeService paymentFinalizeService;
    private final ObjectMapper objectMapper;

    @Value("${inicis.signkey}")
    private String signKey;

    public void execute(HttpServletRequest request) {
        try {
            request.setCharacterEncoding(StandardCharsets.UTF_8.name());

            Map<String, String> paramMap = extractRequestParams(request);
            log.debug("결제 리턴 파라미터: {}", paramMap);

            if (!"0000".equals(paramMap.get("resultCode"))) {
                throw new RuntimeException("결제 인증 실패: " + paramMap.get("resultMsg"));
            }

            ApprovalRequestContext approvalContext = buildApprovalRequestContext(paramMap);
            Map<String, String> resultMap = requestPaymentApproval(approvalContext);

            if (!"0000".equals(resultMap.get("resultCode"))) {
                throw new RuntimeException("결제 승인 실패: " + resultMap.get("resultMsg"));
            }

            finalizeApprovedPayment(approvalContext, resultMap);
        } catch (Exception e) {
            log.error("결제 처리 중 오류", e);
            throw new RuntimeException("결제 처리 실패", e);
        }
    }

    private void finalizeApprovedPayment(
            ApprovalRequestContext approvalContext,
            Map<String, String> resultMap
    ) {
        try {
            paymentFinalizeService.saveApprovedPayment(resultMap);
        } catch (PaymentFinalizeException e) {
            if (e.requiresNetworkCancel()) {
                try {
                    requestNetworkCancel(approvalContext, "결제 저장 실패");
                } catch (Exception cancelEx) {
                    log.error("결제 저장 실패 후 망취소에 실패했습니다. 주문번호: {}", approvalContext.orderNum(), cancelEx);
                    e.addSuppressed(cancelEx);
                }
            }
            throw e;
        }
    }

    private Map<String, String> requestPaymentApproval(ApprovalRequestContext approvalContext) throws Exception {
        HttpUtil httpUtil = new HttpUtil();

        try {
            String authResultString = httpUtil.processHTTP(
                    buildApprovalRequestMap(approvalContext),
                    approvalContext.authUrl()
            );
            return parseJsonResult(authResultString);
        } catch (Exception ex) {
            try {
                requestNetworkCancel(approvalContext, "승인 요청 실패");
            } catch (Exception cancelEx) {
                log.error("승인 요청 실패 후 망취소에 실패했습니다. 주문번호: {}", approvalContext.orderNum(), cancelEx);
                ex.addSuppressed(cancelEx);
            }
            throw ex;
        }
    }

    private void requestNetworkCancel(ApprovalRequestContext approvalContext, String reason) throws Exception {
        if (approvalContext.netCancelUrl() == null || approvalContext.netCancelUrl().isBlank()) {
            throw new IllegalStateException("주문 " + approvalContext.orderNum() + "의 netCancelUrl이 없습니다");
        }

        HttpUtil httpUtil = new HttpUtil();
        String netCancelResultString = httpUtil.processHTTP(
                buildApprovalRequestMap(approvalContext),
                approvalContext.netCancelUrl()
        );
        Map<String, String> cancelResult = parseJsonResult(netCancelResultString);

        if (!"0000".equals(cancelResult.get("resultCode"))) {
            throw new IllegalStateException(
                    "주문 "
                            + approvalContext.orderNum()
                            + "의 망취소에 실패했습니다: "
                            + cancelResult.get("resultMsg")
            );
        }

        log.warn(
                "망취소 완료. 주문번호: {}, 사유: {}, 결과 메시지: {}",
                approvalContext.orderNum(),
                reason,
                cancelResult.get("resultMsg")
        );
    }

    private ApprovalRequestContext buildApprovalRequestContext(Map<String, String> paramMap) {
        String orderNum = firstNonBlank(paramMap.get("orderNumber"), paramMap.get("MOID"), "unknown");
        return new ApprovalRequestContext(
                orderNum,
                requireParam(paramMap, "mid"),
                requireParam(paramMap, "authToken"),
                requireParam(paramMap, "authUrl"),
                requireParam(paramMap, "netCancelUrl"),
                paramMap.get("price")
        );
    }

    private Map<String, String> buildApprovalRequestMap(ApprovalRequestContext approvalContext) throws Exception {
        String timestamp = SignatureUtil.getTimestamp();

        Map<String, String> signatureParam = new HashMap<>();
        signatureParam.put("authToken", approvalContext.authToken());
        signatureParam.put("timestamp", timestamp);

        Map<String, String> verificationParam = new HashMap<>();
        verificationParam.put("authToken", approvalContext.authToken());
        verificationParam.put("signKey", signKey);
        verificationParam.put("timestamp", timestamp);

        Map<String, String> authMap = new Hashtable<>();
        authMap.put("mid", approvalContext.mid());
        authMap.put("authToken", approvalContext.authToken());
        authMap.put("signature", SignatureUtil.makeSignature(signatureParam));
        authMap.put("verification", SignatureUtil.makeSignature(verificationParam));
        authMap.put("timestamp", timestamp);
        authMap.put("charset", StandardCharsets.UTF_8.name());
        authMap.put("format", "JSON");

        if (approvalContext.price() != null && !approvalContext.price().isBlank()) {
            authMap.put("price", approvalContext.price());
        }

        return authMap;
    }

    private Map<String, String> extractRequestParams(HttpServletRequest request) {
        Map<String, String> paramMap = new Hashtable<>();
        Enumeration<String> elems = request.getParameterNames();

        while (elems.hasMoreElements()) {
            String key = elems.nextElement();
            paramMap.put(key, request.getParameter(key));
        }

        return paramMap;
    }

    private String requireParam(Map<String, String> paramMap, String key) {
        String value = paramMap.get(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("필수 결제 파라미터가 없습니다: " + key);
        }
        return value;
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
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

    private record ApprovalRequestContext(
            String orderNum,
            String mid,
            String authToken,
            String authUrl,
            String netCancelUrl,
            String price
    ) {
    }
}
