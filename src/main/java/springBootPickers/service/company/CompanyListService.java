package springBootPickers.service.company;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import springBootPickers.domain.CompanyDTO;
import springBootPickers.domain.StartEndPageDTO;
import springBootPickers.mapper.CompanyMapper;
import springBootPickers.service.StartEndPageService;

@Service
public class CompanyListService {
	@Autowired
	CompanyMapper companyMapper;
	@Autowired
	StartEndPageService startEndPageService;

	public void execute(Integer page, String searchWord, Model model) {
		Integer limit = 10;

		StartEndPageDTO sepDTO = startEndPageService.execute(page, limit, searchWord);
		List<CompanyDTO> list = companyMapper.companySelectList(sepDTO);
		
		Integer count = companyMapper.companyCount();
		startEndPageService.execute(page, limit, count, searchWord, list, model);

	}

	public List<CompanyDTO> searchCompanies(String companyName) {
		return companyMapper.searchCompaniesByName(companyName); // 검색한 기업 리스트 반환
	}

}
