package springBootPickers.domain;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("memberDTO")
public class MemberDTO {

	String memNum;
	String memName;
	String memId;
	String memPw;
	String memAddr;
	String memDetailAddr;
	String memPost;
	String memPhoneNum;
	String gender;
	Date memRegisterDate;
	String memEmail;
	Date memBirth;
	
	private Integer stockQuantity = 10; // 환영주식
}
