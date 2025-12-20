package springBootPickers.service.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import springBootPickers.command.StockCommand;
import springBootPickers.domain.AuthInfoDTO;
import springBootPickers.domain.StockDTO;
import springBootPickers.mapper.EmployeeMapper;
import springBootPickers.mapper.StockMapper;

@Service
public class StockWriteService {
	@Autowired
	StockMapper stockMapper;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	EmployeeMapper employeeMapper;

	public void execute(StockCommand stockCommand, HttpSession session) {
		StockDTO dto = new StockDTO();
		dto.setCompanyNum(stockCommand.getCompanyNum());
		dto.setCurrentPrice(stockCommand.getCurrentPrice());
		dto.setEmpNum(stockCommand.getEmpNum());
		dto.setHighPrice(stockCommand.getHighPrice());
		dto.setLowPrice(stockCommand.getLowPrice());
		dto.setMarketCap(stockCommand.getMarketCap());
		dto.setOpeningPrice(stockCommand.getOpeningPrice());
		dto.setPreviousPrice(stockCommand.getPreviousPrice());
		dto.setPriceChangeRate(stockCommand.getPriceChangeRate());
		dto.setStockNum(stockCommand.getStockNum());
		dto.setTradingAmount(stockCommand.getTradingAmount());

		AuthInfoDTO auth = (AuthInfoDTO) session.getAttribute("auth");
		String empNum = employeeMapper.getEmpNum(auth.getUserId());
		dto.setEmpNum(empNum);

		stockMapper.stockInsert(dto);
	}

}
