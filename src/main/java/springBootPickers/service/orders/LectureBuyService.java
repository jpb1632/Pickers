package springBootPickers.service.orders;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;
import springBootPickers.domain.AuthInfoDTO;
import springBootPickers.domain.LectureCartDTO;
import springBootPickers.domain.MemberDTO;
import springBootPickers.mapper.CartMapper;
import springBootPickers.mapper.MemberMapper;

@Service
public class LectureBuyService {
    @Autowired
    MemberMapper memberMapper;
    @Autowired
    CartMapper cartMapper;

    public void execute(String nums[], HttpSession session, Model model) {
        // 세션에서 사용자 인증 정보 가져오기
        AuthInfoDTO auth = (AuthInfoDTO) session.getAttribute("auth");
        if (auth == null) {
            // 사용자 인증 정보가 없으면 바로 리턴
            return;
        }

        // 회원 번호 가져오기
        String memNum = memberMapper.memberNumSelect(auth.getUserId());

        // 장바구니 데이터 처리
        List<LectureCartDTO> list = cartMapper.cartSelectList(memNum, nums);
        model.addAttribute("list", list);

        // 총 결제 금액 및 강의 번호 처리
        Integer sumPrice = 0;
        StringBuilder lectureNums = new StringBuilder();
        for (LectureCartDTO dto : list) {
            sumPrice += dto.getLectureDTO().getLecturePrice() * dto.getCartDTO().getCartQty();
            lectureNums.append(dto.getLectureDTO().getLectureNum()).append("-");
        }
        model.addAttribute("sumPrice", sumPrice);
        model.addAttribute("lectureNums", lectureNums.toString());

        // 회원 정보 추가
        MemberDTO memberInfo = memberMapper.memberSelectOne(memNum);
        if (memberInfo != null) {
            model.addAttribute("memberName", memberInfo.getMemName());
            model.addAttribute("memberPhone", memberInfo.getMemPhoneNum());
            model.addAttribute("memberEmail", memberInfo.getMemEmail());
        }
    }
}
