package springBootPickers.command;

import java.util.Date;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import org.springframework.format.annotation.DateTimeFormat;

@Data
public class WordCommand {
    private String wordNum;
    @NotEmpty(message = "용어 이름을 입력해주세요.")
    private String wordName;
    private String wordDescription;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date wordRegisterDate; 
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date wordUpdateDate; 
    private String empNum;
}
