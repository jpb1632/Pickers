package springBootPickers.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import springBootPickers.command.InquireCommand;
import springBootPickers.domain.InquireDTO;
import springBootPickers.mapper.InquireMapper;
import springBootPickers.service.AutoNumService;
import springBootPickers.service.inquire.InquireAnswerService;
import springBootPickers.service.inquire.InquireDeleteService;
import springBootPickers.service.inquire.InquireDetailService;
import springBootPickers.service.inquire.InquireListService;
import springBootPickers.service.inquire.InquireWriteService;
import springBootPickers.service.inquire.InquiresDeleteService;

@Controller
@RequestMapping("inquire")
public class InquireController {

	@Autowired
	AutoNumService autoNumService;

	@Autowired
	InquireListService inquireListService;

	@Autowired
	InquireWriteService inquireWriteService;

	@Autowired
	InquireDetailService inquireDetailService;

	@Autowired
	InquireAnswerService inquireAnswerService;

	@Autowired
	InquireMapper inquireMapper;

	@Autowired
	InquiresDeleteService inquiresDeleteService;

	@Autowired
	InquireDeleteService inquireDeleteService;

//	@GetMapping("inquireListM")
//	public String inquireListM(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
//			@RequestParam(value = "searchWord", required = false) String searchWord, HttpSession session, Model model) {
//		String memNum = (String) session.getAttribute("memNum");
//		String grade = (String) session.getAttribute("grade");
//
//		if (grade == null) {
//			grade = "mem"; // 기본값 설정 (회원 등급)
//		}
//
//		inquireListService.executeForMember(page, searchWord, memNum, model);
//		return "thymeleaf/inquire/inquireListM";
//	}
	@GetMapping("inquireListM")
	public String inquireListM(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "searchWord", required = false) String searchWord, HttpSession session, Model model) {
		String memNum = (String) session.getAttribute("memNum");
		if (memNum == null) {
			return "redirect:/login/login"; // 로그인하지 않은 경우
		}
		inquireListService.executeForMember(page, searchWord, memNum, model);

		// 리스트 출력 전에 상태 확인 로그 추가
		List<InquireDTO> list = (List<InquireDTO>) model.getAttribute("list");
		for (InquireDTO dto : list) {
			System.out.println("문의 번호: " + dto.getInquireNum() + ", 문의 상태: " + dto.getInquireStatus());
		}
		return "thymeleaf/inquire/inquireListM";
	}

	@GetMapping("inquireList")
	public String inquireList(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "searchWord", required = false) String searchWord, HttpSession session, Model model) {
		// 세션에서 회원 번호와 등급 가져오기
		String memNum = (String) session.getAttribute("memNum");
		String grade = (String) session.getAttribute("grade");
		String empNum = (String) session.getAttribute("empNum");

		// 세션 정보가 없으면 로그인 페이지로 리다이렉트
		if (memNum == null || grade == null) {
			return "redirect:/login/login"; // 로그인 페이지로 리다이렉트
		}

		// 직원이면 모든 문의 내역 조회, 회원이면 자신의 문의 내역만 조회
		if ("emp".equals(grade)) {
			inquireListService.executeForEmployee(page, searchWord, empNum, model); // 직원 전용 1:1 문의 내역 조회
		} else {
			inquireListService.executeForMember(page, searchWord, memNum, model); // 회원 전용 1:1 문의 내역 조회
		}

		return "thymeleaf/inquire/inquireList"; // 직원 전용 페이지
	}

	@GetMapping("inquireWrite")
	public String inquireWrite(HttpSession session, Model model) {
		String autoNum = autoNumService.execute("inquire_", "inquire_num", 9, "inquire");
		InquireCommand inquireCommand = new InquireCommand();
		inquireCommand.setInquireNum(autoNum);

		model.addAttribute("inquireCommand", inquireCommand);
		return "thymeleaf/inquire/inquireWrite";
	}

	@PostMapping("inquireWrite")
	public String inquireWrite(InquireCommand inquireCommand, BindingResult result, HttpSession session, Model model) {
		String memNum = (String) session.getAttribute("memNum");

		if (memNum == null) {
			return "redirect:/login/login";
		}

		inquireWriteService.execute(inquireCommand, session);
		return "redirect:/inquire/inquireListM";
	}

	@GetMapping("inquireDetail/{inquireNum}")
	public String inquireDetail(@PathVariable("inquireNum") String inquireNum, Model model) {
		if (inquireNum == null || inquireNum.trim().isEmpty()) {
			throw new IllegalArgumentException("유효한 문의 번호가 없습니다.");
		}

		InquireDTO dto = inquireDetailService.execute(inquireNum); // Service에서 DTO 반환

		if (dto == null) {
			// DTO가 null일 경우 처리 (예: 에러 메시지나 다른 페이지로 리다이렉트)
			model.addAttribute("errorMessage", "해당 문의가 존재하지 않습니다.");
			return "thymeleaf/inquire/errorPage"; // 에러 페이지로 리다이렉트
		}

		model.addAttribute("inquireCommand", dto);
		model.addAttribute("memName", dto.getMemName()); // 회원 이름만 별도로 추가
		return "thymeleaf/inquire/inquireDetail";
	}

	public String getMemberName(String memNum) {
		if (memNum != null) {
			return inquireMapper.getMemberNameByMemNum(memNum); // mem_num을 통해 회원의 이름 조회
		}
		return "직원"; // 직원일 경우 기본적으로 "직원"으로 설정
	}

