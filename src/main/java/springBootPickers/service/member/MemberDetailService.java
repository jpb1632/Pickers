package springBootPickers.service.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import springBootPickers.domain.MemberDTO;
import springBootPickers.mapper.MemberMapper;

@Service
public class MemberDetailService {
	@Autowired
	MemberMapper memberMapper;

	public void execute(String memNum, Model model) {
		MemberDTO dto = memberMapper.memberSelectOne(memNum);
		model.addAttribute("memberCommand", dto);

	}

}
