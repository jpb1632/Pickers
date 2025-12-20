package springBootPickers.service.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springBootPickers.mapper.AutoNumMapper;

@Service
public class EmployeesDeleteService {
	@Autowired
	AutoNumMapper autoNumMapper;
	public void execute(String empNums[]) {
		autoNumMapper.numsDelete(empNums, "employee", "emp_num");
	}
}
