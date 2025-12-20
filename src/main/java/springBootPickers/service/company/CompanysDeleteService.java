package springBootPickers.service.company;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springBootPickers.mapper.AutoNumMapper;

@Service
public class CompanysDeleteService {
	@Autowired
	AutoNumMapper autoNumMapper;

	public void execute(String[] companyNums) {
		autoNumMapper.numsDelete(companyNums, "company", "company_num");

	}

}
