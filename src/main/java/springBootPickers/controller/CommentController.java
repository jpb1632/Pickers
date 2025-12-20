package springBootPickers.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import springBootPickers.domain.AuthInfoDTO;
import springBootPickers.domain.CommentDTO;
import springBootPickers.service.comment.CommentService;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<String> addComment(
            @RequestParam("boardType") String boardType, 
            @RequestBody CommentDTO commentDTO, 
            HttpSession session) {


        AuthInfoDTO authInfo = (AuthInfoDTO) session.getAttribute("auth");
        if (authInfo == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        commentDTO.setMemNum(authInfo.getMemNum());
        commentDTO.setBoardType(boardType);


        commentService.addComment(commentDTO);

        return ResponseEntity.ok("댓글이 성공적으로 등록되었습니다.");
    }


    @GetMapping("/{boardNum}")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable String boardNum) {
        List<CommentDTO> comments = commentService.getCommentsByBoardNum(boardNum);
        return ResponseEntity.ok(comments);
    }


    @PutMapping("/{commentNum}")
    public ResponseEntity<String> editComment(@PathVariable String commentNum, @RequestBody CommentDTO commentDTO, HttpSession session) {
        AuthInfoDTO authInfo = (AuthInfoDTO) session.getAttribute("auth");
        if (authInfo == null || !commentDTO.getMemNum().equals(authInfo.getMemNum())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("본인만 댓글을 수정할 수 있습니다.");
        }

        commentDTO.setCommentNum(commentNum);
        commentService.editComment(commentDTO);
        return ResponseEntity.ok("댓글이 수정되었습니다.");
    }

    @DeleteMapping("/{commentNum}")
    public ResponseEntity<String> removeComment(@PathVariable String commentNum, HttpSession session) {
        AuthInfoDTO authInfo = (AuthInfoDTO) session.getAttribute("auth");

    
        if (authInfo == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

     
        CommentDTO comment = commentService.getCommentByCommentNum(commentNum);
        if (comment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 댓글입니다.");
        }

    
        boolean isAuthorized = comment.getMemNum().equals(authInfo.getMemNum()) || "emp".equals(authInfo.getGrade());
        
        if (!isAuthorized) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("본인 또는 직원만 댓글을 삭제할 수 있습니다.");
        }

     
        commentService.removeComment(commentNum);
        return ResponseEntity.ok("댓글이 성공적으로 삭제되었습니다.");
    }

}