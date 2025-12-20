package springBootPickers.service.myPage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;
import springBootPickers.domain.AuthInfoDTO;
import springBootPickers.domain.EmployeeDTO;
import springBootPickers.mapper.EmployeeInfoMapper;

@Service
public class EmployeeInfoService {
	@Autowired
	EmployeeInfoMapper employeeInfoMapper;
	public void execute(HttpSession session, Model model) {
		AuthInfoDTO auth = (AuthInfoDTO)session.getAttribute("auth");
		EmployeeDTO dto = employeeInfoMapper.employeeSelectOne(auth.getUserId());
		model.addAttribute("employeeCommand", dto);
	}
}



