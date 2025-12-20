package springBootPickers.service.stock;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springBootPickers.mapper.StockMapper;

@Service
public class StocksDeleteService {
	@Autowired
	StockMapper stockMapper;

	public void execute(String[] stockNums) {
		List<String> stockNumList = Arrays.asList(stockNums);
		stockMapper.stocksDelete(stockNumList);

	}

}
