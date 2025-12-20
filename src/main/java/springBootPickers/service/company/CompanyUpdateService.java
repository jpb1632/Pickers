package springBootPickers.service.company;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;
import springBootPickers.command.CompanyCommand;
import springBootPickers.domain.AuthInfoDTO;
import springBootPickers.domain.CompanyDTO;
import springBootPickers.mapper.CompanyMapper;
import springBootPickers.mapper.EmployeeMapper;

@Service
public class CompanyUpdateService {
	@Autowired
	CompanyMapper companyMapper;
	@Autowired
	EmployeeMapper employeeMapper;

	public void execute(CompanyCommand companyCommand, HttpSession session, Model model) {
		CompanyDTO dto = new CompanyDTO();
		dto.setCeoName(companyCommand.getCeoName());
		dto.setCompanyDescription(companyCommand.getCompanyDescription());
		dto.setCompanyName(companyCommand.getCompanyName());
		dto.setCompanyNum(companyCommand.getCompanyNum());
		dto.setEmpNum(companyCommand.getEmpNum());
		dto.setEstablishYear(companyCommand.getEstablishYear());
		dto.setIssuedShares(companyCommand.getIssuedShares());
		dto.setListingDate(companyCommand.getListingDate());

		AuthInfoDTO auth = (AuthInfoDTO) session.getAttribute("auth");
		String empNum = employeeMapper.getEmpNum(auth.getUserId());
		dto.setEmpNum(empNum);

		companyMapper.companyUpdate(dto);

	}

}
