package springBootPickers.service.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springBootPickers.mapper.StockMapper;

@Service
public class StockDeleteService {
	@Autowired
	StockMapper stockMapper;

	public void execute(String stockNum) {
		stockMapper.stockDelete(stockNum);

	}

}
