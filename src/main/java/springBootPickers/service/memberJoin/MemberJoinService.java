package springBootPickers.service.memberJoin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import springBootPickers.command.MemberCommand;
import springBootPickers.domain.MemberDTO;
import springBootPickers.mapper.MemberMapper;

@Service
public class MemberJoinService {

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	MemberMapper memberMapper;

	public void execute(MemberCommand memberCommand) {
		MemberDTO dto = new MemberDTO();
		dto.setGender(memberCommand.getGender());
		dto.setMemAddr(memberCommand.getMemAddr());
		dto.setMemDetailAddr(memberCommand.getMemDetailAddr());
		dto.setMemEmail(memberCommand.getMemEmail());
		dto.setMemId(memberCommand.getMemId());
		dto.setMemName(memberCommand.getMemName());
		dto.setMemPhoneNum(memberCommand.getMemPhoneNum());
		dto.setMemPost(memberCommand.getMemPost());
		dto.setMemPw(passwordEncoder.encode(memberCommand.getMemPw()));
		dto.setMemBirth(memberCommand.getMemBirth());
		dto.setMemNum(memberCommand.getMemNum());

		String encodePw = passwordEncoder.encode(memberCommand.getMemPw());
		dto.setMemPw(encodePw);
		memberMapper.memberInsert(dto);

		dto.setStockQuantity(10); // 환영주식 10주
	}
}
