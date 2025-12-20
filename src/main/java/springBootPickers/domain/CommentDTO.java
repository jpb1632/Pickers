package springBootPickers.domain;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("commentDTO")
public class CommentDTO {
    private String commentNum;
    private String commentContent;
    private String boardNum;
    private String memNum;
    private String memId; // 회원 아이디 필드 추가
    private String boardType;
    private Date commentRegisterDate;
}
