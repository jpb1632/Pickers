package springBootPickers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import springBootPickers.service.EmailCheckService;
import springBootPickers.service.FileDelService;

@RestController
public class CheckRestController {
	@Autowired
	EmailCheckService emailCheckService;
	@Autowired
	FileDelService fileDelService;
	
	@PostMapping("/checkRest/userEmailCheck")
	public Integer emailCheck(String userEmail) {
		return emailCheckService.execute(userEmail);
	}
	@RequestMapping("/file/fileDel")
	public int fileDel(String orgFile, HttpSession session) {
		return fileDelService.execute(orgFile, session);
	}
}
