package springBootPickers.service.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import springBootPickers.domain.AuthInfoDTO;
import springBootPickers.mapper.ItemMapper;
import springBootPickers.mapper.MemberMapper;

@Service
public class LectureWishDelService {
    @Autowired
    MemberMapper memberMapper; // 사용자 정보 조회
    @Autowired
    ItemMapper itemMapper; // 찜 데이터 관리

    public String execute(String[] lectureNums, HttpSession session) {
        AuthInfoDTO auth = (AuthInfoDTO) session.getAttribute("auth");
        String memNum = memberMapper.memberNumSelect(auth.getUserId());

        // 조건 준비
        List<String> lectureNumList = new ArrayList<>();
        for (String lectureNum : lectureNums) {
            lectureNumList.add(lectureNum);
        }

        Map<String, Object> condition = new HashMap<>();
        condition.put("memNum", memNum);
        condition.put("lectureNums", lectureNumList);

        // 삭제 실행
        int result = itemMapper.wishDelete(condition);
        return result > 0 ? "200" : "000";
    }
}
