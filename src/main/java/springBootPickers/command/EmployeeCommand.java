package springBootPickers.command;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmployeeCommand {
	String empNum;
	@NotEmpty(message = "이름을 입력해주세요.")
	String empName;
	@NotEmpty(message = "아이디를 입력해주세요.")
	String empId;
	@Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$",
			message = "영문자와 숫자 그리고 특수문자가 포함된 8글자 이상")
	String empPw;
	@NotBlank(message = "비밀번호 확인을 입력해주세요.")
	String empPwCon;
	@NotBlank(message = "주소를 입력해주세요.")
	String empAddr;
	String empDetailAddr;
	String empPost;
	@NotBlank(message = "전화번호를 입력해주세요.")
	@Size(min = 11, max = 13)
	String empPhoneNum;
	@NotEmpty(message = "이메일을 입력해주세요.")
	String empEmail;
	@NotNull(message = "성별을 선택해주세요.")
	String gender;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "입사일을 선택해주세요.")
    Date empHireDate;
	public boolean isEmpPwEqualEmpPwCon() {
		return empPw.equals(empPwCon);
	}
	
}
