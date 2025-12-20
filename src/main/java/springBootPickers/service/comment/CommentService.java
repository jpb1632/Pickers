package springBootPickers.service.comment;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springBootPickers.domain.CommentDTO;
import springBootPickers.mapper.BoardMapper;
import springBootPickers.mapper.CommentMapper;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private BoardMapper boardMapper; 


    public void addComment(CommentDTO commentDTO) {
  
        String boardNum = commentDTO.getBoardNum();
        if (boardMapper.getBoardByNum(boardNum) == null) {
            throw new IllegalArgumentException("존재하지 않는 게시글입니다: " + boardNum);
        }

     
        String commentNum = UUID.randomUUID().toString().replace("-", "").substring(0, 30);
        commentDTO.setCommentNum(commentNum);

  
        commentMapper.insertComment(commentDTO);
    }
 
    public List<CommentDTO> getCommentsByBoardNum(String boardNum) {
        return commentMapper.selectCommentsByBoardNum(boardNum);
    }


    public CommentDTO getCommentByCommentNum(String commentNum) {
        return commentMapper.selectCommentByCommentNum(commentNum);
    }

  
    public void editComment(CommentDTO commentDTO) {
        commentMapper.updateComment(commentDTO);
    }

  
    public void removeComment(String commentNum) {
        commentMapper.deleteComment(commentNum);
    }
}