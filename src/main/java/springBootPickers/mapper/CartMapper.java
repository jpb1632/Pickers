package springBootPickers.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import springBootPickers.domain.CartDTO;
import springBootPickers.domain.LectureCartDTO;
import springBootPickers.domain.LectureDTO;

@Mapper
public interface CartMapper {
	// 장바구니에 상품 추가 (수량 1 고정)
    public void cartInsert(CartDTO dto);

    // 상품 중복 확인
    public int isLectureInCart(@Param("memNum") String memNum, @Param("lectureNum") String lectureNum);

	
	
	public void cartMerge(CartDTO dto);
	
	public LectureDTO lectureSelect(String lectureNum);
	public CartDTO cartSelect(Integer cartNum);

	public List<LectureCartDTO> cartSelectList(@Param("memNum") String memNum
			,@Param("nums") String nums[]);

	public int cartQtyDown(@Param("lectureNum") String lectureNum
			,@Param("memNum") String memNum);
	
	public int lectureNumsDelete(Map<String, Object> condition);
	
	
	
}
