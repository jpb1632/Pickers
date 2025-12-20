package springBootPickers.service.myPage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import springBootPickers.domain.AuthInfoDTO;
import springBootPickers.mapper.EmployeeInfoMapper;

@Service
public class EmployeePwUpdateService {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    EmployeeInfoMapper employeeInfoMapper;

    public String execute(String oldPw, String newPw, String newPwCon, HttpSession session) {
        AuthInfoDTO auth = (AuthInfoDTO) session.getAttribute("auth");

        // 현재 비밀번호 검증
        if (!passwordEncoder.matches(oldPw, auth.getUserPw())) {
            return "현재 비밀번호가 올바르지 않습니다.";
        }

        // 새 비밀번호와 새 비밀번호 확인 검증
        if (!newPw.equals(newPwCon)) {
            return "새 비밀번호와 확인 비밀번호가 일치하지 않습니다.";
        }

        // 새 비밀번호 업데이트
        String encodedPw = passwordEncoder.encode(newPw);
        employeeInfoMapper.empPwUpdate(encodedPw, auth.getUserId());
        auth.setUserPw(encodedPw);

        return "SUCCESS"; // 성공 메시지 반환
    }
}
