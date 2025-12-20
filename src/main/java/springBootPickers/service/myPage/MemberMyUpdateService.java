package springBootPickers.service.myPage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import springBootPickers.command.MemberCommand;
import springBootPickers.domain.AuthInfoDTO;
import springBootPickers.domain.MemberDTO;
import springBootPickers.mapper.MemberInfoMapper;

@Service
public class MemberMyUpdateService {

	@Autowired
	MemberInfoMapper memberInfoMapper;
	@Autowired
	PasswordEncoder passwordEncoder;

	public void execute(MemberCommand memberCommand, HttpSession session) {
		MemberDTO dto = new MemberDTO();
		dto.setGender(memberCommand.getGender());
		dto.setMemAddr(memberCommand.getMemAddr());
		dto.setMemBirth(memberCommand.getMemBirth());
		dto.setMemDetailAddr(memberCommand.getMemDetailAddr());
		dto.setMemEmail(memberCommand.getMemEmail());
		dto.setMemId(memberCommand.getMemId());
		dto.setMemName(memberCommand.getMemName());
		dto.setMemNum(memberCommand.getMemNum());
		dto.setMemPhoneNum(memberCommand.getMemPhoneNum());
		dto.setMemPost(memberCommand.getMemPost());
		dto.setMemRegisterDate(memberCommand.getMemRegisterDate());
		AuthInfoDTO auth = (AuthInfoDTO) session.getAttribute("auth");
		String currentPw = auth.getUserPw();
		if (passwordEncoder.matches(memberCommand.getMemPw(), currentPw)) {
			memberInfoMapper.memberUpdate(dto);
		}
	}

}
