package springBootPickers.service.lectureInquire;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import springBootPickers.domain.LectureInquireDTO;
import springBootPickers.domain.StartEndPageForInquireDTO;
import springBootPickers.mapper.LectureInquireMapper;
import springBootPickers.service.StartEndPageService;
import springBootPickers.service.StartEndPageServiceForInquire;

@Service
public class LectureInquireListService {
	@Autowired
	LectureInquireMapper lectureInquireMapper;

	@Autowired
	StartEndPageServiceForInquire startEndPageServiceForInquire;

	@Autowired
	StartEndPageService startEndPageService;

	// 직원용: 모든 문의 내역을 조회
	public void executeForEmployee(Integer page, String searchWord, String empNum, Model model) {
		int limit = 10; // 한 페이지에 보여줄 글 개수

		StartEndPageForInquireDTO sepfiDTO = startEndPageServiceForInquire.execute(page, limit, searchWord, empNum,
				"emp");
		List<LectureInquireDTO> list = lectureInquireMapper.lectureInquireSelectList(sepfiDTO); // 모든 문의 내역 조회

		Integer count = lectureInquireMapper.lectureInquireCount(null, "emp"); // 직원은 모든 문의 내역의 개수 조회
		startEndPageServiceForInquire.execute(page, limit, count, searchWord, list, model); // 페이지 네비게이션 처리
	}

//	public void executeForMember(Integer page, String searchWord, String memNum, Model model) {
//		if (memNum == null) {
//			throw new IllegalArgumentException("회원 번호가 없습니다."); // 강제 예외 처리
//		}
//
//		int limit = 10; // 한 페이지에 보여줄 글 개수
//
//		StartEndPageForInquireDTO sepfiDTO = startEndPageServiceForInquire.execute(page, limit, searchWord, memNum,
//				"mem");
//		List<LectureInquireDTO> list = lectureInquireMapper.lectureInquireSelectListForMember(sepfiDTO); // 회원의 문의 내역 조회
//
//		// 디버깅: 각 문의 상태를 확인합니다.
//		for (LectureInquireDTO dto : list) {
//			System.out.println("질문 번호: " + dto.getLectureQNum() + ", 답변 상태: " + dto.getLectureAStatus());
//		}
//
//		model.addAttribute("list", list);
//
//		Integer count = lectureInquireMapper.lectureInquireCount(memNum, "mem"); // 회원은 자신의 문의 내역의 개수 조회
//		startEndPageServiceForInquire.execute(page, limit, count, searchWord, list, model); // 페이지 네비게이션 처리
//	}
	
	public void executeForMember(Integer page, String searchWord, String memNum, Model model) {
	    if (memNum == null) {
	        throw new IllegalArgumentException("회원 번호가 없습니다."); // 강제 예외 처리
	    }

	    int limit = 10; // 한 페이지에 보여줄 글 개수

	    // 페이지네이션에 필요한 DTO 객체 생성
	    StartEndPageForInquireDTO sepfiDTO = startEndPageServiceForInquire.execute(page, limit, searchWord, memNum, "mem");
	    List<LectureInquireDTO> list = lectureInquireMapper.lectureInquireSelectListForMember(sepfiDTO); // 회원의 문의 내역 조회

	    // 디버깅: 각 문의 상태를 확인합니다.
	    for (LectureInquireDTO dto : list) {
	        System.out.println("질문 번호: " + dto.getLectureQNum() + ", 답변 상태: " + dto.getLectureAStatus());
	    }

	    model.addAttribute("list", list);

	    Integer count = lectureInquireMapper.lectureInquireCount(memNum, "mem"); // 회원은 자신의 문의 내역의 개수 조회
	    startEndPageServiceForInquire.execute(page, limit, count, searchWord, list, model); // 페이지 네비게이션 처리
	}




	public void executeForGuest(Integer page, String searchWord, Model model) {
		int limit = 10; // 한 페이지에 보여줄 글 개수

		StartEndPageForInquireDTO sepfiDTO = startEndPageServiceForInquire.execute(page, limit, searchWord, null, null);
		List<LectureInquireDTO> list = lectureInquireMapper.lectureInquireSelectList(sepfiDTO); // 모든 문의 내역 조회

		Integer count = lectureInquireMapper.lectureInquireCount(null, null); // 직원은 모든 문의 내역의 개수 조회
		startEndPageServiceForInquire.execute(page, limit, count, searchWord, list, model); // 페이지 네비게이션 처리
	}

	public void executeForLecture(Integer page, String searchWord, String lectureNum, Model model) {
	    int limit = 10; // 한 페이지에 보여줄 글 개수

	    // 페이지네비게이션을 위한 DTO 설정
	    StartEndPageForInquireDTO sepfiDTO = startEndPageServiceForInquire.execute(page, limit, searchWord, lectureNum, "lecture");
	    System.out.println("sepfiDTO.getMemNum() : " + sepfiDTO.getMemNum());
	    // 강의 번호로 문의 목록 조회 (페이징 적용)
	    List<LectureInquireDTO> list = lectureInquireMapper.selectListByLectureNum(sepfiDTO);

	    model.addAttribute("list", list);

	    // 문의 개수 조회
	    Integer count = lectureInquireMapper.lectureInquireCountLecture(lectureNum, "lecture"); // 해당 강의의 문의 개수 조회

	    // 페이지네비게이션 처리
	    startEndPageServiceForInquire.execute(page, limit, count, searchWord, list, model);

	    // 디버깅 로그 추가
	    System.out.println("startPageNum: " + model.getAttribute("startPageNum"));
	    System.out.println("endPageNum: " + model.getAttribute("endPageNum"));
	    System.out.println("maxPage: " + model.getAttribute("maxPage"));
	}


}