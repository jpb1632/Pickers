package springBootPickers.domain;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Alias("startEndPageForInquireDTO")
public class StartEndPageForInquireDTO {
	int startRow;
	int endRow;
	String searchWord;
	
	String memNum;
	String grade;
}
