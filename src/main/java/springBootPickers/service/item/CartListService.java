package springBootPickers.service.item;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;
import springBootPickers.domain.AuthInfoDTO;
import springBootPickers.domain.LectureCartDTO;
import springBootPickers.mapper.CartMapper;
import springBootPickers.mapper.MemberMapper;

@Service
public class CartListService {
	@Autowired
	MemberMapper memberMapper;
	@Autowired
	CartMapper cartMapper;
	public void execute(Model model, HttpSession session) {
		AuthInfoDTO auth = (AuthInfoDTO)session.getAttribute("auth");
		String memNum = memberMapper.memberNumSelect(auth.getUserId());
		
		List<LectureCartDTO> list = cartMapper.cartSelectList(memNum, null);		
		model.addAttribute("list", list);
		
		Integer totPri = 0;
		Integer totQty = 0;
		
		for(LectureCartDTO dto : list) {
			totPri += dto.getLectureDTO().getLecturePrice() * dto.getCartDTO().getCartQty();
			totQty += dto.getCartDTO().getCartQty();
		}
		model.addAttribute("totPri", totPri);
		model.addAttribute("totQty", totQty);
	}

}
