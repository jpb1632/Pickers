package springBootPickers.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import springBootPickers.command.LoginCommand;
import springBootPickers.domain.AuthInfoDTO;
import springBootPickers.mapper.LoginMapper;

@Service
public class CookiesService {
    @Autowired
    LoginMapper loginMapper;

    public void execute(HttpServletRequest request, Model model, LoginCommand loginCommand) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                System.out.println("쿠키 이름: " + cookie.getName() + ", 쿠키 값: " + cookie.getValue());
                if ("idStore".equals(cookie.getName())) {
                    loginCommand.setUserId(cookie.getValue());
                    loginCommand.setIdStore(true);
                    System.out.println("아이디 저장 쿠키 값 설정: " + cookie.getValue());
                }
                if ("autoLogin".equals(cookie.getName())) {
                    String userId = cookie.getValue();
                    AuthInfoDTO auth = loginMapper.loginSelectOne(userId);
                    if (auth != null) {
                        HttpSession session = request.getSession();
                        session.setAttribute("auth", auth);
                        session.setAttribute("memNum", auth.getMemNum());
                        session.setAttribute("grade", auth.getGrade());
                        System.out.println("자동 로그인 세션 설정 완료: " + auth.getUserId());
                    } else {
                        System.out.println("자동 로그인 실패: 유효하지 않은 쿠키 값");
                    }
                }
            }
        }
        model.addAttribute("loginCommand", loginCommand); // LoginCommand 객체를 뷰에 전달
    }
}
