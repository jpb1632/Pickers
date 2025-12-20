package springBootPickers.service.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springBootPickers.mapper.MemberMapper;

@Service
public class MemberDeleteService {
	@Autowired
	MemberMapper memberMapper;

	public void execute(String memNum) {
		memberMapper.memDelete(memNum);

	}

}
