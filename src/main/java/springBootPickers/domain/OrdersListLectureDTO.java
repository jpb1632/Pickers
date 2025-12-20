package springBootPickers.domain;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("ordersListLectures")
public class OrdersListLectureDTO {
	OrdersListDTO ordersListDTO;
	LectureDTO lectureDTO;
	ReviewDTO reviewDTO;
}
