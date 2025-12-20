package springBootPickers.service.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springBootPickers.mapper.AutoNumMapper;
import springBootPickers.mapper.EmployeeMapper;

@Service
public class EmployeeDeleteService {
	@Autowired
	EmployeeMapper employeeMapper;
	public void execute(String empNum) {
		employeeMapper.empDelete(empNum);
	}
}
