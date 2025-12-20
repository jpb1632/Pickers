package springBootPickers.domain;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Alias("orders")
@NoArgsConstructor
@AllArgsConstructor
public class OrdersDTO {
	String orderNum;
	Date orderDate;
	Integer orderPrice;
	String payStatus;
	String memNum;
	String orderTitle;
	String orderName;
	String orderPhoneNum;
}
