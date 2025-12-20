package springBootPickers.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import springBootPickers.command.CompanyCommand;
import springBootPickers.command.StockCommand;
import springBootPickers.domain.CompanyDTO;
import springBootPickers.domain.StockDTO;
import springBootPickers.service.company.CompanyListService;
import springBootPickers.service.stock.StockDeleteService;
import springBootPickers.service.stock.StockDetailService;
import springBootPickers.service.stock.StockListService;
import springBootPickers.service.stock.StockUpdateService;
import springBootPickers.service.stock.StockWriteService;
import springBootPickers.service.stock.StocksDeleteService;

@Controller
@RequestMapping("stock")
public class StockController {
	@Autowired
	CompanyListService companyListService;

	@Autowired
	StockListService stockListService;

	@Autowired
	StockWriteService stockWriteService;

	@Autowired
	StockDetailService stockDetailService;

	@Autowired
	StockUpdateService stockUpdateService;

	@Autowired
	StocksDeleteService stocksDeleteService;

	@Autowired
	StockDeleteService stockDeleteService;

	@GetMapping("searchCompany")
	@ResponseBody
	public List<CompanyDTO> searchCompany(@RequestParam("companyName") String companyName) {
		 return companyListService.searchCompanies(companyName);
	}

	@GetMapping("stockListM")
	public String stockListM(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "searchWord", required = false) String searchWord, Model model) {
		StockCommand stockCommand = new StockCommand();
		model.addAttribute("stockCommand", stockCommand);

		StockDTO dto = new StockDTO();
		model.addAttribute("dto", dto);

		stockListService.execute(page, searchWord, model);
		return "thymeleaf/stock/stockListM";
	}

	@GetMapping("stockList")
	public String stockList(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "searchWord", required = false) String searchWord, Model model) {
		stockListService.execute(page, searchWord, model);
		return "thymeleaf/stock/stockList";
	}
	
	@GetMapping("stockWrite")
	public String stockWrite(HttpSession session, Model model) {
		StockCommand stockCommand = new StockCommand();
		model.addAttribute("stockCommand", stockCommand);
		return "thymeleaf/stock/stockWrite";
	}
	
	@PostMapping("stockWrite")
	public String stockWrite(@Validated StockCommand stockCommand, BindingResult result, HttpSession session,
			Model model) {
		if (result.hasErrors()) {
			return "thymeleaf/stock/stockWrite";
		}
		stockWriteService.execute(stockCommand, session);
		return "redirect:/stock/stockList";
	}
	
	@GetMapping("stockDetail/{stockNum}")
	public String stockDetail(@PathVariable("stockNum") String stockNum, Model model) {
		StockDTO dto = stockDetailService.execute(stockNum, model);

		if (dto == null) {
			// 데이터가 없을 경우, 사용자에게 오류 메시지를 표시할 수 있습니다.
			model.addAttribute("error", "해당 회사 정보를 찾을 수 없습니다.");
			return "thymeleaf/inquire/errorPage"; // 오류 페이지로 리다이렉트
		}

		model.addAttribute("stockCommand", dto);
		model.addAttribute("empName", dto.getEmpName());
		return "thymeleaf/stock/stockDetail";
	}

	@GetMapping("stockUpdate")
	public String stockUpdate(@RequestParam("stockNum") String stockNum, Model model) {
		stockDetailService.execute(stockNum, model);
		return "thymeleaf/stock/stockUpdate";
	}

	@PostMapping("stockUpdate")
	public String stockUpdate(@Validated StockCommand stockCommand, BindingResult result, HttpSession session,
			Model model) {
		if (result.hasErrors()) {
			return "thymeleaf/stock/stockUpdate";
		}
		stockUpdateService.execute(stockCommand, session, model);
		return "redirect:/stock/stockDetail/" + stockCommand.getStockNum(); // 답변 후 상세 페이지로 리다이렉트
	}

	@RequestMapping(value = "stockDelete")
	private String stockDelete(@RequestParam("nums") String stockNums[]) {
		stocksDeleteService.execute(stockNums);
		return "redirect:stockList";
	}

	@GetMapping("stockDelete/{stockNum}")
	public String stockDelete(@PathVariable("stockNum") String stockNum) {
		stockDeleteService.execute(stockNum);
		return "redirect:../stockList";
	}
	@GetMapping("companyList")
	public String companyList(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
	                          @RequestParam(value = "searchWord", required = false) String searchWord,
	                          Model model) {
	    companyListService.execute(page, searchWord, model);
	    return "thymeleaf/stock/companyList";
	}

}