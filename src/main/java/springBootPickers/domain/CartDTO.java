package springBootPickers.domain;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("cart")
public class CartDTO {
	String memNum;
	String lectureNum;
	Date cartDate;
	Integer cartQty;
	String cartNum;
}
