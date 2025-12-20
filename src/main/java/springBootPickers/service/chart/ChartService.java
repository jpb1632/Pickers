package springBootPickers.service.chart;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springBootPickers.domain.StockDTO;
import springBootPickers.mapper.StockMapper;

@Service
public class ChartService {

    @Autowired
    private StockMapper stockMapper;

    public Map<String, Object> getChartList(int page, int pageSize, String sortField, String sortOrder) {
        int offset = (page - 1) * pageSize;
        List<StockDTO> stocks = stockMapper.stockSelectForChart(offset, pageSize, sortField, sortOrder);
        int totalCount = stockMapper.stockCount();

        Map<String, Object> chartData = new HashMap<>();
        chartData.put("stocks", stocks != null ? stocks : Collections.emptyList());
        chartData.put("totalCount", totalCount);
        chartData.put("pageSize", pageSize);
        return chartData;
    }

    public StockDTO getChartDetail(String stockNum) {
        return stockMapper.stockSelectOne(stockNum);
    }
}