//	@GetMapping("inquireAnswer")
//	public String inquireAnswer(@RequestParam("inquireNum") String inquireNum, Model model) {
//		InquireDTO inquire = inquireDetailService.execute(inquireNum);
//
//		String memNum = inquire.getMemNum();
//		String memName = getMemberName(memNum); // 여기서 회원 이름을 가져옵니다
//
//		model.addAttribute("inquireCommand", inquire);
//		model.addAttribute("memName", memName); // 조회한 회원 이름을 모델에 추가
//
//		return "thymeleaf/inquire/inquireAnswer";
//	}
//
//	@PostMapping("inquireAnswer")
//	public String inquireAnswer(@Validated InquireCommand inquireCommand, BindingResult result, HttpSession session,
//			Model model) {
//		if (result.hasErrors()) {
//			return "thymeleaf/inquire/inquireAnswer";
//		}
//
//		inquireAnswerService.execute(inquireCommand, session);
//		return "redirect:/inquire/inquireDetail/" + inquireCommand.getInquireNum();
//	}

	@GetMapping("/inquireAnswer")
	public String inquireAnswer(@RequestParam("inquireNum") String inquireNum, Model model) {
		InquireDTO inquire = inquireDetailService.execute(inquireNum);
		model.addAttribute("inquireCommand", inquire);
		return "thymeleaf/inquire/inquireAnswer";
	}

	@PostMapping("/inquireAnswer")
	public String inquireAnswer(@ModelAttribute InquireCommand inquireCommand, HttpSession session,	RedirectAttributes redirectAttributes) {
		inquireAnswerService.execute(inquireCommand, session);
		redirectAttributes.addFlashAttribute("successMessage", "답변이 성공적으로 등록되었습니다!");
		return "redirect:/inquire/inquireDetail/" + inquireCommand.getInquireNum();
	}

	@RequestMapping(value = "inquireDelete")
	private String inquireDelete(@RequestParam("nums") String inquireNums[]) {
		inquiresDeleteService.execute(inquireNums);
		return "redirect:inquireList";
	}

	@GetMapping("inquireDelete/{inquireNum}")
	public String inquireDelete(@PathVariable("inquireNum") String inquireNum) {
		inquireDeleteService.execute(inquireNum);
		return "redirect:../inquireList";
	}

}