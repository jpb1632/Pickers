package springBootPickers.service.news;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springBootPickers.mapper.AutoNumMapper;

@Service
public class NewssDeleteService {
@Autowired
AutoNumMapper autoNumMapper;
public void execute(String[] newsNums) {
	autoNumMapper.numsDelete(newsNums, "news", "news_num");
}
}
