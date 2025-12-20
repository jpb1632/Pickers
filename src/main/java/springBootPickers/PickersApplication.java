package springBootPickers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import springBootPickers.command.LoginCommand;

@SpringBootApplication
@Controller
public class PickersApplication {

	public static void main(String[] args) {
		SpringApplication.run(PickersApplication.class, args);
	}

	@GetMapping("/")
	public String index(LoginCommand loginCommand) {
		return "thymeleaf/index";
	}
	@RequestMapping("realData")	
	public String real() {
		return "thymeleaf/realData";
	}

}
