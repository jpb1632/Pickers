package springBootPickers.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import springBootPickers.domain.CommentDTO;

@Mapper
public interface CommentMapper {
	int deleteCommentsByBoardNum(String boardNum); 

    void insertComment(CommentDTO commentDTO);

    List<CommentDTO> selectCommentsByBoardNum(String boardNum);


    CommentDTO selectCommentByCommentNum(String commentNum);


    void updateComment(CommentDTO commentDTO);


    void deleteComment(String commentNum);
}

