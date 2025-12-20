package springBootPickers.domain;

import java.util.Date;

import org.apache.ibatis.type.Alias;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Data
@Alias("boardDTO")
public class BoardDTO {
    private String boardNum;
    private String memNum;
    private String memId;  // 회원 아이디 추가
    private String boardTitle;
    private String boardContent;
    private String boardType; // 영어 타입
    private String boardTypeKorean; // UI에 표시할 한글 타입
    private Date boardRegisterDate;
    private Date boardUpdateDate;
    private Integer likeCount;
    private Integer viewCount;
    private String empNum;
    private String likeMembers;
}

