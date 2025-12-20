package springBootPickers.service.help;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import springBootPickers.domain.AuthInfoDTO;
import springBootPickers.domain.UserChangePasswordDTO;
import springBootPickers.mapper.FindMapper;
import springBootPickers.mapper.LoginMapper;

@Service
public class FindPwService {
    @Autowired
    FindMapper findMapper;
    @Autowired
    PasswordEncoder passwordEncoder; 
    @Autowired
    LoginMapper loginMapper;

    public void execute(String userId, String userPhone, Model model) {
        // 로그인 정보 조회
        AuthInfoDTO auth = loginMapper.loginSelectOne(userId);

        // 해당하는 사용자 정보가 없는 경우 처리
        if (auth == null) {
            model.addAttribute("newPw", null);
            model.addAttribute("errorMessage", "해당하는 사용자 정보를 찾을 수 없습니다.");
            return;
        }

        // 임시 비밀번호 생성
        String newPw = UUID.randomUUID().toString().substring(0, 8);
        UserChangePasswordDTO dto = new UserChangePasswordDTO();
        dto.setUserId(userId);
        dto.setUserPhone(userPhone);
        dto.setUserPw(passwordEncoder.encode(newPw));

        // 등급에 따라 테이블 및 컬럼 설정
        if (auth.getGrade().equals("mem")) {
            dto.setTableName("member");
            dto.setPwColumName("mem_pw");
            dto.setUserIdColumName("mem_id");
            dto.setPhoneColumn("mem_phone_num");
        } else if (auth.getGrade().equals("emp")) {
            dto.setTableName("employee");
            dto.setPwColumName("emp_pw");
            dto.setUserIdColumName("emp_id");
            dto.setPhoneColumn("emp_phone_num");
        }

        // 비밀번호 업데이트
        int result = findMapper.pwUpdate(dto);
        if (result > 0) {
            model.addAttribute("newPw", newPw);
        } else {
            model.addAttribute("newPw", null);
            model.addAttribute("errorMessage", "비밀번호 변경에 실패했습니다.");
        }
    }
}
