package springBootPickers.domain;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("auth")
public class AuthInfoDTO {
	String userId;
	String userPw;
	String userName;
	String userEmail;
	String grade; //회원인지 직원인지 구분하기 위해서
	
    String memNum;   
	String empNum;
}
