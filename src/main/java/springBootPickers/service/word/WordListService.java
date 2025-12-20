package springBootPickers.service.word;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import springBootPickers.command.WordCommand;
import springBootPickers.domain.StartEndPageDTO;
import springBootPickers.domain.WordDTO;
import springBootPickers.mapper.WordMapper;
import springBootPickers.service.StartEndPageService;

@Service
public class WordListService {

	@Autowired
	private WordMapper wordMapper;
	@Autowired
	private StartEndPageService startEndPageService;

	public int countWords(String keyword) {
		return wordMapper.countAllWords(keyword);
	}

	public List<WordCommand> getWords(String keyword, int page, int pageSize) {
		int startRow = (page - 1) * pageSize;
		return wordMapper.getWords(keyword, startRow, pageSize);
	}

	public void execute(int page, String searchWord, Model model) {
		int limit = 10;
		StartEndPageDTO sepDTO = startEndPageService.execute(page, limit, searchWord);

		// 검색된 데이터
		List<WordDTO> list = wordMapper.selectWordList(sepDTO);
		int count = wordMapper.countAllWords(searchWord);

		startEndPageService.execute(page, limit, count, searchWord, list, model);
	}

	// 직원 검색 및 페이징
	public void executeForAdmin(int page, String searchWord, Model model) {
		int limit = 10;
		StartEndPageDTO sepDTO = startEndPageService.execute(page, limit, searchWord);

		List<WordDTO> list = wordMapper.selectWordList(sepDTO);
		int count = wordMapper.countAllWords(searchWord);

		startEndPageService.execute(page, limit, count, searchWord, list, model);
	}

	public String getLastWordNum() {
		return wordMapper.getLastWordNum();
	}
	
	public String getEmployeeNumber(String userId) {
        return wordMapper.selectEmployeeNumberByUserId(userId);
    }

}
