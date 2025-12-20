package springBootPickers.service.orders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;
import springBootPickers.domain.AuthInfoDTO;
import springBootPickers.domain.OrdersListDTO;
import springBootPickers.mapper.MemberMapper;
import springBootPickers.repository.OrderRepository;

@Service
public class OrderProcessListService {
	@Autowired
	MemberMapper memberMapper;
	@Autowired
	OrderRepository orderRepository;
	
	public void execute(String orderNum, HttpSession session, Model model) {
		AuthInfoDTO auth = (AuthInfoDTO)session.getAttribute("auth");
		String memNum = memberMapper.memberNumSelect(auth.getUserId());
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("memNum", memNum);
		map.put("orderNum", orderNum);

		List<OrdersListDTO> list = orderRepository.orderList(map);
		
		model.addAttribute("list", list);
	}
}
