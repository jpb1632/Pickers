package springBootPickers.domain;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Alias("newsDTO")
@AllArgsConstructor
@NoArgsConstructor
public class NewsDTO {
	String newsNum;
	String newsTitle;
	String newsContent;
	Date newsRegisterDate;
	Date newsWriterDate;
	String newsWriterName;
	String empNum;

	String newsMainImage;
	String newsDetailImage;

	String newsMainStoreImage;
	String newsDetailStoreImage;

}
