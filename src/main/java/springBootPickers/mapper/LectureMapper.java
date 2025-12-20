package springBootPickers.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import springBootPickers.domain.LectureDTO;
import springBootPickers.domain.StartEndPageDTO;

@Mapper
public interface LectureMapper {
	public int lectureInsert(LectureDTO dto);
	public List<LectureDTO> allSelect(StartEndPageDTO sepDTO);
	public int lectureCount(String searchWord);
	public int productDelete(@Param("lectures") String lectures[]);
	public LectureDTO lectureSelectOne(String lectureNum);
	public int lectureUpdate(LectureDTO dto);
	public int lectureDelete(String lectureNum);
	public List<LectureDTO> paidLecturesWithPaging(Map<String, Object> paramMap);
	public int paidLectureCount(@Param("memNum") String memNum, @Param("searchWord") String searchWord);
    public String checkLecturePurchased(Map<String, String> paramMap);
	



}
