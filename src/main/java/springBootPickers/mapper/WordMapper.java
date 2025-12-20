package springBootPickers.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import springBootPickers.command.WordCommand;
import springBootPickers.domain.StartEndPageDTO;
import springBootPickers.domain.WordDTO;

@Mapper
public interface WordMapper {
	String getLastWordNum();
    void insertWord(WordDTO dto);
    List<WordDTO> selectWordList(StartEndPageDTO sepDTO);
    int countAllWords(); 
    WordDTO selectWordDetail(String wordNum);
    void updateWord(WordDTO dto);
    void deleteWord(String wordNum);
    int countAdminWords(@Param("searchWord") String searchWord);
    

    List<WordCommand> getWords(
        @Param("keyword") String keyword,
        @Param("startRow") int startRow,
        @Param("pageSize") int pageSize
    );

    int countAllWords(@Param("keyword") String keyword);
    String selectEmployeeNumberByUserId(@Param("userId") String userId);
}
