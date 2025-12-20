package springBootPickers.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import springBootPickers.command.NewsCommand;
import springBootPickers.domain.NewsDTO;
import springBootPickers.service.AutoNumService;
import springBootPickers.service.news.NewsDeleteService;
import springBootPickers.service.news.NewsDetailService;
import springBootPickers.service.news.NewsListService;
import springBootPickers.service.news.NewsUpdateService;
import springBootPickers.service.news.NewsWriteService;
import springBootPickers.service.news.NewssDeleteService;

@Controller
@RequestMapping("news")
public class NewsController {

	@Autowired
	NewsWriteService newsWriteService;
	@Autowired
	AutoNumService autoNumService;
	@Autowired
	NewsListService newsListService;
	@Autowired
	NewssDeleteService newssDeleteService;
	@Autowired
	NewsDeleteService newsDeleteService;
	@Autowired
	NewsDetailService newsDetailService;
	@Autowired
	NewsUpdateService newsUpdateService;

	@RequestMapping("newsListM")
	public String newsListM(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "searchWord", required = false) String searchWord, Model model) {
		NewsCommand newsCommand = new NewsCommand(); // 또는 필요한 데이터를 설정합니다.
		model.addAttribute("newsCommand", newsCommand);

		NewsDTO dto = new NewsDTO();
		model.addAttribute("dto", dto);

		newsListService.execute(page, searchWord, model);
		return "thymeleaf/news/newsListM";
	}

	@GetMapping("newsList")
	public String newsList(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "searchWord", required = false) String searchWord, Model model) {
		newsListService.execute(page, searchWord, model);
		return "thymeleaf/news/newsList";
	}
	

	@GetMapping("newsWrite")
	public String newsWrite(HttpSession session, Model model) {
		String autoNum = autoNumService.execute("news_", "news_num", 6, "news");
		NewsCommand newsCommand = new NewsCommand();
		newsCommand.setNewsNum(autoNum);
		LocalDate sysDate = LocalDate.now();

		model.addAttribute("newsRegisterDate", sysDate);
		model.addAttribute("newsCommand", newsCommand);

		return "thymeleaf/news/newsWrite";
	}

	@PostMapping("newsWrite")
	public String newsWrite(@Validated NewsCommand newsCommand, BindingResult result, HttpSession session,
			Model model) {
		newsWriteService.execute(newsCommand, session);
		return "redirect:newsList";
	}

	@RequestMapping(value = "newsDelete")
	private String newsDelete(@RequestParam("nums") String newsNums[]) {
		newssDeleteService.execute(newsNums);
		return "redirect:newsList";

	}

//	@GetMapping("newsDelete/{newsNum}")
//	public String newsDelete(@PathVariable("newsNum") String newsNum) {
//	    newsDeleteService.execute(newsNum);
//	    return "redirect:newsList";
//	}

	@GetMapping("newsDelete/{newsNum}")
	public String newsDelete(@PathVariable("newsNum") String newsNum) {
	    System.out.println("삭제 요청된 뉴스 번호: " + newsNum);

	    if (newsNum == null || newsNum.trim().isEmpty()) {
	        System.out.println("잘못된 뉴스 번호 요청!");
	        return "redirect:/news/newsList";
	    }

	    // 만약 newsNum이 예상과 다르게 'newsList'라면 차단
	    if (newsNum.equalsIgnoreCase("newsList")) {
	        System.out.println("잘못된 뉴스 삭제 요청!");
	        return "redirect:/news/newsList";
	    }

	    newsDeleteService.execute(newsNum);
	    return "redirect:/news/newsList";
	}



	@GetMapping("newsDetail/{newsNum}")
	public String newsDetail(@PathVariable("newsNum") String newsNum, Model model) {
		newsDetailService.execute(newsNum, model);
		return "thymeleaf/news/newsDetail";
	}

	@GetMapping("newsUpdate")
	public String newsUpdate(String newsNum, Model model) {
		LocalDate sysDate = LocalDate.now();

		model.addAttribute("newsRegisterDate", sysDate);
		newsDetailService.execute(newsNum, model);
		return "thymeleaf/news/newsUpdate";
	}

	@PostMapping("newsUpdate")
	public String newsUpdate(@Validated NewsCommand newsCommand, BindingResult result, HttpSession session,
			Model model) {
		if (result.hasErrors()) {
			return "thymeleaf/news/newsUpdate";
		}
		newsUpdateService.execute(newsCommand, session, model);
		return "redirect:newsDetail/" + newsCommand.getNewsNum();
	}

}