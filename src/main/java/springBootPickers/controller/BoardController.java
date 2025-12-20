package springBootPickers.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import springBootPickers.command.BoardCommand;
import springBootPickers.domain.AuthInfoDTO;
import springBootPickers.domain.BoardDTO;
import springBootPickers.domain.CommentDTO;
import springBootPickers.service.board.BoardDeleteService;
import springBootPickers.service.board.BoardDetailService;
import springBootPickers.service.board.BoardListService;
import springBootPickers.service.board.BoardUpdateService;
import springBootPickers.service.board.BoardWriteService;
import springBootPickers.service.comment.CommentService;

@Controller
@RequestMapping("/board")
public class BoardController {

	private static final Logger log = LoggerFactory.getLogger(BoardController.class);
    @Autowired
    private BoardListService boardListService;
    @Autowired
    private BoardWriteService boardWriteService;
    @Autowired
    private BoardDetailService boardDetailService;
    @Autowired
    private BoardUpdateService boardUpdateService;
    @Autowired
    private BoardDeleteService boardDeleteService;
    @Autowired
    private CommentService commentService;
    
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
            default:
                throw new IllegalArgumentException("알 수 없는 boardType: " + boardType);
        }
    }


    private String mapBoardTypeToKorean(String boardType) {
        switch (boardType) {
            case "Question":
                return "질문";
            case "Free":
                return "자유";
            case "Profit":
                return "수익인증";
            case "Tip":
                return "꿀팁공유";
            default:
                throw new IllegalArgumentException("알 수 없는 boardType: " + boardType);
        }
    }

    @GetMapping("/boardUpdate/{boardNum}")
    public String boardUpdateForm(
            @PathVariable("boardNum") String boardNum,
            @RequestParam("boardType") String boardType,
            HttpSession session,
            Model model
    ) {
        AuthInfoDTO authInfo = (AuthInfoDTO) session.getAttribute("auth");
        if (authInfo == null) {
            return "redirect:/login/login";
        }

        BoardDTO board = boardDetailService.getBoardDetail(boardNum);

        if (!board.getMemNum().equals(authInfo.getMemNum())) {
            return "redirect:/board/boardQueList";
        }

        model.addAttribute("board", board);
        model.addAttribute("boardType", boardType);
        return "thymeleaf/board/boardUpdate";
    }



    @PostMapping("/boardUpdate")
    public String boardUpdateSubmit(@ModelAttribute("boardCommand") BoardCommand boardCommand, HttpSession session) {
        AuthInfoDTO authInfo = (AuthInfoDTO) session.getAttribute("auth");
        if (authInfo == null || !authInfo.getMemNum().equals(boardCommand.getMemNum())) {
            return "redirect:/login/login"; 
        }

        boardUpdateService.updateBoard(boardCommand);


        return "redirect:/board/boardDetail/" + boardCommand.getBoardNum() + "?boardType=" + boardCommand.getBoardType();
    }
    @GetMapping("/boardDetail/{boardNum}")
    public String boardDetail(
            @PathVariable("boardNum") String boardNum,
            @RequestParam(value = "boardType", required = true) String boardType,
            HttpSession session,
            Model model
    ) {
        // 게시판 타입 한글 매핑
        String koreanBoardType = mapBoardTypeToKorean(boardType);

        // 게시글 상세 정보 가져오기
        BoardDTO board = boardDetailService.getBoardDetail(boardNum);
        boardDetailService.incrementViewCount(boardNum);

        // 댓글 목록 가져오기
        List<CommentDTO> comments = commentService.getCommentsByBoardNum(boardNum);

        // 이전 글과 다음 글 가져오기
        Map<String, BoardDTO> prevAndNext = boardDetailService.getPrevAndNextBoard(boardNum, boardType);

        // 로그인 정보 가져오기
        AuthInfoDTO authInfo = (AuthInfoDTO) session.getAttribute("auth");
        boolean isAuthor = authInfo != null && authInfo.getMemNum().equals(board.getMemNum());
        boolean isEmployee = authInfo != null && "emp".equals(authInfo.getGrade());
        
        // 좋아요 상태와 개수 가져오기
        String memNum = (authInfo != null) ? authInfo.getMemNum() : null;
        Map<String, Object> likeDetails = boardDetailService.getLikeDetails(boardNum, memNum);
        boolean isLiked = (boolean) likeDetails.get("isLiked");
        int likeCount = (int) likeDetails.get("likeCount");

        // 리스트 URL 설정
        String listUrl;
        switch (boardType) {
            case "Free":
                listUrl = "/board/boardFreeList";
                break;
            case "Profit":
                listUrl = "/board/boardProfitList";
                break;
            case "Tip":
                listUrl = "/board/boardTipList";
                break;
            default:
                listUrl = "/board/boardQueList";
        }

        // 모델에 데이터 추가
        model.addAttribute("listUrl", listUrl);
        model.addAttribute("board", board);
        model.addAttribute("comments", comments);
        model.addAttribute("prevBoard", prevAndNext.get("prevBoard"));
        model.addAttribute("nextBoard", prevAndNext.get("nextBoard"));
        model.addAttribute("auth", authInfo);
        model.addAttribute("isAuthor", isAuthor);
        model.addAttribute("isEmployee", isEmployee);
        model.addAttribute("boardType", boardType);
        model.addAttribute("isLiked", isLiked); // 좋아요 상태 전달
        model.addAttribute("likeCount", likeCount); // 좋아요 개수 전달

        return "thymeleaf/board/boardDetail";
    }


    @PostMapping("/boardDetail/like/{boardNum}")
    @ResponseBody
    public Map<String, Object> toggleLike(@PathVariable("boardNum") String boardNum, HttpSession session) {
        log.debug("toggleLike() 호출됨 - boardNum: {}", boardNum);

        // 세션에서 사용자 정보 확인
        AuthInfoDTO authInfo = (AuthInfoDTO) session.getAttribute("auth");
        if (authInfo == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        // 좋아요 토글 처리
        boardDetailService.toggleLike(boardNum, authInfo.getMemNum());

        // 좋아요 상태와 좋아요 수를 반환
        Map<String, Object> likeDetails = boardDetailService.getLikeDetails(boardNum, authInfo.getMemNum());
    
        log.debug("좋아요 상태 - {}", likeDetails);

        if (likeDetails == null || !likeDetails.containsKey("isLiked") || !likeDetails.containsKey("likeCount")) {
            log.debug("likeDetails가 null이거나 예상치 못한 값");
            likeDetails = new HashMap<>();
            likeDetails.put("isLiked", false);
            likeDetails.put("likeCount", 0);
        }

        log.debug("반환할 likeDetails - {}", likeDetails);
        
    
        return likeDetails;
}




    @PostMapping("/boardDetail/delete/{boardNum}")
    public String deleteBoard(
            @PathVariable("boardNum") String boardNum,
            @RequestParam("boardType") String boardType,
            HttpSession session) {

        log.debug("삭제 요청 - boardNum: {}, boardType: {}", boardNum, boardType);

        if (boardType == null || boardType.trim().isEmpty()) {
            log.error("boardType이 전달되지 않음");
            return "redirect:/error"; // 에러 페이지로 리다이렉트
        }

        AuthInfoDTO authInfo = (AuthInfoDTO) session.getAttribute("auth");

        if (authInfo != null) {
            if ("emp".equals(authInfo.getGrade()) || boardDeleteService.isWriter(authInfo.getMemNum(), boardNum)) {
                boardDeleteService.deleteBoard(boardNum);
                log.info("삭제 성공");
            } else {
                log.warn("삭제 권한 없음");
                return "redirect:/error/noPermission"; 
            }
        } else {
            log.warn("사용자 인증 실패");
            return "redirect:/login/login"; 
        }

 
        String redirectUrl = "redirect:/board/";
        switch (boardType) {
            case "Free":
                redirectUrl += "boardFreeList";
                break;
            case "Profit":
                redirectUrl += "boardProfitList";
                break;
            case "Tip":
                redirectUrl += "boardTipList";
                break;
            case "Question":
            default:
                redirectUrl += "boardQueList";
                break;
        }

        log.debug("리다이렉트 URL - {}", redirectUrl);
        return redirectUrl;
    }


    private String loadBoardList(String boardType, int currentPage, HttpSession session, Model model) {
        if (boardType == null || boardType.isEmpty()) {
            throw new IllegalArgumentException("boardType이 누락되었습니다.");
        }


        int pageSize = 10; 
        int blockSize = 5; 
        int totalBoardCount = boardListService.getBoardCount(boardType); 
        int totalPages = (int) Math.ceil((double) totalBoardCount / pageSize); 


        currentPage = Math.max(1, Math.min(currentPage, totalPages));

  
        int startPage = ((currentPage - 1) / blockSize) * blockSize + 1;
        int endPage = Math.min(startPage + blockSize - 1, totalPages);

  
        Map<String, Object> boardData = boardListService.getBoardList(boardType, currentPage, pageSize);


        AuthInfoDTO authInfo = (AuthInfoDTO) session.getAttribute("auth");
        boolean isLoggedIn = authInfo != null;


        model.addAttribute("boardList", boardData.get("boardList"));
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("isLoggedIn", isLoggedIn);
        model.addAttribute("auth", authInfo);
        model.addAttribute("boardType", boardType);


        switch (boardType) {
            case "Free":
                return "thymeleaf/board/boardFreeList";
            case "Profit":
                return "thymeleaf/board/boardProfitList";
            case "Tip":
                return "thymeleaf/board/boardTipList";
            case "Question":
                return "thymeleaf/board/boardQueList";
            default:
                throw new IllegalArgumentException("잘못된 boardType: " + boardType);
        }
    }


    @GetMapping("/boardQueList")
    public String getBoardQueList(
            @RequestParam(value = "page", defaultValue = "1") int currentPage,
            HttpSession session,
            Model model
    ) {
        return loadBoardList("Question", currentPage, session, model);
    }


    @GetMapping("/boardFreeList")
    public String getBoardFreeList(
            @RequestParam(value = "page", defaultValue = "1") int currentPage,
            HttpSession session,
            Model model
    ) {
        return loadBoardList("Free", currentPage, session, model);
    }


    @GetMapping("/boardProfitList")
    public String getBoardProfitList(
            @RequestParam(value = "page", defaultValue = "1") int currentPage,
            HttpSession session,
            Model model
    ) {
        return loadBoardList("Profit", currentPage, session, model);
    }


    @GetMapping("/boardTipList")
    public String getBoardTipList(
            @RequestParam(value = "page", defaultValue = "1") int currentPage,
            HttpSession session,
            Model model
    ) {
        return loadBoardList("Tip", currentPage, session, model);
    }

    
    @GetMapping("/boardWrite")
    public String boardWriteForm(
            @RequestParam(value = "boardType", required = true) String boardType,
            HttpSession session,
            Model model) {
        AuthInfoDTO authInfo = (AuthInfoDTO) session.getAttribute("auth");
        if (authInfo != null) {
            BoardCommand boardCommand = new BoardCommand();

           
            String memNum = boardWriteService.getMemberNumber(authInfo.getUserId());
            boardCommand.setMemNum(memNum);

        
            String newBoardNum = "board_" + String.format("%06d", boardWriteService.getMaxBoardNum() + 1);
            boardCommand.setBoardNum(newBoardNum);

        
            boardCommand.setBoardRegisterDate(new Date());
            boardCommand.setBoardType(boardType);

            model.addAttribute("boardCommand", boardCommand);
        }
        model.addAttribute("boardType", boardType);
        return "thymeleaf/board/boardWrite";
    }

    @PostMapping("/boardWrite")
    public String boardWriteSubmit(@ModelAttribute("boardCommand") BoardCommand boardCommand, HttpSession session) {
        AuthInfoDTO authInfo = (AuthInfoDTO) session.getAttribute("auth");

        if (authInfo == null) {
            return "redirect:/login/login";
        }


        boardCommand.setMemNum(authInfo.getMemNum());

 
        if (boardCommand.getBoardType() == null || boardCommand.getBoardType().isEmpty()) {
            throw new IllegalArgumentException("boardType이 누락되었습니다.");
        }

        boardWriteService.execute(boardCommand);

    
        switch (boardCommand.getBoardType()) {
            case "Free":
                return "redirect:/board/boardFreeList";
            case "Profit":
                return "redirect:/board/boardProfitList";
            case "Tip":
                return "redirect:/board/boardTipList";
            case "Question":
                return "redirect:/board/boardQueList";
            default:
                throw new IllegalArgumentException("유효하지 않은 boardType: " + boardCommand.getBoardType());
        }
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}