package springBootPickers.service.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import springBootPickers.mapper.BoardMapper;
import springBootPickers.mapper.CommentMapper;

@Service
public class BoardDeleteService {

	private static final Logger log = LoggerFactory.getLogger(BoardDeleteService.class);

    @Autowired
    private BoardMapper boardMapper;
    
    @Autowired
    private CommentMapper commentMapper; 

    
    @Autowired
    public BoardDeleteService(BoardMapper boardMapper, CommentMapper commentMapper) {
        this.boardMapper = boardMapper;
        this.commentMapper = commentMapper;
    }

    public void deleteBoard(String boardNum) {
        log.debug("DEBUG: deleteBoard 호출 - boardNum: {}", boardNum);


        int deletedComments = commentMapper.deleteCommentsByBoardNum(boardNum); 
        log.debug("삭제된 댓글 수: {}", deletedComments);

   
        int rowsAffected = boardMapper.deleteBoard(boardNum); 
        if (rowsAffected == 0) {
            throw new RuntimeException("삭제 실패: 게시글이 존재하지 않습니다.");
        }
        log.info("삭제 성공 - 삭제된 게시글 행 수: {}", rowsAffected);
    }


    public boolean isWriter(String memNum, String boardNum) {
        try {
            log.debug("DEBUG: isWriter 호출 - memNum: {}, boardNum: {}", memNum, boardNum);
            String writerMemNum = boardMapper.getWriterMemNum(boardNum);
            log.debug("DEBUG: 작성자 확인 - writerMemNum: {}", writerMemNum);
            return memNum.equals(writerMemNum);
        } catch (Exception e) {
            log.error("isWriter 실행 중 오류 발생", e);
            return false;
        }
    }
}