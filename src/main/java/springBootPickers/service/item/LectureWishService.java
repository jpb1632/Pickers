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
public class LectureWishService {
	@Autowired
	MemberMapper memberMapper;
	@Autowired
	ItemMapper itemMapper;
	public void execute(String lectureNum, HttpSession session) {
		AuthInfoDTO auth = (AuthInfoDTO)session.getAttribute("auth");
		String memNum = memberMapper.memberNumSelect(auth.getUserId());
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("lectureNum", lectureNum);
		map.put("memNum", memNum);
		
		itemMapper.wishItem(map);
				
	}
}
