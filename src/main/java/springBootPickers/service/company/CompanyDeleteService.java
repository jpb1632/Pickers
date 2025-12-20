package springBootPickers.service.company;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springBootPickers.mapper.CompanyMapper;

@Service
public class CompanyDeleteService {
	@Autowired
	CompanyMapper companyMapper;

	public void execute(String companyNum) {
		companyMapper.companyDelete(companyNum);

	}
}
