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

import springBootPickers.command.EmployeeCommand;
import springBootPickers.service.AutoNumService;
import springBootPickers.service.employee.EmployeeDeleteService;
import springBootPickers.service.employee.EmployeeDetailService;
import springBootPickers.service.employee.EmployeeListService;
import springBootPickers.service.employee.EmployeeUpdateService;
import springBootPickers.service.employee.EmployeeWriteService;
import springBootPickers.service.employee.EmployeesDeleteService;


@Controller
@RequestMapping("employee")
public class EmployeeController {
	@Autowired
	EmployeeWriteService employeeWriteService;
	@Autowired
	AutoNumService autoNumService;
	@Autowired
	EmployeeListService employeeListService;
	@Autowired
	EmployeesDeleteService employeesDeleteService;
	@Autowired
	EmployeeDetailService employeeDetailService;
	@Autowired
	EmployeeUpdateService employeeUpdateService;
	@Autowired
	EmployeeDeleteService employeeDeleteService;
	
	@GetMapping("empList")
	public String empList(@RequestParam(value="page", required = false, defaultValue = "1") Integer page
			,@RequestParam(value="searchWord", required = false) String searchWord
			, Model model) {
		employeeListService.execute(page, searchWord, model);
		return "thymeleaf/employee/empList";
	}
	
	@GetMapping("empWrite")
	public String empForm(Model model) {
		String autoNum = autoNumService.execute("emp_", "emp_num", 5, "employee");
		EmployeeCommand employeeCommand = new EmployeeCommand();
		employeeCommand.setEmpNum(autoNum);
		model.addAttribute("employeeCommand", employeeCommand);
		return "thymeleaf/employee/empForm";
	}
	@PostMapping("empRegist")
	public String empRegist(@Validated EmployeeCommand employeeCommand
			, BindingResult result, Model model) {
		if(result.hasErrors()) {
			return "thymeleaf/employee/empForm";
		}
		if(!employeeCommand.isEmpPwEqualEmpPwCon()) {
			//model.addAttribute("errPw", "비밀번호가 일치하지 않습니다.");
			result.rejectValue("empPwCon", "employeeCommand.empPwCon", "비밀번호가 일치하지 않습니다.");
			return "thymeleaf/employee/empForm";
		}
		employeeWriteService.execute(employeeCommand);
		return "thymeleaf/employee/empFinished";
	}
	@RequestMapping(value="empDelete")
	private String empDelete(@RequestParam("nums") String empNums[]) {
		employeesDeleteService.execute(empNums);
		return "redirect:empList";

	}
	@GetMapping("empDetail/{empNum}")
	public String empDtail(@PathVariable("empNum") String empNum, Model model) {
		employeeDetailService.execute(empNum, model);
		return "thymeleaf/employee/empInfo";
	}
	@GetMapping("empUpdate")
	public String empUpdate(String empNum, Model model) {
		employeeDetailService.execute(empNum, model);
		return "thymeleaf/employee/empModify";
	}
	@PostMapping("empUpdate")
	public String empUpdate(@Validated EmployeeCommand employeeCommand, BindingResult result) {
		if(result.hasErrors()) {
			return "thymeleaf/employee/empModify";
		}
		employeeUpdateService.execute(employeeCommand);
		return "redirect:empDetail/"+employeeCommand.getEmpNum();
	}
	@GetMapping("empDelete/{empNum}")
	public String empDelete(@PathVariable("empNum") String empNum) {
		employeeDeleteService.execute(empNum);
		return "redirect:../empList";
	}
	
}
