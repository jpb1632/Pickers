package springBootPickers.service.company;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import springBootPickers.command.CompanyCommand;
import springBootPickers.domain.AuthInfoDTO;
import springBootPickers.domain.CompanyDTO;
import springBootPickers.mapper.CompanyMapper;
import springBootPickers.mapper.EmployeeMapper;

@Service
public class CompanyWriteService {
	@Autowired
	CompanyMapper companyMapper;

	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	EmployeeMapper employeeMapper;

	public void execute(CompanyCommand companyCommand, HttpSession session) {
		CompanyDTO dto = new CompanyDTO();
		dto.setCeoName(companyCommand.getCeoName());
		dto.setCompanyDescription(companyCommand.getCompanyDescription());
		dto.setCompanyName(companyCommand.getCompanyName());
		dto.setCompanyNum(companyCommand.getCompanyNum());
		dto.setEmpNum(companyCommand.getEmpNum());
		dto.setIssuedShares(companyCommand.getIssuedShares());
		dto.setListingDate(companyCommand.getListingDate());
		dto.setEstablishYear(companyCommand.getEstablishYear());
		
		AuthInfoDTO auth = (AuthInfoDTO) session.getAttribute("auth");
		String empNum = employeeMapper.getEmpNum(auth.getUserId());
		dto.setEmpNum(empNum);

		companyMapper.companyInsert(dto);

	}

}
