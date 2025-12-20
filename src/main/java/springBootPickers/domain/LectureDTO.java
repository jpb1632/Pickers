package springBootPickers.domain;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Alias("lecture")
@AllArgsConstructor
@NoArgsConstructor
public class LectureDTO {
	String lectureNum;
	String lectureTeacherName;
	String lectureTitle;
	String lectureTopic;
	Integer lecturePrice;
	String lectureDescription;
	String lectureStatus;
	Date lectureRegisterDate;
	String empNum;
	String lectureMainImage;
	String lectureDetailImage;
	String lectureMainStoreImage;
	String lectureDetailStoreImage;
}
