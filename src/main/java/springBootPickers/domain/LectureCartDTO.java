package springBootPickers.domain;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("cartLecture")
public class LectureCartDTO {
	LectureDTO lectureDTO;
	CartDTO cartDTO;
	Integer totalPrice;
}
