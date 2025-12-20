package springBootPickers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import springBootPickers.command.MemberCommand;
import springBootPickers.service.AutoNumService;
import springBootPickers.service.memberJoin.MemberJoinService;

@Controller
@RequestMapping("register")
public class MemberJoinController {

	@Autowired
	MemberJoinService memberJoinService;

	@Autowired
	AutoNumService autoNumService;

	@RequestMapping("membershipAgree")
	public String membershipAgree() {
		return "thymeleaf/memberJoin/membershipAgree";
	}

	@GetMapping("membershipInfo")
	public String membershipInfo(Model model) {
		String autoNum = autoNumService.execute("mem_", "mem_num", 5, "member");
		MemberCommand memberCommand = new MemberCommand();
		memberCommand.setMemNum(autoNum);
		model.addAttribute("memberCommand", memberCommand);
		return "thymeleaf/memberJoin/membershipInfo";
	}

	@PostMapping("membershipInfo")
	public String membershipInfo1(@Validated MemberCommand memberCommand, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "thymeleaf/memberJoin/membershipInfo";
		}
		if (!memberCommand.isMemPwEqualMemPwCon()) {
			// model.addAttribute("errPw", "비밀번호가 일치하지 않습니다.");
			result.rejectValue("memPwCon", "memberCommand.memPwCon", "비밀번호가 일치하지 않습니다.");
			return "thymeleaf/memberJoin/membershipInfo";
		}
		memberJoinService.execute(memberCommand);
		return "thymeleaf/memberJoin/membershipFinished";
	}

	@GetMapping("membershipFinished")
	public String membershipFinished() {
		return "thymeleaf/memberJoin/membershipFinished";
	}
}
