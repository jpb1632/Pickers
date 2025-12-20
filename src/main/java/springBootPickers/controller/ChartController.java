package springBootPickers.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import springBootPickers.domain.StockDTO;
import springBootPickers.service.chart.ChartService;

@Controller
@RequestMapping("chart")
public class ChartController {

    @Autowired
    private ChartService chartService;

    @GetMapping("list")
    public String chartList(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "currentPrice") String sortField,
        @RequestParam(defaultValue = "desc") String sortOrder,
        Model model
    ) {
        int pageSize = 10;
        Map<String, Object> chartData = chartService.getChartList(page, pageSize, sortField, sortOrder);

        // totalCount를 Integer로 변환 후 처리
        int totalCount = (int) chartData.get("totalCount");
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
        int startPage = Math.max(1, page - 2);
        int endPage = Math.min(startPage + 4, totalPages);

        model.addAttribute("chartData", chartData);
        model.addAttribute("currentPage", page);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "thymeleaf/chart/chartList";
    }




    @GetMapping("detail")
    public String chartDetail(@RequestParam("stockNum") String stockNum, Model model) {
        StockDTO stock = chartService.getChartDetail(stockNum);
        model.addAttribute("stock", stock);
        return "thymeleaf/chart/chartDetail"; // HTML 파일 경로
    }
}
