package springBootPickers;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import springBootPickers.domain.AuthInfoDTO;

@Component
public class InterceptorConfig implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession();
        AuthInfoDTO auth = (AuthInfoDTO) session.getAttribute("auth");
        String uri = request.getRequestURI();

        // 비회원과 회원에게 공통으로 허용되는 주소
        if ("/help/findId".equals(uri) ||
            "/help/findPw".equals(uri) ||
            "/login/login".equals(uri) ||
            "/register/membershipAgree".equals(uri) ||
            "/lecture/lectureList".equals(uri) ||
            "/news/newsListM".equals(uri) ||
            "/company/companyListM".equals(uri) ||
            "/stock/stockListM".equals(uri) ||
            "/board/boardQueList".equals(uri) ||
            "/board/boardFreeList".equals(uri) ||
            "/board/boardProfitList".equals(uri) ||
            "/board/boardTipList".equals(uri) ||
            "/board/boardDetail".equals(uri) ||
            "/chart/list".equals(uri) ||
            "/chart/Detail".equals(uri) ||
            "/word/wordSearch".equals(uri)) {
            return true; // 허용된 URL
        }

        // 회원 전용 접근 제한
        if ("/myPage/memMyPage".equals(uri) ||
            "/lecture/mylectureList".equals(uri) ||
            "/item/cartList".equals(uri) ||
            "/item/wishList".equals(uri) ||
            "/inquire/inquireListM".equals(uri) ||
            "/lectureInquire/lectureInquireListM".equals(uri) ||
            "/orders/orderList".equals(uri) ||
            "/orders/paymentOk".equals(uri) ||
            "/inquire/inquireWrite".equals(uri)) {
            if (ObjectUtils.isEmpty(auth) || !"mem".equals(auth.getGrade())) { // 'member'로 확인
                response.sendRedirect("/login/login"); // 로그인 페이지로 리다이렉트
                return false;
            }
        }

        // 직원 전용 접근 제한
        if ("/myPage/empMyPage".equals(uri) ||
            "/lecture/emplectureList".equals(uri) ||
            "/orders/empOrderList".equals(uri) ||
            "/lecture/lectureForm".equals(uri) ||
            "/news/newsWrite".equals(uri) ||
            "/company/companyWrite".equals(uri) ||
            "/stock/stockWrite".equals(uri) ||
            "/word/wordWrite".equals(uri) ||
            "/employee/empWrite".equals(uri) ||
            "/member/memList".equals(uri) ||
            "/employee/empList".equals(uri) ||
            "/news/newsList".equals(uri) ||
            "/company/companyList".equals(uri) ||
            "/stock/stockList".equals(uri) ||
            "/board/boardDetail".equals(uri) ||
            "/word/wordList".equals(uri) ||
            "/inquire/inquireList".equals(uri)) {
            if (ObjectUtils.isEmpty(auth) || !"emp".equals(auth.getGrade())) { // 'employee'로 확인
                response.sendRedirect("/login/login"); // 로그인 페이지로 리다이렉트
                return false;
            }
        }

        // 접근 불가 URL
        if ("/register/membershipInfo".equals(uri) || 
            "/register/membershipFinished".equals(uri)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden
            return false;
        }

        return true; // 모든 조건을 통과하면 요청 진행
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
