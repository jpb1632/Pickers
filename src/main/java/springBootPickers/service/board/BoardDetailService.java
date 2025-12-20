package springBootPickers.service.board;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import springBootPickers.domain.BoardDTO;
import springBootPickers.mapper.BoardMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class BoardDetailService {

	private static final Logger log = LoggerFactory.getLogger(BoardDetailService.class);
    @Autowired
    private BoardMapper boardMapper;
    public Map<String, BoardDTO> getPrevAndNextBoard(String boardNum, String boardType) {
        Map<String, Object> params = new HashMap<>();
        params.put("boardNum", boardNum);
        params.put("boardType", boardType); 

        Map<String, BoardDTO> result = new HashMap<>();
        result.put("prevBoard", boardMapper.getPrevBoard(params));
        result.put("nextBoard", boardMapper.getNextBoard(params));

        return result;
    }

    
    public BoardDTO getBoardDetail(String boardNum) {
        BoardDTO board = boardMapper.getBoardByNum(boardNum);

        if (board == null) {
            throw new IllegalArgumentException("게시글을 찾을 수 없습니다: " + boardNum);
        }

        return board;
    }
    
    public void incrementViewCount(String boardNum) {
        try {
            boardMapper.incrementViewCount(boardNum);
        } catch (Exception e) {
            log.error("incrementViewCount() 실행 중 오류 발생", e);
        }
    }
    


    @Transactional
    public int toggleLike(String boardNum, String memNum) {
        int isLiked = boardMapper.isLikedByMember(boardNum, memNum);

        if (isLiked > 0) {
            // 좋아요 취소
            boardMapper.removeLikeByMember(boardNum, memNum);
            boardMapper.decrementLikeCount(boardNum);
        } else {
            // 좋아요 추가
            try {
                boardMapper.addLikeByMember(boardNum, memNum);
                boardMapper.incrementLikeCount(boardNum);
            } catch (Exception e) {
                // PK 중복 에러 무시 (동시성으로 이미 추가된 경우)
                log.debug("좋아요 중복 시도: boardNum={}, memNum={}", boardNum, memNum);
            }
        }

        return boardMapper.getLikeCount(boardNum);
    }
    
    
    public Map<String, Object> getLikeDetails(String boardNum, String memNum) {
        // 좋아요 여부와 좋아요 수를 동시에 반환
        boolean isLiked = memNum != null && boardMapper.isLikedByMember(boardNum, memNum) > 0;
        Integer likeCount = boardMapper.getLikeCount(boardNum);

        // Null 방지
        if (likeCount == null) {
            likeCount = 0;
        }
        
        Map<String, Object> likeDetails = new HashMap<>();
        likeDetails.put("isLiked", isLiked);
        likeDetails.put("likeCount", likeCount);

        return likeDetails;
    }
    
    public void deleteBoard(String boardNum) {
        boardMapper.deleteBoard(boardNum);
    }

    public boolean isWriter(String memNum, String boardNum) {
        String writerMemNum = boardMapper.getWriterMemNum(boardNum);
        return memNum.equals(writerMemNum);
        
    }
    
}