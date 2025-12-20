package springBootPickers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import springBootPickers.command.LoginCommand;
import springBootPickers.domain.AuthInfoDTO;
import springBootPickers.mapper.LoginMapper;
import springBootPickers.service.CookiesService;
import springBootPickers.service.IdCheckService;
import springBootPickers.service.login.UserLoginService;

@Controller
@RequestMapping("login")
public class LoginController {
	@Autowired
	IdCheckService idCheckService;

	@Autowired
	UserLoginService userLoginService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	LoginMapper loginMapper;
	@Autowired
	CookiesService cookiesService;

	@PostMapping("userIdCheck")
	public @ResponseBody Integer userIdCheck(String userId) {
		return idCheckService.execute(userId);
	}

	@GetMapping("login")
	public String login(HttpServletRequest request, Model model, LoginCommand loginCommand) {
		model.addAttribute("loginCommand", new LoginCommand());
		cookiesService.execute(request, model, loginCommand);
		return "thymeleaf/login";
	}

	@PostMapping("login")
	public String login(@Validated LoginCommand loginCommand, BindingResult result, HttpSession session,
	                    HttpServletResponse response) {
	    System.out.println("로그인 요청 시작: " + loginCommand);

	    // UserLoginService 실행
	    userLoginService.execute(loginCommand, session, result, response);

	    // 에러 발생 시 로그인 페이지로 반환
	    if (result.hasErrors()) {
	        System.out.println("유효성 검사 실패");
	        return "thymeleaf/login";
	    }

	    // AuthInfoDTO로 사용자 정보 가져오기
	    AuthInfoDTO auth = loginMapper.loginSelectOne(loginCommand.getUserId());

	    if (auth != null && passwordEncoder.matches(loginCommand.getUserPw(), auth.getUserPw())) {
	        System.out.println("로그인 성공");

	        // 세션에 사용자 정보 저장
	        session.setAttribute("auth", auth);
	        session.setAttribute("memNum", auth.getMemNum());
	        session.setAttribute("grade", auth.getGrade());
	        System.out.println("세션 저장 완료");

	        // 아이디 저장 기능 처리
	        if (loginCommand.isIdStore()) {
	            Cookie idStoreCookie = new Cookie("idStore", loginCommand.getUserId());
	            idStoreCookie.setPath("/");
	            idStoreCookie.setMaxAge(60 * 60 * 24 * 30); // 30일 유지
	            response.addCookie(idStoreCookie);
	            System.out.println("아이디 저장 쿠키 생성: " + idStoreCookie.getValue());
	        } else {
	            Cookie idStoreCookie = new Cookie("idStore", "");
	            idStoreCookie.setPath("/");
	            idStoreCookie.setMaxAge(0); // 삭제
	            response.addCookie(idStoreCookie);
	            System.out.println("아이디 저장 쿠키 삭제");
	        }

	        // 자동 로그인 기능 처리
	        if (loginCommand.isAutoLogin()) {
	            Cookie autoLoginCookie = new Cookie("autoLogin", loginCommand.getUserId());
	            autoLoginCookie.setPath("/");
	            autoLoginCookie.setMaxAge(60 * 60 * 24 * 30); // 30일 유지
	            response.addCookie(autoLoginCookie);
	            System.out.println("자동 로그인 쿠키 생성: " + autoLoginCookie.getValue());
	        }

	        // 메인 페이지로 리다이렉트
	        return "redirect:/";
	    } else {
	        System.out.println("로그인 실패: 아이디 또는 비밀번호 불일치");
	        result.reject("loginFail", "아이디 또는 비밀번호가 잘못되었습니다.");
	        return "thymeleaf/login";
	    }
	}


// 다영-master merge할 때 충돌 생긴 구간. 1:1문의관리땜시 다영이 코드 쓸게요. 
// 	public String login(@Validated LoginCommand loginCommand
// 			,BindingResult result
// 			,HttpSession session
// 			,HttpServletResponse response) {
// 		userLoginService.execute(loginCommand, session, result, response);
// 		if(result.hasErrors()) {
// 			return "thymeleaf/login";
// 		}
// 		 System.out.println("로그인된 사용자 정보: " + session.getAttribute("authInfo"));
// 		return "redirect:/";

// 	}

	@GetMapping("logout")
	public String logout(HttpSession session, HttpServletResponse response) {
	    // 세션 무효화
	    session.invalidate();

	    // 모든 쿠키 삭제
	    Cookie autoLoginCookie = new Cookie("autoLogin", null);
	    autoLoginCookie.setPath("/");
	    autoLoginCookie.setMaxAge(0);
	    response.addCookie(autoLoginCookie);

	    Cookie idStoreCookie = new Cookie("idStore", null);
	    idStoreCookie.setPath("/");
	    idStoreCookie.setMaxAge(0);
	    response.addCookie(idStoreCookie);

	    return "redirect:/";
	}

}
