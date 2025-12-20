package springBootPickers.service.orders;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.inicis.std.util.SignatureUtil;

import jakarta.servlet.http.HttpServletRequest;
import springBootPickers.domain.OrdersDTO;
import springBootPickers.repository.OrderRepository;

@Service
public class IniPayReqService {
	@Autowired
	OrderRepository orderRepository;
	@Autowired
	private HttpServletRequest request; 
	
    @Value("${inicis.mid}")
    private String mid;
    
    @Value("${inicis.signkey}")
    private String signKey;

	public void execute(String orderNum, Model model) throws Exception {
	    OrdersDTO dto = orderRepository.orderSelectOne(orderNum);

	    String mKey = SignatureUtil.hash(signKey, "SHA-256");
	    String timestamp = SignatureUtil.getTimestamp();
	    String orderNumber = orderNum;
	    String price = String.valueOf(dto.getOrderPrice());

	    Map<String, String> signParam = new HashMap<>();
	    signParam.put("oid", orderNumber);
	    signParam.put("price", price);
	    signParam.put("timestamp", timestamp);

	    String signature = SignatureUtil.makeSignature(signParam);

	    String scheme = request.getScheme();  
	    String serverName = request.getServerName();  
	    int serverPort = request.getServerPort();  
	    
	    String baseUrl;
	    if ((scheme.equals("http") && serverPort == 80) || 
	        (scheme.equals("https") && serverPort == 443)) {
	        baseUrl = scheme + "://" + serverName;
	    } else {
	        baseUrl = scheme + "://" + serverName + ":" + serverPort;
	    }

	    model.addAttribute("mid", mid);
	    model.addAttribute("orderNumber", orderNumber);
	    model.addAttribute("price", price);
	    model.addAttribute("timestamp", timestamp);
	    model.addAttribute("signature", signature);
	    model.addAttribute("mKey", mKey);
	    model.addAttribute("orderName", dto.getOrderName());
	    model.addAttribute("orderPhoneNum", dto.getOrderPhoneNum());
	    model.addAttribute("orderTitle", dto.getOrderTitle());
	    model.addAttribute("baseUrl", baseUrl);  
}
}