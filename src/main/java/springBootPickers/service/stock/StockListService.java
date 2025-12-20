package springBootPickers.service.stock;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import springBootPickers.domain.StartEndPageDTO;
import springBootPickers.domain.StockDTO;
import springBootPickers.mapper.StockMapper;
import springBootPickers.service.StartEndPageService;

@Service
public class StockListService {
	@Autowired
	StockMapper stockMapper;
	@Autowired
	StartEndPageService startEndPageService;

	public void execute(Integer page, String searchWord, Model model) {
		Integer limit = 10;

		StartEndPageDTO sepDTO = startEndPageService.execute(page, limit, searchWord);
		List<StockDTO> list = stockMapper.stockSelectList(sepDTO);

		Integer count = stockMapper.stockCount();
		startEndPageService.execute(page, limit, count, searchWord, list, model);

	}

}
