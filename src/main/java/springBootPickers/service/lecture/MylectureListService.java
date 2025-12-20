package springBootPickers.service.lecture;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import springBootPickers.domain.LectureDTO;
import springBootPickers.domain.StartEndPageDTO;
import springBootPickers.mapper.LectureMapper;
import springBootPickers.service.StartEndPageService;

@Service
public class MylectureListService {
    @Autowired
    LectureMapper lectureMapper;
    @Autowired
    StartEndPageService startEndPageService;

    public void execute(String memNum, String searchWord, int page, Model model) {
        int limit = 6; // 한 페이지에 표시할 강의 수

        // 페이징 처리를 위한 DTO 생성
        StartEndPageDTO sepDTO = startEndPageService.execute(page, limit, searchWord);

        // Map 생성하여 검색 조건과 페이징 정보 설정
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("memNum", memNum);
        paramMap.put("searchWord", searchWord);
        paramMap.put("startRow", sepDTO.getStartRow());
        paramMap.put("endRow", sepDTO.getEndRow());

        // 강의 리스트와 총 개수 가져오기
        List<LectureDTO> lectures = lectureMapper.paidLecturesWithPaging(paramMap);
        int count = lectureMapper.paidLectureCount(memNum, searchWord);

        // 모델에 데이터 추가
        model.addAttribute("lectures", lectures);
        model.addAttribute("searchWord", searchWord);
        model.addAttribute("page", page);
        model.addAttribute("maxPage", (int) Math.ceil((double) count / limit));

        // 페이지 네비게이션 범위 계산
        int startPageNum = ((page - 1) / 5) * 5 + 1;
        int endPageNum = Math.min(startPageNum + 4, (int) Math.ceil((double) count / limit));
        model.addAttribute("startPageNum", startPageNum);
        model.addAttribute("endPageNum", endPageNum);
    }
}
