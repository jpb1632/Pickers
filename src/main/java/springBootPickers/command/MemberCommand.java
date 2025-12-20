package springBootPickers.command;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MemberCommand {

	String memNum;
	
	@NotEmpty(message="이름을 입력해주세요")
	String memName;
	
	@NotEmpty(message="아이디를 입력해주세요")
	String memId;
	
	@Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$", message = "영문자와 숫자 그리고 특수문자가 포함된 8글자 이상")
	String memPw;
	
	@NotEmpty(message="비밀번호를 확인해주세요")
	String memPwCon;
	
	@NotEmpty(message="주소를 입력해주세요")
	String memAddr;
	String memDetailAddr;
	String memPost;
	
	@NotEmpty(message="이메일을 입력해주세요")
	String memEmail;
	
	@NotEmpty(message="연락처를 입력해주세요")
	@Size(min = 11, max = 23)
	String memPhoneNum;
	
	String gender;
	

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date memBirth;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date memRegisterDate;
	

	public boolean isMemPwEqualMemPwCon() {
	      return memPw.equals(memPwCon);
	   }
}
