package springBootPickers.service.inquire;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springBootPickers.mapper.InquireMapper;

@Service
public class InquireDeleteService {
	@Autowired
	InquireMapper inquireMapper;

	public void execute(String inquireNum) {
		inquireMapper.inquireDelete(inquireNum);

	}

}
