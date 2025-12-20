package springBootPickers.service.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import springBootPickers.command.CartCommand;
import springBootPickers.domain.AuthInfoDTO;
import springBootPickers.domain.CartDTO;
import springBootPickers.mapper.CartMapper;
import springBootPickers.mapper.MemberMapper;

@Service
public class CartInsertService {
    @Autowired
    MemberMapper memberMapper;
    @Autowired
    CartMapper cartMapper;

    public String execute(CartCommand cartCommand, HttpSession session) {
        AuthInfoDTO auth = (AuthInfoDTO) session.getAttribute("auth");
        if (auth == null) {
            return "000"; // 로그아웃 상태
        }

        String memNum = memberMapper.memberNumSelect(auth.getUserId());
        if (memNum == null) {
            return "900"; // 관리자 계정
        }
        

        // 장바구니 중복 확인
        int count = cartMapper.isLectureInCart(memNum, cartCommand.getLectureNum());
        if (count > 0) {
            // 이미 장바구니에 있는 경우 상품 번호 반환
            return "EXISTS-" + cartCommand.getLectureNum();
        }

        // 장바구니에 추가
        CartDTO dto = new CartDTO();
        dto.setCartQty(cartCommand.getQty());
        dto.setLectureNum(cartCommand.getLectureNum());
        dto.setMemNum(memNum);
        cartMapper.cartInsert(dto);

        return "200"; // 성공
    }
}
