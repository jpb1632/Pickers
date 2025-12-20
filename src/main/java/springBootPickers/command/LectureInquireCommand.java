package springBootPickers.command;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LectureInquireCommand {
	String lectureQNum;
	String lectureNum;
	@NotBlank(message = "종류를 선택해주세요.")
	String lectureQType;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date lectureQRegisterDate;
	String memNum;
	String lectureAStatus;

	String lectureANum;
	@NotEmpty(message = "내용을 입력해주세요.")
	String lectureAContent;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date lectureARegisterDate;
	String empNum;
	@NotBlank(message = "내용을 입력해주세요.")
	String lectureQTitle;
}
