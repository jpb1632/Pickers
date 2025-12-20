package springBootPickers.command;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class InquireCommand {

	String inquireNum;
	String memNum;
	@NotNull(message = "문의 제목을 입력해주세요.")
	String inquireTitle;
	@NotNull(message = "문의 내용을 입력해주세요.")
	String inquireContent;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date inquireRegisterDate;
	
	String inquireStatus;
	String answerNum;
	@NotNull(message = "문의 답변을 입력해주세요.")
	String answerContent;
	String answerTitle;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date answerRegisterDate;
	String empNum;
}
