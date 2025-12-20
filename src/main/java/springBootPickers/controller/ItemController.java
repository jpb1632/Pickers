package springBootPickers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import springBootPickers.command.CartCommand;
import springBootPickers.service.item.CartInsertService;
import springBootPickers.service.item.CartListService;
import springBootPickers.service.item.LectureCartDelService;
import springBootPickers.service.item.LectureWishDelService;
import springBootPickers.service.item.LectureWishListService;

@Controller
@RequestMapping("item")
public class ItemController {
	@Autowired
	LectureWishListService lectureWishListService;
	@Autowired
	CartListService cartListService;
	@Autowired
	LectureCartDelService lectureCartDelService;
	@Autowired
	CartInsertService cartInsertService;
	@Autowired
	LectureWishDelService lectureWishDelService;
	
	@GetMapping("wishList")
	public String wishList(HttpSession session, Model model) {
		lectureWishListService.execute(session, model);
		return "thymeleaf/wish/wishList";
	}
	@GetMapping("wishDel")
	public String wishDel(String[] lectureNums, HttpSession session) {
	    lectureWishDelService.execute(lectureNums, session);
	    return "redirect:wishList";
	}

	@RequestMapping("cartList")
	   public String cartList(Model model, HttpSession session) {
	      cartListService.execute(model, session);
	      return "thymeleaf/cart/cartList";
	   }
	@GetMapping("cartDel")
	public String cartDel(String lectureNums[] , HttpSession session) {
		lectureCartDelService.execute(lectureNums, session);
		return "redirect:cartList";
	}
	@GetMapping("buyItem")
	public String buyItem(CartCommand cartCommand, HttpSession session, Model model) {
	    String result = cartInsertService.execute(cartCommand, session);

	    // 결과 처리
	    if (result.startsWith("EXISTS-")) {
	        String lectureNum = result.split("-")[1];
	        return "redirect:/orders/goodsBuy?nums=" + lectureNum;
	    } else if ("200".equals(result)) {
	        return "redirect:/orders/goodsBuy?nums=" + cartCommand.getLectureNum();
	    } else {
	        return "redirect:/item/detailView/" + cartCommand.getLectureNum();
	    }
	}


}
