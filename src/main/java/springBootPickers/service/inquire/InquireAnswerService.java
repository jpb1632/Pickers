package springBootPickers.service.inquire;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import springBootPickers.command.InquireCommand;
import springBootPickers.domain.InquireDTO;
import springBootPickers.mapper.InquireMapper;
import springBootPickers.service.AutoNumService;

@Service
public class InquireAnswerService {

	@Autowired
	InquireMapper inquireMapper;
	@Autowired
	AutoNumService autoNumService;

	public void execute(InquireCommand inquireCommand, HttpSession session) {
	    InquireDTO dto = new InquireDTO();
	    dto.setInquireNum(inquireCommand.getInquireNum()); 
	    dto.setAnswerContent(inquireCommand.getAnswerContent());
	    dto.setAnswerNum(inquireCommand.getAnswerNum());
	    dto.setAnswerRegisterDate(inquireCommand.getAnswerRegisterDate());
	    dto.setEmpNum(inquireCommand.getEmpNum());

	    // 세션에서 MEM_NUM 가져오기 (회원의 경우)
	    String memNum = inquireCommand.getMemNum();  // inquireCommand로 전달된 mem_num 사용
	    if (memNum != null) {
	        dto.setMemNum(memNum);  // mem_num이 있으면 설정
	    } else {
	        // 직원이 작성하는 경우에는 mem_num을 설정하지 않음
	        // 직원이 답변을 작성하는 경우, mem_num을 아예 설정하지 않음.
	         dto.setMemNum(null); // 이 코드는 제거!
	    }

	    // 자동 생성된 inquireNum을 세팅
//	    String inquireNum = autoNumService.execute("inquire_", "inquire_num", 9, "inquire");
//	    dto.setInquireNum(inquireNum);

	    System.out.println("DTO inquireNum 값: " + dto.getInquireNum()); // 디버깅용 로그
	    // 답변 내용 저장 전 로그 출력
	    System.out.println("답변 등록 전, 문의 상태: " + inquireCommand.getInquireStatus());

	    
	    // 답변 저장
	    inquireMapper.inquireAnswer(dto); // DB에 답변 저장
	    
	    inquireMapper.updateInquireStatusToAnswered(inquireCommand.getInquireNum());
	    
	    // 상태 업데이트 후 로그
	    System.out.println("답변 등록 후, 문의 상태: " + inquireCommand.getInquireStatus());
	}


}