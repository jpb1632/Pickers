package springBootPickers.command;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class LectureCommand {
	String lectureNum;
	@NotEmpty(message = "강사 이름을 입력해주세요.")
	String lectureTeacherName;
	@NotEmpty(message = "강의 제목을 입력해주세요.")
	String lectureTitle;
	String lectureTopic;
	@NotNull(message = "강의 가격을 입력해주세요.")
	Integer lecturePrice;
	String lectureDescription;
	String lectureStatus;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date lectureRegisterDate;
	String empNum;
	MultipartFile lectureMainImage;
	MultipartFile lectureDetailImage[];


}
