package springBootPickers.service.word;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;

import springBootPickers.domain.WordDTO;
import springBootPickers.mapper.WordMapper;

@Service
public class WordDetailService {

	private static final Logger log = LoggerFactory.getLogger(WordDetailService.class);
    @Autowired
    private WordMapper wordMapper;

    public void execute(String wordNum, Model model) {
        WordDTO dto = wordMapper.selectWordDetail(wordNum);
        log.debug("DEBUG: WordDTO -> {}", dto);
        log.debug("DEBUG: Model Attribute Test -> {}", model.containsAttribute("wordCommand"));
        model.addAttribute("wordCommand", dto);
    }

    public WordDTO getWordDetail(String wordNum) {
        return wordMapper.selectWordDetail(wordNum);
    }
}