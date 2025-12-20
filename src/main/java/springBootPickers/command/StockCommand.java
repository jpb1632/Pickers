package springBootPickers.command;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StockCommand {
	@NotEmpty(message = "주식종목코드를 입력해주세요.")
	String stockNum;
	String companyNum;
	String empNum;

	@NotNull(message = "현재가는 필수 입력 항목입니다.")
	Long currentPrice;

	@NotNull(message = "시가는 필수 입력 항목입니다.")
	Long openingPrice;

	@NotNull(message = "고가는 필수 입력 항목입니다.")
	Long highPrice;

	@NotNull(message = "저가는 필수 입력 항목입니다.")
	Long lowPrice;

	@NotNull(message = "전일가는 필수 입력 항목입니다.")
	Long previousPrice;

	@NotNull(message = "등락률은 필수 입력 항목입니다.")
	@DecimalMax(value = "99", inclusive = true, message = "등락률을 99%까지 입력할 수 있습니다.")
    @Digits(integer = 2, fraction = 2, message = "등락률은 소수점 이하 2자리까지 입력할 수 있습니다.")
	BigDecimal priceChangeRate;

	@NotNull(message = "거래량은 필수 입력 항목입니다.")
	Long tradingAmount;

	@NotNull(message = "시가총액은 필수 입력 항목입니다.")
	Long marketCap;
}
