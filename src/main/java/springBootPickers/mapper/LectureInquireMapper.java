package springBootPickers.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import springBootPickers.domain.LectureInquireDTO;
import springBootPickers.domain.StartEndPageForInquireDTO;

@Mapper
public interface LectureInquireMapper {

	public void lectureInquireInsert(LectureInquireDTO dto);

	public List<LectureInquireDTO> lectureInquireSelectList(StartEndPageForInquireDTO sepfiDTO);

	public Integer lectureInquireCount(String memNum, String grade);

	public List<LectureInquireDTO> lectureInquireSelectListForMember(StartEndPageForInquireDTO sepfiDTO);

	public LectureInquireDTO lectureInquireSelectOne(String lectureQNum);

	public void lectureInquireAnswer(LectureInquireDTO dto);

	public void updateLectureInquireStatusToAnswered(String lectureQNum);

	public void lectureInquireDelete(String lectureQNum);

	public String getMemberNameByMemNum(String memNum);

	///

	public List<LectureInquireDTO> selectListByLectureNum(@Param("lectureNum") String lectureNum,
			@Param("offset") int offset, @Param("limit") int limit);

	public Integer lectureInquireCountLecture(@Param("lectureNum") String lectureNum, @Param("role") String role);

	public List<LectureInquireDTO> selectListByLectureNum(StartEndPageForInquireDTO sepfiDTO);

}
