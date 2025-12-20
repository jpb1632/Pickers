package springBootPickers.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import springBootPickers.domain.NewsDTO;
import springBootPickers.domain.StartEndPageDTO;

@Mapper
public interface NewsMapper {

	public void newsInsert(NewsDTO dto);

	public List<NewsDTO> newsSelectList(StartEndPageDTO sepDTO);

	public Integer newsCount();

	public int newsDelete(String newsNum);

	public NewsDTO newsSelectOne(String newsNum);

	public int newsUpdate(NewsDTO dto);

}