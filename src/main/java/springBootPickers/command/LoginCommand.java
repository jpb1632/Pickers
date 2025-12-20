package springBootPickers.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginCommand {

	@NotBlank(message = "아이디를 입력해주세요.")
	String userId;

	@NotBlank(message = "비밀번호를 입력해주세요.")
	@Size(min = 8, max = 20)
	String userPw;

	boolean idStore;
	boolean autoLogin;
	
	
}
