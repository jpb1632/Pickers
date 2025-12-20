package springBootPickers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import springBootPickers.repository.OrderRepository;
import springBootPickers.service.orders.INIstdpayPcReturn;

@Controller
@RequestMapping("payment")
public class PaymentController {
	@Autowired
	INIstdpayPcReturn iniPayReturnService;
	@Autowired
    OrderRepository orderRepository;

	@RequestMapping("INIstdpay_pc_return")
	public String payReturn(HttpServletRequest request) {
		// 1. 결제 처리 실행
        iniPayReturnService.execute(request);

		return "thymeleaf/orders/buyfinished";
	}

	@RequestMapping("close")
	public String close() {
		return "thymeleaf/orders/close";
	}
}
