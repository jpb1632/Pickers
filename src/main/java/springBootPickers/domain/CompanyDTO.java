package springBootPickers.domain;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("companyDTO")
public class CompanyDTO {
	String companyNum;
	String companyName;
	String ceoName;
	Integer establishYear;
	Date listingDate;
	Long issuedShares;
	String companyDescription;
	String empNum;

	String empName;
	
	public String getEmpName() {
        return empName != null ? empName : "미등록 직원"; // 기본값 설정
    }
}
