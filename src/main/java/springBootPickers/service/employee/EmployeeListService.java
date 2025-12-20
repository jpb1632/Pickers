package springBootPickers.service.employee;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import springBootPickers.domain.EmployeeDTO;
import springBootPickers.domain.StartEndPageDTO;
import springBootPickers.mapper.EmployeeMapper;
import springBootPickers.service.StartEndPageService;

@Service
public class EmployeeListService {
	@Autowired
	EmployeeMapper employeemapper;
	@Autowired
	StartEndPageService startEndPageService;

	public void execute(Integer page, String searchWord, Model model) {
		Integer limit = 3;

		StartEndPageDTO sepDTO = startEndPageService.execute(page, limit, searchWord);
		List<EmployeeDTO> list = employeemapper.employeeSelectList(sepDTO);

		Integer count = employeemapper.employeeCount();
		startEndPageService.execute(page, limit, count, searchWord, list, model);

	}

}
