package springBootPickers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import springBootPickers.command.LectureInquireCommand;
import springBootPickers.domain.LectureInquireDTO;
import springBootPickers.mapper.LectureInquireMapper;
import springBootPickers.service.AutoNumService;
import springBootPickers.service.lectureInquire.LectureInquireAnswerService;
import springBootPickers.service.lectureInquire.LectureInquireDeleteService;
import springBootPickers.service.lectureInquire.LectureInquireDetailService;
import springBootPickers.service.lectureInquire.LectureInquireListService;
import springBootPickers.service.lectureInquire.LectureInquireWriteService;
import springBootPickers.service.lectureInquire.LectureInquiresDeleteService;

@Controller
@RequestMapping("lectureInquire")
public class LectureInquireController {
	@Autowired
	AutoNumService autoNumService;

	@Autowired
	LectureInquireListService lectureInquireListService;

	@Autowired
	LectureInquireWriteService lectureInquireWriteService;

	@Autowired
	LectureInquireDetailService lectureInquireDetailService;

	@Autowired
	LectureInquireAnswerService lectureInquireAnswerService;

	@Autowired
	LectureInquireMapper lectureInquireMapper;

	@Autowired
	LectureInquiresDeleteService lectureInquiresDeleteService;

	@Autowired
	LectureInquireDeleteService lectureInquireDeleteService;

//	@GetMapping("lectureInquireListM")
//	public String lectureInquireListM(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
//			@RequestParam(value = "searchWord", required = false) String searchWord, HttpSession session, Model model) {
//		String memNum = (String) session.getAttribute("memNum");
//		if (memNum != null) {
//			lectureInquireListService.executeForMember(page, searchWord, memNum, model);
//		} else {
//			lectureInquireListService.executeForGuest(page, searchWord, model);
//		}
//		return "thymeleaf/lectureInquire/lectureInquireListM";
//	}
	@GetMapping("lectureInquireListM/{memNum}")
	public String lectureInquireListM(@PathVariable("memNum") String memNum,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "searchWord", required = false) String searchWord, Model model) {
		// memNum이 제대로 전달되었는지 확인
		System.out.println("회원번호: " + memNum);

		if (memNum != null) {
			lectureInquireListService.executeForMember(page, searchWord, memNum, model);
		} else {
			lectureInquireListService.executeForGuest(page, searchWord, model);
		}
		return "thymeleaf/lectureInquire/lectureInquireListM";
	}

	@GetMapping("lectureInquireList")
	public String lectureInquireList(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
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
			lectureInquireListService.executeForEmployee(page, searchWord, empNum, model); // 직원 전용 1:1 문의 내역 조회
		} else {
			lectureInquireListService.executeForMember(page, searchWord, memNum, model); // 회원 전용 1:1 문의 내역 조회
		}

		return "thymeleaf/lectureInquire/lectureInquireList"; // 직원 전용 페이지
	}

	@GetMapping("lectureInquireWrite")
	public String lectureInquireWrite(String lectureNum, HttpSession session, Model model) {
		String autoNum = autoNumService.execute("lectureQ_", "lecture_q_num", 10, "lecture_Q");
		LectureInquireCommand lectureInquireCommand = new LectureInquireCommand();
		lectureInquireCommand.setLectureQNum(autoNum);
		System.out.println("lectureNum : " + lectureNum);
		model.addAttribute("lectureInquireCommand", lectureInquireCommand);
		model.addAttribute("lectureNum", lectureNum);
		return "thymeleaf/lectureInquire/lectureInquireWrite";
	}

	@PostMapping("lectureInquireWrite")
	public String lectureInquireWrite(LectureInquireCommand lectureInquireCommand, HttpSession session, Model model) {
		// System.out.println("선택된 질문 종류: " + lectureInquireCommand.getLectureQType());
		String memNum = (String) session.getAttribute("memNum");

		if (memNum == null) {
			return "redirect:/login/login";
		}

		lectureInquireWriteService.execute(lectureInquireCommand, session);

		String lectureNum = lectureInquireCommand.getLectureNum();
		return "redirect:/item/lectureInquireListDetailView/" + lectureNum;
	}

