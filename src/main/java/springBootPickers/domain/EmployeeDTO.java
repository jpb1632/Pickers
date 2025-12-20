package springBootPickers.domain;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("employeeDTO")
public class EmployeeDTO {
	String empNum;
	String empName;
	String empId;
	String empPw;
	String empAddr;
	String empDetailAddr;
	String empPost;
	String empPhoneNum;
	String empEmail;
	String gender;
	Date empHireDate;

}
