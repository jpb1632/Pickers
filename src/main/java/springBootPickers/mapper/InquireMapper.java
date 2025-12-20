package springBootPickers.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import springBootPickers.domain.InquireDTO;
import springBootPickers.domain.StartEndPageForInquireDTO;

@Mapper
public interface InquireMapper {

	public List<InquireDTO> inquireSelectList(StartEndPageForInquireDTO sepfiDTO); // 모든 문의내역

	public void inquireInsert(InquireDTO dto);

	public InquireDTO inquireSelectOne(String inquireNum);

	public Integer inquireCount(String memNum, String grade);

	public List<InquireDTO> inquireSelectListForMember(StartEndPageForInquireDTO sepfiDTO); // 회원의 문의내역만 조희;

	public void inquireAnswer(InquireDTO dto);

	public String getMemberNameByMemNum(String memNum);

	public void inquireDelete(String inquireNum);

	public void updateInquireStatusToAnswered(String inquireNum);






	


}
