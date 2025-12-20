package springBootPickers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import springBootPickers.command.MemberCommand;
import springBootPickers.service.AutoNumService;
import springBootPickers.service.member.MemberDeleteService;
import springBootPickers.service.member.MemberDetailService;
import springBootPickers.service.member.MemberListService;
import springBootPickers.service.member.MemberUpdateService;
import springBootPickers.service.member.MemberWriteService;
import springBootPickers.service.member.MembersDeleteService;
import springBootPickers.service.myPage.MemberPwUpdateService;

@Controller
@RequestMapping("member")
public class MemberController {
	@Autowired
	MemberWriteService memberWriteService;
	@Autowired
	AutoNumService autoNumService;
	@Autowired
	MemberListService memberListService;
	@Autowired
	MembersDeleteService membersDeleteService;
	@Autowired
	MemberDetailService memberDetailService;
	@Autowired
	MemberUpdateService memberUpdateService;
	@Autowired
	MemberDeleteService memberDeleteService;
	@Autowired
	MemberPwUpdateService memberPwUpdateService;

	@GetMapping("memList")
	public String memList(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "searchWord", required = false) String searchWord, Model model) {
		memberListService.execute(page, searchWord, model);
		return "thymeleaf/member/memList";
	}

	@GetMapping("memWrite")
	public String memForm(Model model) {
		String autoNum = autoNumService.execute("mem_", "mem_num", 5, "member");
		MemberCommand memberCommand = new MemberCommand();
		memberCommand.setMemNum(autoNum);
		model.addAttribute("memberCommand", memberCommand);
		return "thymeleaf/member/memForm";
	}

	@PostMapping("memRegist")
	public String memRegist(@Validated MemberCommand memberCommand, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "thymeleaf/member/memForm";
		}
		if (!memberCommand.isMemPwEqualMemPwCon()) {
			// model.addAttribute("errPw", "비밀번호가 일치하지 않습니다.");
			result.rejectValue("memPwCon", "memberCommand.memPwCon", "비밀번호가 일치하지 않습니다.");
			return "thymeleaf/member/memForm";
		}
		memberWriteService.execute(memberCommand);
		//return "thymeleaf/member/memFinished";
		return "redirect:memList";
	}

	@RequestMapping(value = "memDelete")
	private String memDelete(@RequestParam("nums") String memNums[]) {
		membersDeleteService.execute(memNums);
		return "redirect:memList";

	}

	@GetMapping("memDetail/{memNum}")
	public String memDtail(@PathVariable("memNum") String memNum, Model model) {
		memberDetailService.execute(memNum, model);
		return "thymeleaf/member/memInfo";
	}

	@GetMapping("memUpdate")
	public String memUpdate(String memNum, Model model) {
		memberDetailService.execute(memNum, model);
		return "thymeleaf/member/memModify";
	}

	@PostMapping("memUpdate")
	public String memUpdate(@Validated MemberCommand memberCommand, BindingResult result) {
		if (result.hasErrors()) {
			return "thymeleaf/member/memModify";
		}
		memberUpdateService.execute(memberCommand);
		return "redirect:memDetail/" + memberCommand.getMemNum();
	}

	@GetMapping("memDelete/{memNum}")
	public String memDelete(@PathVariable("memNum") String memNum) {
		memberDeleteService.execute(memNum);
		return "redirect:../memList";
	}

}
