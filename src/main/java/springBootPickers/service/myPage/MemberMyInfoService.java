package springBootPickers.service.myPage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;
import springBootPickers.domain.AuthInfoDTO;
import springBootPickers.domain.MemberDTO;
import springBootPickers.mapper.MemberInfoMapper;

@Service
public class MemberMyInfoService {
	@Autowired
	MemberInfoMapper memberInfoMapper;

	public void execute(HttpSession session, Model model) {
		AuthInfoDTO auth = (AuthInfoDTO) session.getAttribute("auth");
		String memId = auth.getUserId();
		MemberDTO dto = memberInfoMapper.memberSelectOne(memId);
		model.addAttribute("memberCommand", dto);

	}

}
