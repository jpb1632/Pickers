package springBootPickers.service.myPage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import springBootPickers.domain.AuthInfoDTO;
import springBootPickers.mapper.MemberInfoMapper;
import springBootPickers.mapper.MemberMapper;

@Service
public class MemberPwUpdateService {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    MemberInfoMapper memberInfoMapper;

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

        // 비밀번호 업데이트
        String encodedPw = passwordEncoder.encode(newPw);
        memberInfoMapper.memberPwUpdate(encodedPw, auth.getUserId());
        auth.setUserPw(encodedPw);

        return "SUCCESS";
    }
}
