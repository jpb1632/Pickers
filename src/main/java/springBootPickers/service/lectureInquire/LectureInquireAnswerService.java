package springBootPickers.service.lectureInquire;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import springBootPickers.command.LectureInquireCommand;
import springBootPickers.domain.LectureInquireDTO;
import springBootPickers.mapper.LectureInquireMapper;
import springBootPickers.service.AutoNumService;

@Service
public class LectureInquireAnswerService {
	@Autowired
	LectureInquireMapper lectureInquireMapper;
	@Autowired
	AutoNumService autoNumService;

	public void execute(LectureInquireCommand lectureInquireCommand, HttpSession session) {
		LectureInquireDTO dto = new LectureInquireDTO();
		dto.setLectureQNum(lectureInquireCommand.getLectureQNum());
		dto.setLectureAContent(lectureInquireCommand.getLectureAContent());
		dto.setLectureANum(lectureInquireCommand.getLectureANum());
		dto.setLectureARegisterDate(lectureInquireCommand.getLectureARegisterDate());
		dto.setEmpNum(lectureInquireCommand.getEmpNum());

		// 세션에서 MEM_NUM 가져오기 (회원의 경우)
		String memNum = lectureInquireCommand.getMemNum(); // inquireCommand로 전달된 mem_num 사용
		if (memNum != null) {
			dto.setMemNum(memNum); // mem_num이 있으면 설정
		} else {
			// 직원이 작성하는 경우에는 mem_num을 설정하지 않음
			// 직원이 답변을 작성하는 경우, mem_num을 아예 설정하지 않음.
			dto.setMemNum(null); // 이 코드는 제거!
		}

		// 답변 저장
		lectureInquireMapper.lectureInquireAnswer(dto); // DB에 답변 저장

		lectureInquireMapper.updateLectureInquireStatusToAnswered(lectureInquireCommand.getLectureQNum());
	}
}