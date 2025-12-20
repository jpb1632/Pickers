package springBootPickers.service.inquire;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springBootPickers.mapper.AutoNumMapper;

@Service
public class InquiresDeleteService {
	@Autowired
	AutoNumMapper autoNumMapper;

	public void execute(String[] inquireNums) {
		autoNumMapper.numsDelete(inquireNums, "inquire", "inquire_num");
	}
}
