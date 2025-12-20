package springBootPickers.command;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import org.springframework.web.multipart.MultipartFile;
@Data
public class BoardCommand {
    private String boardNum;     
    private String memNum;       
    private String boardTitle;  
    private String boardContent;
    private String boardType;    
    private Date boardRegisterDate; 
}