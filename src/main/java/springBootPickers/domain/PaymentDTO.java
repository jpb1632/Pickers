package springBootPickers.domain;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("payment")
public class PaymentDTO {
	String payNum;
    String orderNum;
    String confirmNum;
    String cardNum;
    String tid;
    String totalPrice;
    String resultMessage;
    String payMethod;
    String applDate;
    String applTime;
}
