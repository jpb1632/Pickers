package springBootPickers.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import springBootPickers.domain.StartEndPageDTO;
import springBootPickers.domain.StockDTO;

@Mapper
public interface StockMapper {

	public List<StockDTO> stockSelectList(StartEndPageDTO sepDTO);

	public Integer stockCount();

	public void stockInsert(StockDTO dto);

	public StockDTO stockSelectOne(@Param("stockNum") String stockNum);

	public void stockUpdate(StockDTO dto);

	public void stockDelete(String stockNum);

	public void stocksDelete(List<String> stockNumList);
	
	List<StockDTO> stockSelectForChart(
	        @Param("offset") int offset,
	        @Param("limit") int limit,
	        @Param("sortField") String sortField,
	        @Param("sortOrder") String sortOrder
	    );
	
}
