package springBootPickers.service.word;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springBootPickers.mapper.WordMapper;

@Service
public class WordDeleteService {
    @Autowired
    private WordMapper wordMapper;

    public void execute(String wordNum) {
        wordMapper.deleteWord(wordNum);
    }
}
