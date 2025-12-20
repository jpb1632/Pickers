package springBootPickers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import springBootPickers.command.OrdersCommand;
import springBootPickers.domain.OrdersDTO;
import springBootPickers.domain.PaymentDTO;
import springBootPickers.repository.OrderRepository;
import springBootPickers.service.orders.IniPayReqService;
import springBootPickers.service.orders.LectureBuyService;
import springBootPickers.service.orders.LectureOrderService;
import springBootPickers.service.orders.OrderProcessListService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Controller
@RequestMapping("orders")
public class OrdersController {

	private static final Logger log = LoggerFactory.getLogger(OrdersController.class);
	@Autowired
	LectureBuyService lectureBuyService;
	@Autowired
	LectureOrderService lectureOrderService;
	@Autowired
	OrderProcessListService orderProcessListService;
	@Autowired
	IniPayReqService iniPayReqService;	
	@Autowired
	OrderRepository orderRepository;


	
	@RequestMapping("goodsBuy")
	public String goodsBuy(String nums[], HttpSession session, Model model) {
	    lectureBuyService.execute(nums, session, model);
	    log.debug("{}", model.getAttribute("list"));
	    log.debug("{}", model.getAttribute("memberPhone"));
	    return "thymeleaf/orders/goodsOrder";
	}

	@PostMapping("goodsOrder")
	public String goodsOrder(OrdersCommand ordersCommand, 
	                         HttpSession session, 
	                         Model model,
	                         RedirectAttributes redirectAttributes) {
	    try {
	        String orderNum = lectureOrderService.execute(ordersCommand, session, model);
	        return "redirect:paymentOk?orderNum=" + orderNum;
	        
	    } catch (Exception e) {
	        log.error("주문 처리 실패", e);
	        redirectAttributes.addFlashAttribute("errorMessage", "주문 처리 중 오류가 발생했습니다.");
	        return "redirect:/lecture/cart";
	    }
	}
	@GetMapping("paymentOk")
	public String paymentOk(String orderNum, Model model) {
	    try {
	        iniPayReqService.execute(orderNum, model);
	        
	        // 데이터 확인용 로그 출력
	        log.debug("Model attributes:{}", model.asMap());
	    } catch (Exception e) {
	        log.error("요청 처리 중 오류", e);
	    }
	    return "thymeleaf/orders/payment";
	}

	
	
	@GetMapping("orderList")
	public String orderList(HttpSession session, Model model) {
		orderProcessListService.execute(null, session, model);
		return "thymeleaf/orders/orderList";
	}
	
	@GetMapping("empOrderList")
	public String empOrderList(HttpSession session, Model model) {
		orderProcessListService.execute(null, session, model);
		return "thymeleaf/orders/empOrderList";
	}
	
	@PostMapping("paymentDel")
	@Transactional
	public String paymentDel(String orderNum, RedirectAttributes redirectAttributes) {
	    try {
	        // 1. 결제 정보 조회
	        PaymentDTO payment = orderRepository.getPayment(orderNum);
	        if (payment == null) {
	            log.warn("취소할 결제 내역 없음: {}", orderNum);
	            redirectAttributes.addFlashAttribute("errorMessage", "취소할 결제 내역이 없습니다.");
	            return "redirect:orderList";
	        }
	        
	        // 2. 주문 정보 조회
	        OrdersDTO order = orderRepository.orderSelectOne(orderNum);
	        if (order == null) {
	            log.error("주문 정보 없음: {}", orderNum);
	            redirectAttributes.addFlashAttribute("errorMessage", "주문 정보를 찾을 수 없습니다.");
	            return "redirect:orderList";
	        }
	        
	        // 3. 결제 상태 확인
	        if (!"결제완료".equals(order.getPayStatus())) {
	            log.warn("취소 불가능한 상태: {}, status: {}", orderNum, order.getPayStatus());
	            redirectAttributes.addFlashAttribute("errorMessage", "취소할 수 없는 주문 상태입니다.");
	            return "redirect:orderList";
	        }
	        
	        // 4. DB에서 결제 정보 삭제
	        int result = orderRepository.paymentDel(orderNum);
	        if (result > 0) {
	            log.info("결제 데이터 삭제 성공: {}", orderNum);
	            
	            // 5. 주문 상태 초기화
	            orderRepository.resetPayStatus(orderNum);
	            
	            redirectAttributes.addFlashAttribute("message", 
	                "결제가 취소되었습니다. (실제 PG 취소는 관리자에게 문의하세요)");
	        } else {
	            redirectAttributes.addFlashAttribute("errorMessage", "결제 취소 처리 중 오류가 발생했습니다.");
	        }
	        
	    } catch (Exception e) {
	        log.error("결제 취소 처리 중 오류", e);
	        redirectAttributes.addFlashAttribute("errorMessage", "결제 취소 중 오류가 발생했습니다.");
	    }
	    
	    return "redirect:orderList";
	}
	
	@GetMapping("orderDetail")
	public String orderDetail(String orderNum, HttpSession session, Model model) {
		orderProcessListService.execute(orderNum, session, model);
		return "thymeleaf/orders/orderDetail";
	}

}