package springBootPickers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import springBootPickers.command.LectureCommand;
import springBootPickers.domain.AuthInfoDTO;
import springBootPickers.service.AutoNumService;
import springBootPickers.service.lecture.LectureDeleteService;
import springBootPickers.service.lecture.LectureDetailService;
import springBootPickers.service.lecture.LectureListService;
import springBootPickers.service.lecture.LectureUpdateService;
import springBootPickers.service.lecture.LectureWriteService;
import springBootPickers.service.lecture.MylectureListService;
import springBootPickers.service.lecture.ProductDeleteService;




@Controller
@RequestMapping("lecture")
public class LectureController {
	@Autowired
	LectureWriteService lectureWriteService;
	@Autowired
	AutoNumService autoNumService;
	@Autowired
	LectureListService lectureListService;
	@Autowired
	ProductDeleteService productDeleteService;
	@Autowired
	LectureDetailService lectureDetailService;
	@Autowired
	LectureUpdateService lectureUpdateService;
	@Autowired
	LectureDeleteService lectureDeleteService;
	@Autowired
	MylectureListService mylectureListService;

	
		
	@RequestMapping("emplectureList")
	public String emplectureList(@RequestParam(value="searchWord", required = false) String searchWord
			, @RequestParam(value="page", required = false, defaultValue = "1") int page
			, Model model) {
		lectureListService.execute(searchWord, page, model);
		return "thymeleaf/lecture/emplectureList";
	}
	
	@RequestMapping("lectureList")
	public String lecture(@RequestParam(value="searchWord", required = false) String searchWord
			, @RequestParam(value="page", required = false, defaultValue = "1") int page
			, Model model) {
		lectureListService.execute(searchWord, page, model);
		return "thymeleaf/lecture/lectureList";
	}
	@RequestMapping("lectureForm")
	public String lectureForm(Model model) {
		String autoNum = autoNumService.execute("lecture_", "lecture_num", 9, "lecture");
		LectureCommand lectureCommand = new LectureCommand();
		lectureCommand.setLectureNum(autoNum);
		model.addAttribute("lectureCommand", lectureCommand);
		return "thymeleaf/lecture/lectureForm";
	}
	
	@PostMapping("lectureWrite")
	public String lectureWrite(@Validated LectureCommand lectureCommand, HttpSession session) {
		lectureWriteService.execute(lectureCommand, session);
		return "thymeleaf/lecture/lectureRedirect";
		//return "redirect:emplectureList";
	}
	@PostMapping("lectureDelete")
	public String lectureDelete(@RequestParam(value = "nums", required = false) String[] nums) {
	    if (nums != null && nums.length > 0) {
	        productDeleteService.execute(nums);  // 선택 삭제 처리
	    }
	    return "redirect:emplectureList";
	}

	@RequestMapping("lectureDeleteOne/{lectureNum}")
	public String lectureDeleteOne(@PathVariable("lectureNum") String lectureNum) {
	    lectureDeleteService.execute(lectureNum);  // 단일 삭제 처리
	    return "redirect:../emplectureList";
	}

	@GetMapping("emplectureDetail")
	public String emplectureDetail(@RequestParam("lectureNum") String lectureNum
			,Model model, HttpSession session) {
		session.removeAttribute("fileList");
		lectureDetailService.execute(lectureNum, model);
		return "thymeleaf/lecture/empLectureInfo.html";
	}
	@GetMapping("lectureUpdate")
	public String lectureUpdate(@RequestParam("lectureNum") String lectureNum, Model model, HttpSession session) {
		session.removeAttribute("fileList");
		lectureDetailService.execute(lectureNum, model);
		return "thymeleaf/lecture/lectureModify";
	}
	@PostMapping("lectureUpdate")
	public String lectureUpdate(LectureCommand lectureCommand, HttpSession session, Model model) {
		lectureUpdateService.execute(lectureCommand, session, model);
		return "redirect:emplectureDetail?lectureNum="+lectureCommand.getLectureNum();
	}
	@RequestMapping("lectureDel/{lectureNum}")
	public String lectureDel(@PathVariable("lectureNum")String lectureNum) {
		lectureDeleteService.execute(lectureNum);
		return "redirect:../emplectureList";
	}
	
	@RequestMapping("mylectureList")
	public String mylectureList(
	        @RequestParam(value = "searchWord", required = false) String searchWord,
	        @RequestParam(value = "page", required = false, defaultValue = "1") int page,
	        Model model,
	        HttpSession session) {

	    AuthInfoDTO authInfo = (AuthInfoDTO) session.getAttribute("auth");
	    String memNum = authInfo.getMemNum();

	    mylectureListService.execute(memNum, searchWord, page, model);

	    return "thymeleaf/lecture/myLecture";
	}
	@GetMapping("lectureWatch")
	public String lectureWatch() {
		return "thymeleaf/lecture/lectureWatch";
	}

}
