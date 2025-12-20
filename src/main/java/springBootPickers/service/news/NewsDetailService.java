package springBootPickers.service.news;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import springBootPickers.domain.NewsDTO;
import springBootPickers.mapper.NewsMapper;

@Service
public class NewsDetailService {
	@Autowired
	NewsMapper newsMapper;
	public void execute(String newsNum, Model model) {
		NewsDTO dto = newsMapper.newsSelectOne(newsNum);
		model.addAttribute("newsCommand", dto);
	}
}
