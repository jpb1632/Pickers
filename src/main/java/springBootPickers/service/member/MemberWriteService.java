package springBootPickers.service.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import springBootPickers.command.MemberCommand;
import springBootPickers.domain.MemberDTO;
import springBootPickers.mapper.MemberMapper;

@Service
public class MemberWriteService {
	@Autowired
	MemberMapper memberMapper;

	@Autowired
	PasswordEncoder passwordEncoder;

	public void execute(MemberCommand memberCommand) {
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

		String encodePw = passwordEncoder.encode(memberCommand.getMemPw());
		dto.setMemPw(encodePw);
		memberMapper.memberInsert(dto);
	}
}
