package springBootPickers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import springBootPickers.domain.LectureDTO;
import springBootPickers.service.item.CartListService;
import springBootPickers.service.item.LectureDetailViewService;
import springBootPickers.service.lectureInquire.LectureInquireListService;

@Controller
@RequestMapping("item")
public class CornerController {
	@Autowired
	LectureDetailViewService lectureDetailViewService;
	@Autowired
	CartListService cartListService;
	@Autowired
	LectureInquireListService lectureInquireListService;

	@GetMapping("detailView/{lectureNum}")
	public String detailView(@PathVariable("lectureNum") String lectureNum, Model model, HttpSession session) {
		lectureDetailViewService.execute(lectureNum, model, session);
		return "thymeleaf/item/detailView";
	}

	@GetMapping("lectureInquireListDetailView/{lectureNum}")
	public String lectureInquireListDetailView(@PathVariable("lectureNum") String lectureNum,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "searchWord", required = false, defaultValue = "") String searchWord, Model model,
			HttpSession session) {
		System.out.println("Received lectureNum: " + lectureNum); // 디버깅 로그

		// 강의 상세 정보 조회
		lectureDetailViewService.execute(lectureNum, model, session);

		// 모델에 dto가 추가되었는지 확인
		LectureDTO dto = (LectureDTO) model.getAttribute("dto");
		System.out.println("LectureDTO: " + dto); // 디버깅 로그

		// 해당 강의에 대한 질문 목록 조회 (페이지네비게이션 포함)
		lectureInquireListService.executeForLecture(page, searchWord, lectureNum, model);

		
		 // 로그 출력
	    System.out.println("LectureNum: " + lectureNum);
	    // 모델 추가
	    model.addAttribute("lectureNum", lectureNum);
		return "thymeleaf/item/lectureInquireListDetailView";
	}

}
