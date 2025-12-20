package springBootPickers.domain;


import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("ordersList")
public class OrdersListDTO {
	String lectureNum;
	String  orderNum;
	Integer orderQuantity;
	Integer lectureTotalPrice;	
}
