package springBootPickers.service.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springBootPickers.command.MemberCommand;
import springBootPickers.domain.MemberDTO;
import springBootPickers.mapper.MemberMapper;

@Service
public class MemberUpdateService {
	@Autowired
	MemberMapper memberMapper;

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
		dto.setMemPw(memberCommand.getMemPw());
		dto.setMemRegisterDate(memberCommand.getMemRegisterDate());

		memberMapper.memberUpdate(dto);
	}

}
