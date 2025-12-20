package springBootPickers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import springBootPickers.command.EmployeeCommand;
import springBootPickers.command.MemberCommand;
import springBootPickers.service.myPage.EmployeeDropService;
import springBootPickers.service.myPage.EmployeeInfoService;
import springBootPickers.service.myPage.EmployeeMyUpdateService;
import springBootPickers.service.myPage.EmployeePwUpdateService;
import springBootPickers.service.myPage.MemberDropService;
import springBootPickers.service.myPage.MemberMyInfoService;
import springBootPickers.service.myPage.MemberMyUpdateService;
import springBootPickers.service.myPage.MemberPwUpdateService;

@Controller
@RequestMapping("myPage")
public class MyPageController {

   @Autowired
   MemberMyInfoService memberMyInfoService;
   @Autowired
   MemberMyUpdateService memberMyUpdateService;
   @Autowired
   MemberPwUpdateService memberPwUpdateService;
   @Autowired
   MemberDropService memberDropService;
   @Autowired
   EmployeeInfoService employeeInfoService;
   @Autowired
   EmployeeMyUpdateService employeeMyUpdateService;
   @Autowired
   EmployeePwUpdateService employeePwUpdateService;
   @Autowired
   EmployeeDropService employeeDropService;

   @GetMapping("memMyPage")
   public String memMyPage(HttpSession session, Model model) {
      memberMyInfoService.execute(session, model);
      return "thymeleaf/myPage/myPage";
   }

   @GetMapping("memberUpdate")
   public String memberUpdate(HttpSession session, Model model) {
      memberMyInfoService.execute(session, model);
      return "thymeleaf/myPage/myModify";
   }

   @PostMapping("memberModify")
   public String memberModify(MemberCommand memberCommand, HttpSession session) {
      memberMyUpdateService.execute(memberCommand, session);
      return "redirect:memMyPage";
   }

   @GetMapping("memberPwModify")
   public String memberPwModify() {
      return "thymeleaf/myPage/myNewPw";
   }

   @PostMapping("memberPwPro")
   public String memberPwPro(String oldPw, String newPw, String newPwCon, HttpSession session, Model model) {
       String result = memberPwUpdateService.execute(oldPw, newPw, newPwCon, session);

       if (!"SUCCESS".equals(result)) {
           model.addAttribute("errorMessage", result);
           return "thymeleaf/myPage/myNewPw"; // 에러 메시지와 함께 비밀번호 변경 페이지로 이동
       }

       return "redirect:memMyPage"; // 성공 시 마이페이지로 리다이렉트
   }


   @GetMapping("memberDelete")
   public String memberDrop() {
      return "thymeleaf/myPage/memberDrop";
   }

   @PostMapping("memberDropOk")
   public String memberDropOk(String memPw, HttpSession session, Model model) {
       String result = memberDropService.execute(memPw, session);

       if (!"SUCCESS".equals(result)) {
           model.addAttribute("errorMessage", result); // 오류 메시지 설정
           return "thymeleaf/myPage/memberDrop"; // 탈퇴 페이지로 다시 이동
       }

       // 성공 시 세션 무효화 및 로그아웃 처리
       session.invalidate();
       return "redirect:/login/logout";
   }


   @GetMapping("empMyPage")
   public String empMyPage(HttpSession session, Model model) {
      employeeInfoService.execute(session, model);
      return "thymeleaf/myPage/empMyPage";
   }

   @GetMapping("empUpdate")
   public String empModify(HttpSession session, Model model) {
      employeeInfoService.execute(session, model);
      return "thymeleaf/myPage/empMyModify";
   }

   @PostMapping("empModify")
   public String empMyModify(
           @ModelAttribute("employeeCommand") @Valid EmployeeCommand employeeCommand,
           BindingResult bindingResult,
           HttpSession session) {
       if (bindingResult.hasErrors()) {
         return "thymeleaf/myPage/empMyModify";
       }
       employeeMyUpdateService.execute(employeeCommand);
       return "redirect:empMyPage";
   }


   @GetMapping("empPwModify")
   public String empPwModify() {
      return "thymeleaf/myPage/empMyNewPw";
   }

   @PostMapping("empPwPro")
   public String empPwPro(String oldPw, String newPw, String newPwCon, HttpSession session, Model model) {
       String result = employeePwUpdateService.execute(oldPw, newPw, newPwCon, session);

       if (!"SUCCESS".equals(result)) {
           model.addAttribute("errorMessage", result);
           return "thymeleaf/myPage/empMyNewPw"; // 비밀번호 변경 페이지로 이동
       }

       return "redirect:empMyPage"; // 성공 시 마이페이지로 리다이렉트
   }


   @GetMapping("empDelete")
   public String empDrop() {
      return "thymeleaf/myPage/empMyDrop";
   }

   @PostMapping("empDropOk")
   public String empDropOk(String empPw, HttpSession session, Model model) {
       String result = employeeDropService.execute(empPw, session);

       if (!"SUCCESS".equals(result)) {
           model.addAttribute("errorMessage", result);
           return "thymeleaf/myPage/empMyDrop"; // 에러 메시지와 함께 탈퇴 페이지로 이동
       }

       // 계정 삭제 성공 시 로그아웃
       session.invalidate();
       return "redirect:/login/logout";
   }

}