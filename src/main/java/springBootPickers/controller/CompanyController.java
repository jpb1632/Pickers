package springBootPickers.controller;

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
import springBootPickers.command.CompanyCommand;
import springBootPickers.domain.CompanyDTO;
import springBootPickers.service.AutoNumService;
import springBootPickers.service.company.CompanyDeleteService;
import springBootPickers.service.company.CompanyDetailService;
import springBootPickers.service.company.CompanyListService;
import springBootPickers.service.company.CompanyUpdateService;
import springBootPickers.service.company.CompanyWriteService;
import springBootPickers.service.company.CompanysDeleteService;

@Controller
@RequestMapping("company")
public class CompanyController {
	@Autowired
	AutoNumService autoNumService;

	@Autowired
	CompanyListService companyListService;

	@Autowired
	CompanyWriteService companyWriteService;

	@Autowired
	CompanyDetailService companyDetailService;

	@Autowired
	CompanyUpdateService companyUpdateService;

	@Autowired
	CompanysDeleteService companysDeleteService;

	@Autowired
	CompanyDeleteService companyDeleteService;

	@GetMapping("companyListM")
	public String companyListM(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "searchWord", required = false) String searchWord, Model model) {

		CompanyCommand companyCommand = new CompanyCommand();
		model.addAttribute("companyCommand", companyCommand);

		CompanyDTO dto = new CompanyDTO();
		model.addAttribute("dto", dto);

		companyListService.execute(page, searchWord, model);
		return "thymeleaf/company/companyListM";
	}

	@GetMapping("companyList")
	public String companyList(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "searchWord", required = false) String searchWord, Model model) {
		companyListService.execute(page, searchWord, model);
		return "thymeleaf/company/companyList"; // 직원 전용 페이지
	}

	@GetMapping("companyWrite")
	public String companyWrite(HttpSession session, Model model) {
		String autoNum = autoNumService.execute("company_", "company_num", 9, "company");
		CompanyCommand companyCommand = new CompanyCommand();
		companyCommand.setCompanyNum(autoNum);
		model.addAttribute("companyCommand", companyCommand);
		return "thymeleaf/company/companyWrite";
	}

	@PostMapping("companyWrite")
	public String companyWrite(@Validated CompanyCommand companyCommand, BindingResult result, HttpSession session,
			Model model) {
		 if (result.hasErrors()) {
		        return "thymeleaf/company/companyWrite";  // 오류가 있으면 다시 폼으로 돌아감
		    }
		companyWriteService.execute(companyCommand, session);
		return "redirect:companyList";
	}

	@GetMapping("companyDetail/{companyNum}")
	public String companyDetail(@PathVariable("companyNum") String companyNum, Model model) {
//		companyDetailService.execute(companyNum, model);
//		return "thymeleaf/company/companyDetail";
		CompanyDTO dto = companyDetailService.execute(companyNum, model);

		if (dto == null) {
			// 데이터가 없을 경우, 사용자에게 오류 메시지를 표시할 수 있습니다.
			model.addAttribute("error", "해당 회사 정보를 찾을 수 없습니다.");
			return "thymeleaf/inquire/errorPage"; // 오류 페이지로 리다이렉트
		}

		model.addAttribute("companyCommand", dto);
		model.addAttribute("empName", dto.getEmpName());
		return "thymeleaf/company/companyDetail";
	}

	@GetMapping("companyUpdate")
	public String companyUpdate(@RequestParam("companyNum") String companyNum, Model model) {
		companyDetailService.execute(companyNum, model);
		return "thymeleaf/company/companyUpdate";
	}

	@PostMapping("companyUpdate")
	public String companyUpdate(@Validated CompanyCommand companyCommand, BindingResult result, HttpSession session,
			Model model) {
		if (result.hasErrors()) {
			return "thymeleaf/company/companyUpdate";
		}
		companyUpdateService.execute(companyCommand, session, model);
		return "redirect:/company/companyDetail/" + companyCommand.getCompanyNum(); // 답변 후 상세 페이지로 리다이렉트
	}

	@RequestMapping(value = "companyDelete")
	private String companyDelete(@RequestParam("nums") String companyNums[]) {
		companysDeleteService.execute(companyNums);
		return "redirect:companyList";
	}

	@GetMapping("companyDelete/{companyNum}")
	public String companyDelete(@PathVariable("companyNum") String companyNum) {
		companyDeleteService.execute(companyNum);
		return "redirect:../companyList";
	}

}