package springBootPickers.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import springBootPickers.command.BoardCommand;
import springBootPickers.domain.BoardDTO;

@Mapper
public interface BoardMapper {
	void insertBoard(BoardCommand boardCommand);
	String getMemberNumber(String userId);

	int getMaxBoardNum();
	
    List<BoardDTO> getBoardList(
        @Param("boardType") String boardType,
        @Param("startRow") int startRow,
        @Param("pageSize") int pageSize
    );


    
    BoardDTO getPrevBoard(Map<String, Object> params);
    BoardDTO getNextBoard(Map<String, Object> params);

    int getBoardCount(@Param("boardType") String boardType);
    
    

    String getWriterMemNum(@Param("boardNum") String boardNum);

    int deleteBoard(@Param("boardNum") String boardNum);
    



        BoardDTO getBoardByNum(@Param("boardNum") String boardNum);

        void incrementLikeCount(@Param("boardNum") String boardNum);

        void decrementLikeCount(@Param("boardNum") String boardNum);

        Integer getLikeCount(@Param("boardNum") String boardNum);

        int isLikedByMember(@Param("boardNum") String boardNum, @Param("memNum") String memNum);

        void addLikeByMember(@Param("boardNum") String boardNum, @Param("memNum") String memNum);

        void removeLikeByMember(@Param("boardNum") String boardNum, @Param("memNum") String memNum);
  
        void incrementViewCount(@Param("boardNum") String boardNum);
        
        void updateBoard(BoardCommand boardCommand);
        
        
}