//	@PostMapping("lectureInquireWrite")
//	public String lectureInquireWrite(
//	        @Validated LectureInquireCommand lectureInquireCommand,
//	        BindingResult result,
//	        HttpSession session,
//	        Model model) {
//	    // 유효성 검사 에러 처리
//	    if (result.hasErrors()) {
//	        result.getAllErrors().forEach(error -> {
//	            System.out.println("유효성 검증 오류: " + error.getDefaultMessage());
//	        });
//	        model.addAttribute("lectureNum", lectureInquireCommand.getLectureNum());
//	        return "thymeleaf/lectureInquire/lectureInquireWrite";
//	    }
//
//	    String memNum = (String) session.getAttribute("memNum");
//	    if (memNum == null) {
//	        return "redirect:/login/login";
//	    }
//
//	    lectureInquireWriteService.execute(lectureInquireCommand, session);
//	    return "redirect:/item/lectureInquireListDetailView/" + lectureInquireCommand.getLectureNum();
//	}

	@GetMapping("lectureInquireDetail/{lectureQNum}")
	public String lectureInquireDetail(@PathVariable("lectureQNum") String lectureQNum, Model model) {
		if (lectureQNum == null || lectureQNum.trim().isEmpty()) {
			throw new IllegalArgumentException("유효한 질문 번호가 없습니다.");
		}
		LectureInquireDTO dto = lectureInquireDetailService.execute(lectureQNum);

		System.out.println("질문 종류: " + dto.getLectureQType());
		model.addAttribute("lectureInquireCommand", dto);
		// model.addAttribute("memName", dto.getMemName());
		return "thymeleaf/lectureInquire/lectureInquireDetail";
	}

//	public String getMemberName(String memNum) {
//		if (memNum != null) {
//			return lectureInquireMapper.getMemberNameByMemNum(memNum); // mem_num을 통해 회원의 이름 조회
//		}
//		return "직원"; // 직원일 경우 기본적으로 "직원"으로 설정
//	}

//	@GetMapping("lectureInquireAnswer")
//	public String lectureInquireAnswer(@RequestParam("lectureQNum") String lectureQNum, Model model) {
//		LectureInquireDTO lectureInquire = lectureInquireDetailService.execute(lectureQNum);
//
//		String memNum = lectureInquire.getMemNum();
//		// String memName = getMemberName(memNum); // 여기서 회원 이름을 가져옵니다
//
//		model.addAttribute("lectureInquireCommand", lectureInquire);
//		// model.addAttribute("memName", memName); // 조회한 회원 이름을 모델에 추가
//
//		return "thymeleaf/lectureInquire/lectureInquireAnswer";
//	}
//
//	@PostMapping("lectureInquireAnswer")
//	public String lectureInquireAnswer(LectureInquireCommand lectureInquireCommand, HttpSession session, Model model) {
//		System.out.println("lectureQNum 값: " + lectureInquireCommand.getLectureQNum()); // 디버깅용 로그
//
//		lectureInquireAnswerService.execute(lectureInquireCommand, session);
//
//		return "redirect:/lectureInquire/lectureInquireDetail/" + lectureInquireCommand.getLectureQNum(); 
//	}
	
	@GetMapping("/lectureInquireAnswer")
    public String lectureInquireAnswer(@RequestParam("lectureQNum") String lectureQNum, Model model) {
        LectureInquireDTO lectureInquire = lectureInquireDetailService.execute(lectureQNum);
        model.addAttribute("lectureInquireCommand", lectureInquire);
        return "thymeleaf/lectureInquire/lectureInquireAnswer";
    }

    // 문의 답변을 처리하고 상세 페이지로 리다이렉트하는 POST 메서드
    @PostMapping("/lectureInquireAnswer")
    public String lectureInquireAnswer(@ModelAttribute LectureInquireCommand lectureInquireCommand, HttpSession session, RedirectAttributes redirectAttributes) {
        System.out.println("lectureQNum 값: " + lectureInquireCommand.getLectureQNum()); 
        lectureInquireAnswerService.execute(lectureInquireCommand, session);
        redirectAttributes.addFlashAttribute("successMessage", "답변이 성공적으로 등록되었습니다!");
        return "redirect:/lectureInquire/lectureInquireDetail/" + lectureInquireCommand.getLectureQNum();
    }

	@RequestMapping(value = "lectureInquireDelete")
	private String lectureInquireDelete(@RequestParam("nums") String lectureQNums[]) {
		lectureInquiresDeleteService.execute(lectureQNums);
		return "redirect:lectureInquireList";
	}

	@GetMapping("lectureInquireDelete/{lectureQNum}")
	public String lectureInquireDelete(@PathVariable("lectureQNum") String lectureQNum) {
		lectureInquireDeleteService.execute(lectureQNum);
		return "redirect:../lectureInquireList";
	}
}