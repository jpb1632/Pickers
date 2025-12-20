package springBootPickers.service.inquire;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import springBootPickers.domain.InquireDTO;
import springBootPickers.domain.StartEndPageForInquireDTO;
import springBootPickers.mapper.InquireMapper;
import springBootPickers.service.StartEndPageServiceForInquire;

@Service
public class InquireListService {

	@Autowired
	InquireMapper inquireMapper;

	@Autowired
	StartEndPageServiceForInquire startEndPageServiceForInquire;

	// 직원용: 모든 문의 내역을 조회
	public void executeForEmployee(Integer page, String searchWord, String empNum, Model model) {
		int limit = 10; // 한 페이지에 보여줄 글 개수

		StartEndPageForInquireDTO sepfiDTO = startEndPageServiceForInquire.execute(page, limit, searchWord, empNum,
				"emp");
		List<InquireDTO> list = inquireMapper.inquireSelectList(sepfiDTO); // 모든 문의 내역 조회

		Integer count = inquireMapper.inquireCount(null, "emp"); // 직원은 모든 문의 내역의 개수 조회
		startEndPageServiceForInquire.execute(page, limit, count, searchWord, list, model); // 페이지 네비게이션 처리
	}

	// 회원용: 자신만의 문의 내역을 조회
//    public void executeForMember(Integer page, String searchWord, String memNum, Model model) {
//        int limit = 10;  // 한 페이지에 보여줄 글 개수
//
//        StartEndPageDTO sepDTO = startEndPageServiceForInquire.execute(page, limit, searchWord, memNum, "mem");
//        List<InquireDTO> list = inquireMapper.inquireSelectListForMember(sepDTO);  // 회원의 문의 내역 조회
//
//        Integer count = inquireMapper.inquireCount(memNum, "mem");  // 회원은 자신의 문의 내역의 개수 조회
//        startEndPageServiceForInquire.execute(page, limit, count, searchWord, list, model);  // 페이지 네비게이션 처리
//    }

	public void executeForMember(Integer page, String searchWord, String memNum, Model model) {
		if (memNum == null) {
			throw new IllegalArgumentException("회원 번호가 없습니다."); // 강제 예외 처리
		}

		int limit = 10; // 한 페이지에 보여줄 글 개수

		StartEndPageForInquireDTO sepfiDTO = startEndPageServiceForInquire.execute(page, limit, searchWord, memNum,
				"mem");
		List<InquireDTO> list = inquireMapper.inquireSelectListForMember(sepfiDTO); // 회원의 문의 내역 조회
		

	    // 디버깅: 각 문의 상태를 확인합니다.
	    for (InquireDTO dto : list) {
	        System.out.println("문의 번호: " + dto.getInquireNum() + ", 문의 상태: " + dto.getInquireStatus());
	    }
		
		model.addAttribute("list", list);
		  
		Integer count = inquireMapper.inquireCount(memNum, "mem"); // 회원은 자신의 문의 내역의 개수 조회
		startEndPageServiceForInquire.execute(page, limit, count, searchWord, list, model); // 페이지 네비게이션 처리
	}
}