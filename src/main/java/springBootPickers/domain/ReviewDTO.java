package springBootPickers.domain;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("review")
public class ReviewDTO {
	  String lectureNum;
	  String reviewContent;
	  Date reviewRegisterDate;
	  String orderNum;
	  String memNum; 
}
