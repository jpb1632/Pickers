package springBootPickers.service.item;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;
import springBootPickers.domain.AuthInfoDTO;
import springBootPickers.domain.LectureDTO;
import springBootPickers.mapper.ItemMapper;
import springBootPickers.mapper.LectureMapper;
import springBootPickers.mapper.MemberMapper;

@Service
public class LectureDetailViewService {
    @Autowired
    LectureMapper lectureMapper;
    @Autowired
    ItemMapper itemMapper;

    public void execute(String lectureNum, Model model, HttpSession session) {
        // 강의 정보 조회
        LectureDTO dto = lectureMapper.lectureSelectOne(lectureNum);
        model.addAttribute("dto", dto);

        // 찜 상태 확인
        AuthInfoDTO auth = (AuthInfoDTO) session.getAttribute("auth");
        if (auth != null) {
            String memNum = auth.getMemNum();

            // 구매 여부 확인
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("lectureNum", lectureNum);
            paramMap.put("memNum", memNum);

            String payStatus = lectureMapper.checkLecturePurchased(paramMap);
            model.addAttribute("payStatus", payStatus);

            // 찜 상태 확인
            Integer wishCount = itemMapper.wishCountSelectOne(paramMap);
            model.addAttribute("wish", wishCount);
        } else {
            model.addAttribute("wish", 0);
            model.addAttribute("payStatus", null);
        }
    }
}


