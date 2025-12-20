package springBootPickers.domain;

import java.util.Date;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("wordDTO")
public class WordDTO {
    String wordNum;
    String wordName;
    String wordDescription;
    Date wordRegisterDate;
    Date wordUpdateDate;
    String empNum;
}
