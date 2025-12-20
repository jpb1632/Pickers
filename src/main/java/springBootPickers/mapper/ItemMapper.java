package springBootPickers.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import springBootPickers.domain.LectureDTO;

@Mapper
public interface ItemMapper {
	public int wishItem(Map<String, String> map);
	public List<LectureDTO> wishSelectList(String memNum);
	public Integer wishCountSelectOne(Map<String, String> map);
	public Integer wishDelete(Map<String, Object> condition);
	
}
