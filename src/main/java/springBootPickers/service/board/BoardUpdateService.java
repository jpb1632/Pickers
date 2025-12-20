package springBootPickers.service.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springBootPickers.command.BoardCommand;
import springBootPickers.mapper.BoardMapper;

@Service
public class BoardUpdateService {

    @Autowired
    private BoardMapper boardMapper;

    public void updateBoard(BoardCommand boardCommand) {
        boardMapper.updateBoard(boardCommand);
    }
}
