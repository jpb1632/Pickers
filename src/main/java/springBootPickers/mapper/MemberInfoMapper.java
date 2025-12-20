package springBootPickers.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import springBootPickers.domain.MemberDTO;

@Mapper
public interface MemberInfoMapper {

	public MemberDTO memberSelectOne(String memberId);

	public Integer memberUpdate(MemberDTO dto);

	public Integer memberPwUpdate(@Param("_newPw") String pw, @Param("_memId") String memId);

	public Integer memberDelete(String memId);

}
