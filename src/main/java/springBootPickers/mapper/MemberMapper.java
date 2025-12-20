package springBootPickers.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import springBootPickers.domain.MemberDTO;
import springBootPickers.domain.StartEndPageDTO;

@Mapper
public interface MemberMapper {
	public void memberInsert(MemberDTO dto);
	public List<MemberDTO> memberSelectList(StartEndPageDTO sepDTO);
	public Integer memberCount();
	public MemberDTO memberSelectOne(String memNum);
	public void memberUpdate(MemberDTO dto);
	public void memDelete(String memNum);

	public String memberNumSelect(String memId);

	public String getMemNum(String memId);

}