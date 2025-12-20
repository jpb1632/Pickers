package springBootPickers.domain;

import java.util.List;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("purchaseList")
public class PurchaseListDTO {
	OrdersDTO ordersDTO;
	PaymentDTO paymentDTO;
	List<OrdersListLectureDTO> ordersListLectureDTOs;
	
}
