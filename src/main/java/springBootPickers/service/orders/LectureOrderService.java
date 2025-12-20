package springBootPickers.service.orders;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import springBootPickers.command.OrdersCommand;
import springBootPickers.domain.AuthInfoDTO;
import springBootPickers.domain.OrdersDTO;
import springBootPickers.mapper.CartMapper;
import springBootPickers.mapper.MemberMapper;
import springBootPickers.repository.OrderRepository;

@Service
@Transactional(isolation = Isolation.SERIALIZABLE) 
@RequiredArgsConstructor
public class LectureOrderService {
 
 private final OrderRepository orderRepository;
 private final CartMapper cartMapper;
 private final MemberMapper memberMapper;
 
 public String execute(OrdersCommand ordersCommand, HttpSession session, Model model) {
     AuthInfoDTO auth = (AuthInfoDTO) session.getAttribute("auth");
     if (auth == null) {
         throw new IllegalStateException("로그인이 필요합니다");
     }
     
     String memNum = memberMapper.memberNumSelect(auth.getUserId());
     
     // 주문번호 채번 (동시성 이슈 있지만 격리수준으로 해결)
     String orderNum = orderRepository.selectOrderNum();
     
     OrdersDTO dto = new OrdersDTO();
     dto.setOrderNum(orderNum);
     dto.setOrderPrice(ordersCommand.getTotalPaymentPrice());
     dto.setMemNum(memNum);
     dto.setOrderTitle(ordersCommand.getOrderTitle());
     dto.setOrderName(ordersCommand.getOrderName());
     dto.setOrderPhoneNum(ordersCommand.getOrderPhoneNum());
     
     orderRepository.orderInsert(dto);
     
     String[] lectureNums = Arrays.stream(ordersCommand.getLectureNums().split("-"))
             .filter(num -> !num.isEmpty())
             .toArray(String[]::new);
     
     Map<String, Object> map = new HashMap<>();
     map.put("orderNum", orderNum);
     map.put("memNum", memNum);
     map.put("lectureNums", lectureNums);
     
     orderRepository.orderListInsert(map);
     
     cartMapper.lectureNumsDelete(map);
     
     return orderNum;
 }
}