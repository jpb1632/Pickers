package springBootPickers.service.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springBootPickers.command.EmployeeCommand;
import springBootPickers.domain.EmployeeDTO;
import springBootPickers.mapper.EmployeeMapper;

@Service
public class EmployeeUpdateService {
	@Autowired
	EmployeeMapper employeeMapper;
	public void execute(EmployeeCommand employeeCommand) {
		EmployeeDTO dto = new EmployeeDTO();
		dto.setEmpAddr(employeeCommand.getEmpAddr());
		dto.setEmpDetailAddr(employeeCommand.getEmpDetailAddr());
		dto.setEmpEmail(employeeCommand.getEmpEmail());	
		dto.setEmpId(employeeCommand.getEmpId());
		dto.setEmpName(employeeCommand.getEmpName());
		dto.setEmpNum(employeeCommand.getEmpNum());
		dto.setEmpPhoneNum(employeeCommand.getEmpPhoneNum());
		dto.setEmpPost(employeeCommand.getEmpPost());
		dto.setEmpPw(employeeCommand.getEmpPw());
		dto.setEmpHireDate(employeeCommand.getEmpHireDate());
		dto.setGender(employeeCommand.getGender());
		
		employeeMapper.employeeUpdate(dto);
	}
}
