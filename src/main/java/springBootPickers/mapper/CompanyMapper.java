package springBootPickers.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import springBootPickers.domain.CompanyDTO;
import springBootPickers.domain.StartEndPageDTO;
import springBootPickers.domain.StartEndPageForInquireDTO;

@Mapper
public interface CompanyMapper {
	public CompanyDTO companySelectOne(String companyNum);

	public void companyUpdate(CompanyDTO dto);

	public void companyDelete(String companyNum);

	public void companyInsert(CompanyDTO dto);

	public List<CompanyDTO> companySelectList(StartEndPageDTO sepDTO);

	public Integer companyCount();

	public List<CompanyDTO> searchCompaniesByName(String companyName);

}
