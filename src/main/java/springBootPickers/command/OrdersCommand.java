package springBootPickers.command;

import java.util.Date;

import lombok.Data;

@Data
public class OrdersCommand {
	String lectureNums;
	String orderNum;
	Integer totalPaymentPrice;
	Date orderDate;
	Integer orderPrice;
	String payStatus;
	String memNum;
	String orderTitle;
	String orderName;
	String orderPhoneNum;

}
