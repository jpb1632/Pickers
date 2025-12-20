package springBootPickers.domain;

import java.math.BigDecimal;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("stockDTO")
public class StockDTO {
	String stockNum;
	String companyNum;
	String empNum;

	Long currentPrice;
	Long openingPrice;
	Long highPrice;
	Long lowPrice;
	Long previousPrice;

	BigDecimal priceChangeRate;

	Long tradingAmount;
	Long marketCap;

	String companyName;
	String empName;

	public String getEmpName() {
		return empName != null ? empName : "미등록 직원"; // 기본값 설정
	}

}
