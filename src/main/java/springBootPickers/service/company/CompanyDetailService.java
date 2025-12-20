package springBootPickers.service.company;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import springBootPickers.domain.CompanyDTO;
import springBootPickers.mapper.CompanyMapper;

@Service
public class CompanyDetailService {
	@Autowired
	CompanyMapper companyMapper;

//	public void execute(String companyNum, Model model) {
//		CompanyDTO dto = companyMapper.companySelectOne(companyNum);
//		model.addAttribute("companyCommand", dto);
//	}

	public CompanyDTO execute(String companyNum, Model model) {
		if (companyNum == null || companyNum.isEmpty()) {
			return null;
		}
		CompanyDTO dto = companyMapper.companySelectOne(companyNum);
		model.addAttribute("companyCommand", dto);
		if (dto == null) {
			// 해당 inquireNum에 대한 데이터가 없을 경우 null 반환
			return null;
		}
		return dto;
	}

}