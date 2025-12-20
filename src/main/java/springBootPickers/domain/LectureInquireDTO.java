package springBootPickers.domain;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("lectureInquireDTO")
public class LectureInquireDTO {
	String lectureQNum;
	String lectureNum;
	String lectureQType;
	Date lectureQRegisterDate;
	String memNum;
	String lectureAStatus;
	String lectureANum;
	String lectureAContent;
	Date lectureARegisterDate;
	String empNum;
	String lectureQTitle;
	
	String memName;
	//String memId;
	//String lectureName;
}
