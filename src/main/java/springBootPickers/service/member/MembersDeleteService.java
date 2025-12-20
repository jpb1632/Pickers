package springBootPickers.service.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springBootPickers.mapper.AutoNumMapper;

@Service
public class MembersDeleteService {
	@Autowired
	AutoNumMapper autoNumMapper;
	public void execute(String memNums[]) {
		autoNumMapper.numsDelete(memNums, "member", "mem_num");
	}
}
