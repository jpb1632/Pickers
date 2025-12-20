package springBootPickers.service.myPage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import springBootPickers.command.EmployeeCommand;
import springBootPickers.domain.AuthInfoDTO;
import springBootPickers.domain.EmployeeDTO;
import springBootPickers.mapper.EmployeeInfoMapper;

@Service
public class EmployeeMyUpdateService {
    @Autowired
    EmployeeInfoMapper employeeInfoMapper;

    public void execute(EmployeeCommand employeeCommand) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setEmpAddr(employeeCommand.getEmpAddr());
        dto.setEmpDetailAddr(employeeCommand.getEmpDetailAddr());
        dto.setEmpEmail(employeeCommand.getEmpEmail());
        dto.setEmpHireDate(employeeCommand.getEmpHireDate());
        dto.setEmpName(employeeCommand.getEmpName());
        dto.setEmpPhoneNum(employeeCommand.getEmpPhoneNum());
        dto.setEmpPost(employeeCommand.getEmpPost());
        dto.setGender(employeeCommand.getGender());
        dto.setEmpId(employeeCommand.getEmpId());

        employeeInfoMapper.employeeUpdate(dto);

    }
}


