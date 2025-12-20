package springBootPickers.service.news;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import springBootPickers.domain.NewsDTO;
import springBootPickers.domain.StartEndPageDTO;
import springBootPickers.mapper.NewsMapper;
import springBootPickers.service.StartEndPageService;

@Service
public class NewsListService {
	@Autowired
	NewsMapper newsMapper;
	@Autowired
	StartEndPageService startEndPageService;

	public void execute(Integer page, String searchWord, Model model) {
		Integer limit = 10;

		StartEndPageDTO sepDTO = startEndPageService.execute(page, limit, searchWord);
		List<NewsDTO> list = newsMapper.newsSelectList(sepDTO);

		Integer count = newsMapper.newsCount();
		startEndPageService.execute(page, limit, count, searchWord, list, model);

	}

}
