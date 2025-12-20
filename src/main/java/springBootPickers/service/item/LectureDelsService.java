package springBootPickers.service.item;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import springBootPickers.domain.AuthInfoDTO;
import springBootPickers.mapper.ItemMapper;
import springBootPickers.mapper.MemberMapper;

@Service
public class LectureDelsService {
	@Autowired
    MemberMapper memberMapper;
    @Autowired
    ItemMapper itemMapper;
	public String deleteWishlistItems(String[] lectureNums, HttpSession session) {
		// 사용자 정보 확인
        AuthInfoDTO authInfo = (AuthInfoDTO) session.getAttribute("auth");
        if (authInfo == null) {
            return "401"; // Unauthorized
        }

        // 회원 번호 가져오기
        String memNum = memberMapper.memberNumSelect(authInfo.getUserId());

        // 삭제 조건 설정
        Map<String, Object> condition = new HashMap<>();
        condition.put("memNum", memNum);
        condition.put("lectureNums", lectureNums);

        // 로그로 확인
        System.out.println("삭제 조건: " + condition);

        // 삭제 실행
        int result = itemMapper.wishDelete(condition);
        return result > 0 ? "200" : "000"; // 삭제 성공 또는 실패
    }
}