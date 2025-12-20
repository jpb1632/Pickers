package springBootPickers.service.lectureInquire;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import springBootPickers.command.LectureInquireCommand;
import springBootPickers.domain.LectureInquireDTO;
import springBootPickers.mapper.LectureInquireMapper;

@Service
public class LectureInquireWriteService {
    @Autowired
    LectureInquireMapper lectureInquireMapper;

    public void execute(LectureInquireCommand lectureInquireCommand, HttpSession session) {
        LectureInquireDTO dto = new LectureInquireDTO();
        dto.setLectureNum(lectureInquireCommand.getLectureNum());
        dto.setLectureQNum(lectureInquireCommand.getLectureQNum());
        dto.setLectureQType(lectureInquireCommand.getLectureQType());
        dto.setLectureQTitle(lectureInquireCommand.getLectureQTitle());
        dto.setLectureAStatus("답변대기");

        String memNum = (String) session.getAttribute("memNum");
        if (memNum == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        dto.setMemNum(memNum);

        System.out.println("DTO lectureNum: " + dto.getLectureNum()); 

        lectureInquireMapper.lectureInquireInsert(dto);
    }
}

