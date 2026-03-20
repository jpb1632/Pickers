package springBootPickers.service.orders;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import springBootPickers.domain.OrdersDTO;
import springBootPickers.domain.PaymentDTO;
import springBootPickers.repository.OrderRepository;

@Service
@RequiredArgsConstructor
public class PaymentFinalizeService {

    private static final Logger log = LoggerFactory.getLogger(PaymentFinalizeService.class);

    private static final String ORDER_STATUS_PENDING = "입금대기중";

    private final OrderRepository orderRepository;

    @Transactional
    public void saveApprovedPayment(Map<String, String> resultMap) {
        String orderNum = resultMap.get("MOID");

        PaymentDTO existingPayment = orderRepository.getPayment(orderNum);
        if (existingPayment != null) {
            log.warn("중복 결제 시도 감지: {}", orderNum);
            throw PaymentFinalizeException.nonCompensatable(
                    "이미 결제 완료된 주문입니다: " + orderNum
            );
        }

        OrdersDTO order = orderRepository.orderSelectOne(orderNum);
        if (order == null) {
            log.error("결제 저장 중 주문을 찾을 수 없습니다: {}", orderNum);
            throw PaymentFinalizeException.compensatable(
                    "승인된 결제에 해당하는 주문이 없습니다: " + orderNum
            );
        }

        if (!ORDER_STATUS_PENDING.equals(order.getPayStatus())) {
            log.warn(
                    "결제 가능한 주문 상태가 아닙니다. 주문번호: {}, 현재 상태: {}",
                    orderNum,
                    order.getPayStatus()
            );
            throw PaymentFinalizeException.nonCompensatable(
                    "결제 가능한 상태가 아닙니다: " + orderNum
            );
        }

        int expectedPrice = order.getOrderPrice();
        int paidPrice = parsePaidPrice(resultMap.get("TotPrice"), orderNum);

        if (expectedPrice != paidPrice) {
            log.error(
                    "금액 불일치! 주문번호: {}, 주문금액: {}, 결제금액: {}",
                    orderNum,
                    expectedPrice,
                    paidPrice
            );
            throw PaymentFinalizeException.compensatable(
                    "결제 금액이 주문 금액과 일치하지 않습니다: " + orderNum
            );
        }

        PaymentDTO dto = buildPaymentDto(orderNum, resultMap);

        try {
            int insertResult = orderRepository.paymentInsert(dto);
            if (insertResult == 0) {
                throw PaymentFinalizeException.compensatable(
                        "결제 저장 결과가 비정상입니다: " + orderNum
                );
            }

            int updateResult = orderRepository.updatePayStatus(orderNum);
            if (updateResult == 0) {
                throw PaymentFinalizeException.compensatable(
                        "주문 상태 업데이트에 실패했습니다: " + orderNum
                );
            }
        } catch (PaymentFinalizeException e) {
            throw e;
        } catch (RuntimeException e) {
            log.error("승인된 결제 저장 중 오류가 발생했습니다. 주문번호: {}", orderNum, e);
            throw PaymentFinalizeException.compensatable(
                    "승인된 결제 저장에 실패했습니다: " + orderNum,
                    e
            );
        }

        log.info("결제 완료 처리 성공. 주문번호: {}, 금액: {}", orderNum, paidPrice);
    }

    private int parsePaidPrice(String totalPrice, String orderNum) {
        try {
            return Integer.parseInt(totalPrice);
        } catch (NumberFormatException e) {
            log.error("결제 금액 파싱 실패. 주문번호: {}, totalPrice={}", orderNum, totalPrice, e);
            throw PaymentFinalizeException.compensatable(
                    "결제 금액 파싱에 실패했습니다: " + orderNum,
                    e
            );
        }
    }

    private PaymentDTO buildPaymentDto(String orderNum, Map<String, String> resultMap) {
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
        return dto;
    }
}
