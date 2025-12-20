package springBootPickers.service.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import springBootPickers.domain.StockDTO;
import springBootPickers.mapper.StockMapper;

@Service
public class StockDetailService {
	@Autowired
	StockMapper stockMapper;

	public StockDTO execute(String stockNum, Model model) {
		if (stockNum == null || stockNum.isEmpty()) {
			return null;
		}
		StockDTO dto = stockMapper.stockSelectOne(stockNum);
		model.addAttribute("stockCommand", dto);
		if (dto == null) {
			return null;
		}
		return dto;
	}

}