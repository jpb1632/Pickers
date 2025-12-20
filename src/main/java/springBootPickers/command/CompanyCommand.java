package springBootPickers.command;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CompanyCommand {
	String companyNum;
	@NotEmpty(message = "기업 이름을 입력해주세요.")
	String companyName;
	@NotEmpty(message = "대표 이름을 입력해주세요.")
	String ceoName;

	Integer establishYear;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date listingDate;

	Long issuedShares;

	@NotEmpty(message = "기업 설명을 입력해주세요.")
	String companyDescription;
	String empNum;
}
