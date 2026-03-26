package springBootPickers.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        model.addAttribute("statusCode", statusCode);
        if (statusCode != null) {
            switch (statusCode) {
                case 404:
                    model.addAttribute("errorMessage", "페이지를 찾을 수 없습니다.");
                    break;
                case 500:
                    model.addAttribute("errorMessage", "서버 내부 오류가 발생했습니다.");
                    break;
                default:
                    model.addAttribute("errorMessage", "알 수 없는 오류가 발생했습니다.");
            }
        }

        return "thymeleaf/error";
    }
}
