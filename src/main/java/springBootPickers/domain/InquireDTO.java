package springBootPickers.domain;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("inquireDTO")
public class InquireDTO {
	String inquireNum;
	String memNum;
	String inquireTitle;
	String inquireContent="";
	Date inquireRegisterDate;
	String inquireStatus;
	String answerNum;
	String answerContent;
	String answerTitle;
	Date answerRegisterDate;
	String empNum;

	String memName;
}
