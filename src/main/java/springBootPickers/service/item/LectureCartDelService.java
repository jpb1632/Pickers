package springBootPickers.service.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import springBootPickers.domain.AuthInfoDTO;
import springBootPickers.mapper.CartMapper;
import springBootPickers.mapper.MemberMapper;

@Service
public class LectureCartDelService {
	@Autowired
	MemberMapper memberMapper;
	@Autowired
	CartMapper cartMapper;
	public String execute(String lectureNums[], HttpSession session) {
		AuthInfoDTO auth  = (AuthInfoDTO)session.getAttribute("auth");
		String memNum = memberMapper.memberNumSelect(auth.getUserId());
		List<String> cs  = new ArrayList<String>();
		for(String lectureNum : lectureNums) {
			cs.add(lectureNum);
		}
		Map<String, Object> condition = new HashMap<String, Object>(); 
		condition.put("memNum", memNum);
		condition.put("lectureNums", cs);
		int i = cartMapper.lectureNumsDelete(condition);
		if(i > 0)return "200";
		else return "000";
	}
}
