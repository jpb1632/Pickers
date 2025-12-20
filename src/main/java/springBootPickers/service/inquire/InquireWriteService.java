package springBootPickers.service.inquire;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpSession;
import springBootPickers.command.InquireCommand;
import springBootPickers.domain.InquireDTO;
import springBootPickers.mapper.InquireMapper;

@Service
public class InquireWriteService {

    @Autowired
    InquireMapper inquireMapper;

    public void execute(InquireCommand inquireCommand, HttpSession session) {
        InquireDTO dto = new InquireDTO();
        dto.setInquireNum(inquireCommand.getInquireNum());
        dto.setInquireTitle(inquireCommand.getInquireTitle());
        dto.setInquireContent(inquireCommand.getInquireContent());

        String memNum = (String) session.getAttribute("memNum");

        if (memNum == null) {
            throw new IllegalStateException("로그인 상태에서만 문의를 작성할 수 있습니다.");
        }

        dto.setMemNum(memNum);
        dto.setInquireStatus("답변대기");

        inquireMapper.inquireInsert(dto);
    }
}
