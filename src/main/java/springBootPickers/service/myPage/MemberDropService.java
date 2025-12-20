package springBootPickers.service.myPage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import springBootPickers.domain.AuthInfoDTO;
import springBootPickers.mapper.MemberInfoMapper;

@Service
public class MemberDropService {

    @Autowired
    MemberInfoMapper memberInfoMapper;
    @Autowired
    PasswordEncoder passwordEncoder;

    public String execute(String memPw, HttpSession session) {
        AuthInfoDTO auth = (AuthInfoDTO) session.getAttribute("auth");

        // 비밀번호 검증
        if (!passwordEncoder.matches(memPw, auth.getUserPw())) {
            return "비밀번호가 일치하지 않습니다."; // 실패 메시지 반환
        }

        // 계정 삭제
        memberInfoMapper.memberDelete(auth.getUserId());
        return "SUCCESS"; // 성공 메시지 반환
    }
}
