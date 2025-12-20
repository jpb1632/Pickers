package springBootPickers.service.word;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springBootPickers.command.WordCommand;
import springBootPickers.domain.WordDTO;
import springBootPickers.mapper.WordMapper;

@Service
public class WordUpdateService {
    @Autowired
    private WordMapper wordMapper;

    public void execute(WordCommand wordCommand) {
        WordDTO wordDTO = new WordDTO();
        wordDTO.setWordNum(wordCommand.getWordNum());
        wordDTO.setWordName(wordCommand.getWordName());
        wordDTO.setWordDescription(wordCommand.getWordDescription());
        wordDTO.setWordUpdateDate(new Date()); 
        wordDTO.setEmpNum(wordCommand.getEmpNum());
        wordMapper.updateWord(wordDTO);
    }
}

