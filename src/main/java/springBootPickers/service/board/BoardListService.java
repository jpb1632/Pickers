package springBootPickers.service.board;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springBootPickers.domain.BoardDTO;
import springBootPickers.mapper.BoardMapper;

@Service
public class BoardListService {
    @Autowired
    private BoardMapper boardMapper;

    public Map<String, Object> getBoardList(String boardType, int currentPage, int pageSize) {
        int startRow = (currentPage - 1) * pageSize + 1;

        
        List<BoardDTO> boardList = boardMapper.getBoardList(boardType, startRow, pageSize);

       
        int totalCount = boardMapper.getBoardCount(boardType);
        int totalPage = (int) Math.ceil((double) totalCount / pageSize);

       
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("boardList", boardList);
        resultMap.put("currentPage", currentPage);
        resultMap.put("totalPage", totalPage);

        return resultMap;
    }

    public int getBoardCount(String boardType) {
        return boardMapper.getBoardCount(boardType);
    }
}
