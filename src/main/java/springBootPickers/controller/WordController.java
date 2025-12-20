package springBootPickers.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import springBootPickers.command.WordCommand;
import springBootPickers.domain.AuthInfoDTO;
import springBootPickers.domain.WordDTO;
import springBootPickers.service.word.WordDeleteService;
import springBootPickers.service.word.WordDetailService;
import springBootPickers.service.word.WordListService;
import springBootPickers.service.word.WordUpdateService;
import springBootPickers.service.word.WordWriteService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("word")
public class WordController {

	private static final Logger log = LoggerFactory.getLogger(WordController.class);

    @Autowired
    private WordListService wordListService;
    @Autowired
    private WordWriteService wordWriteService;
    @Autowired
    private WordDetailService wordDetailService;
    @Autowired
    private WordUpdateService wordUpdateService;
    @Autowired
    private WordDeleteService wordDeleteService;
  
    @GetMapping("/wordSearch")
    public String wordSearch(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page,
            Model model) {

        int pageSize = 10; 
        int totalWords = wordListService.countWords(keyword);
        int totalPages = (int) Math.ceil((double) totalWords / pageSize);

        int maxPageNumbers = 5; 
        int startPage = ((page - 1) / maxPageNumbers) * maxPageNumbers + 1;
        int endPage = Math.min(startPage + maxPageNumbers - 1, totalPages);

        model.addAttribute("totalWords", totalWords);
        model.addAttribute("words", wordListService.getWords(keyword, page, pageSize));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("keyword", keyword);

        return "thymeleaf/word/wordSearch";
    }
    
    // 직원용 검색 및 리스트
    @GetMapping("/wordList")
    public String wordList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "searchWord", required = false) String searchWord,
            Model model) {

        wordListService.executeForAdmin(page, searchWord, model); // 직원 검색 실행
        return "thymeleaf/word/wordList";
    }
    

    @GetMapping("/wordWrite")
    public String wordForm(Model model, HttpSession session) {
        // 자동 부여될 용어 번호
        String lastWordNum = wordListService.getLastWordNum(); 
        if (lastWordNum == null) {
            lastWordNum = "word_100000"; 
        }

      
        int lastNum = Integer.parseInt(lastWordNum.replaceAll("[^0-9]", ""));
        String nextWordNum = "word_" + (lastNum + 1);

        WordCommand wordCommand = new WordCommand();
        wordCommand.setWordNum(nextWordNum);
        wordCommand.setWordRegisterDate(new Date()); 
       
        
        AuthInfoDTO authInfo = (AuthInfoDTO) session.getAttribute("auth");
        if (authInfo != null) {
            log.debug("DEBUG: 세션의 authInfo -> {}", authInfo);

            // 직원 번호를 데이터베이스에서 조회
            if ("emp".equals(authInfo.getGrade())) {
                String empNum = wordListService.getEmployeeNumber(authInfo.getUserId());
                if (empNum != null) {
                    wordCommand.setEmpNum(empNum);
                } else {
                    wordCommand.setEmpNum("알 수 없음");
                }
            } else {
                wordCommand.setEmpNum("알 수 없음");
            }
        } else {
            log.debug("DEBUG: 세션에 authInfo가 설정되지 않았습니다.");
            wordCommand.setEmpNum("알 수 없음");
        }

        
        model.addAttribute("wordCommand", wordCommand);
        return "thymeleaf/word/wordWrite";
    }



    @PostMapping("/wordWrite")
    public String wordWrite(@Validated WordCommand wordCommand, BindingResult result) {
        if (result.hasErrors()) {
            return "thymeleaf/word/wordWrite";
        }
        wordWriteService.execute(wordCommand);
        return "redirect:/word/wordList";
    }


    @GetMapping("/wordDetail/{wordNum}")
    public String wordDetail(@PathVariable("wordNum") String wordNum, Model model) {
        wordDetailService.execute(wordNum, model);
        log.debug("DEBUG: Model contains wordCommand -> {}", model.containsAttribute("wordCommand"));
        return "thymeleaf/word/wordDetail";
    }


    @GetMapping("/wordUpdate/{wordNum}")
    public String showUpdateForm(@PathVariable("wordNum") String wordNum, Model model) {
        WordDTO wordDTO = wordDetailService.getWordDetail(wordNum);
        model.addAttribute("wordCommand", wordDTO);
        return "thymeleaf/word/wordUpdate";
    }

    @PostMapping("/wordUpdate")
    public String wordUpdate(@Validated WordCommand wordCommand, BindingResult result) {
        log.debug("WordCommand: {}", wordCommand);
        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> log.debug("Validation error: {}", error));
            return "thymeleaf/word/wordUpdate";
        }
        wordUpdateService.execute(wordCommand);
        return "redirect:/word/wordDetail/" + wordCommand.getWordNum();
    }
    @GetMapping("/wordDelete/{wordNum}")
    public String wordDeleteGet(@PathVariable("wordNum") String wordNum) {
        wordDeleteService.execute(wordNum);
        return "redirect:/word/wordList"; 
    }
    
    // 단일 삭제 처리
    @PostMapping("/wordDelete")
    public String wordDelete(@RequestParam("wordNum") String wordNum) {
        wordDeleteService.execute(wordNum);
        return "redirect:/word/wordList";
    }
   

    @PostMapping("/deleteSelected")
    @ResponseBody
    public ResponseEntity<String> deleteSelected(@RequestBody List<String> wordNums) {
        wordNums.forEach(wordDeleteService::execute);
        return ResponseEntity.ok("삭제 성공");
    }

}