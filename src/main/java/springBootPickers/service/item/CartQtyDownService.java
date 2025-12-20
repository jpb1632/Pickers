package springBootPickers.service.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import springBootPickers.domain.AuthInfoDTO;
import springBootPickers.mapper.CartMapper;
import springBootPickers.mapper.MemberMapper;

@Service
public class CartQtyDownService {
	@Autowired
	MemberMapper memberMapper;
	@Autowired
	CartMapper cartMapper;
	public void execute(String lectureNum, HttpSession session) {
		AuthInfoDTO auth = (AuthInfoDTO)session.getAttribute("auth");
		String memNum = memberMapper.memberNumSelect(auth.getUserId());
		cartMapper.cartQtyDown(lectureNum, memNum);
	}

}
