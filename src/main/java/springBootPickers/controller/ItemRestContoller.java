package springBootPickers.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import springBootPickers.command.CartCommand;
import springBootPickers.service.item.CartInsertService;
import springBootPickers.service.item.CartQtyDownService;
import springBootPickers.service.item.LectureCartDelService;
import springBootPickers.service.item.LectureDelsService;
import springBootPickers.service.item.LectureWishService;

@RestController
@RequestMapping("item")
public class ItemRestContoller {
	@Autowired
	LectureWishService lectureWishService;
	@Autowired
	CartInsertService cartInsertService;
	@Autowired
	CartQtyDownService cartQtyDownService;
	@Autowired
	LectureCartDelService lectureCartDelService;
	@Autowired
	LectureDelsService lectureDelsService;
	
	@RequestMapping("wishItem")
	public void wishadd(@RequestBody Map<String, Object> map, HttpSession session) {
		lectureWishService.execute(map.get("lectureNum").toString(), session);
	}
	@RequestMapping("cartAdd")
	public String cartAdd(@RequestBody CartCommand cartCommand, HttpSession session) {
	    String result = cartInsertService.execute(cartCommand, session);
	    return result;
	}

	@GetMapping("cartQtyDown")
	public void cartQtyDown(String lectureNum, HttpSession session) {
		cartQtyDownService.execute(lectureNum, session);
	}
	@PostMapping("cartDels")
	public String cartDels(@RequestBody String lectureNums[], HttpSession session) {
		return lectureCartDelService.execute(lectureNums, session);
	}
	@PostMapping("wishDels")
	public String deleteWishlist(@RequestBody String[] lectureNums, HttpSession session) {
		return lectureDelsService.deleteWishlistItems(lectureNums, session);
	}
	
}
