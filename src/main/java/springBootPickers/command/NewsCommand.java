package springBootPickers.command;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class NewsCommand {

	String newsNum;

	@NotEmpty(message = "뉴스 제목을 입력해주세요.")
	String newsTitle;

	@NotEmpty(message = "뉴스 내용을 입력해주세요.")
	String newsContent;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date newsRegisterDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date newsWriterDate;

	@NotEmpty(message = "뉴스 작성자를 입력해주세요.")
	String newsWriterName;
	String empNum;

	MultipartFile newsMainImage;
	MultipartFile newsDetailImage[];

}
