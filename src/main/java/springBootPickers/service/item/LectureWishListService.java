package springBootPickers.service.item;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;
import springBootPickers.domain.AuthInfoDTO;
import springBootPickers.domain.LectureDTO;
import springBootPickers.mapper.ItemMapper;
import springBootPickers.mapper.MemberMapper;

@Service
public class LectureWishListService {
	@Autowired
	MemberMapper memberMapper;
	@Autowired
	ItemMapper itemMapper;
	public void execute(HttpSession session, Model model) {
		AuthInfoDTO authInfo = (AuthInfoDTO)session.getAttribute("auth");
		String memNum = memberMapper.memberNumSelect(authInfo.getUserId());
		List<LectureDTO> list = itemMapper.wishSelectList(memNum);
		model.addAttribute("list", list);
	}
}
