package springBootPickers.service.board;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springBootPickers.command.BoardCommand;
import springBootPickers.mapper.BoardMapper;

@Service
public class BoardWriteService {
    @Autowired
    private BoardMapper boardMapper;
    
    public String getMemberNumber(String userId) {
        return boardMapper.getMemberNumber(userId);
    }

    public int getMaxBoardNum() {
        return boardMapper.getMaxBoardNum();
    }

    public void execute(BoardCommand boardCommand) {
    	 validateBoardType(boardCommand.getBoardType());
   
        String boardType = boardCommand.getBoardType();
        if (boardType == null) {
            throw new IllegalArgumentException("boardType이 누락되었습니다.");
        }

        boardType = mapBoardTypeToEnglish(boardType);
        boardCommand.setBoardType(boardType);

     
        String newBoardNum = "board_" + String.format("%06d", boardMapper.getMaxBoardNum() + 1);
        boardCommand.setBoardNum(newBoardNum);

   
        boardMapper.insertBoard(boardCommand);
    }
    private void validateBoardType(String boardType) {
        if (!Arrays.asList("Question", "Free", "Profit", "Tip").contains(boardType)) {
            throw new IllegalArgumentException("유효하지 않은 boardType: " + boardType);
        }
    }
        
    private String mapBoardTypeToEnglish(String boardType) {
        switch (boardType) {
            case "질문":
                return "Question";
            case "자유":
                return "Free";
            case "수익인증":
                return "Profit";
            case "꿀팁공유":
                return "Tip";
           
            case "Question":
            case "Free":
            case "Profit":
            case "Tip":
                return boardType; 
            default:
                throw new IllegalArgumentException("알 수 없는 boardType: " + boardType);
        }
    }

    public String generateBoardNum() {
        int maxBoardNum = getMaxBoardNum();
        return "board_" + String.format("%06d", maxBoardNum + 1);
    }
}
