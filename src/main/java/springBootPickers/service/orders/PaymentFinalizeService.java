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

    private final OrderRepository orderRepository;

    @Transactional
    public void saveApprovedPayment(Map<String, String> resultMap) {
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
            log.error(
                    "금액 불일치! 주문번호: {}, 주문금액: {}, 결제금액: {}",
                    orderNum,
                    expectedPrice,
                    paidPrice
            );
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
