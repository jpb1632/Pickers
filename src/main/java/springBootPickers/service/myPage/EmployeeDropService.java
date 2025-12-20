package springBootPickers.service.myPage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import springBootPickers.domain.AuthInfoDTO;
import springBootPickers.mapper.EmployeeInfoMapper;

@Service
public class EmployeeDropService {
    @Autowired
    EmployeeInfoMapper employeeInfoMapper;
    @Autowired
    PasswordEncoder passwordEncoder;

    public String execute(String empPw, HttpSession session) {
        AuthInfoDTO auth = (AuthInfoDTO) session.getAttribute("auth");

        // 비밀번호 검증
        if (!passwordEncoder.matches(empPw, auth.getUserPw())) {
            return "비밀번호가 일치하지 않습니다."; // 실패 메시지 반환
        }

        // 계정 삭제
        employeeInfoMapper.empDelete(auth.getUserId());
        return "SUCCESS"; // 성공 메시지 반환
    }
}
