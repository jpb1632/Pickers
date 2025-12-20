package springBootPickers.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import springBootPickers.domain.EmployeeDTO;
import springBootPickers.domain.StartEndPageDTO;

@Mapper
public interface EmployeeMapper {
	public void employeeInsert(EmployeeDTO dto);
	public List<EmployeeDTO> employeeSelectList(StartEndPageDTO sepDTO);
	public Integer employeeCount();
	public EmployeeDTO employeeSelectOne(String empNum);
	public void employeeUpdate(EmployeeDTO dto);
	public void empDelete(String empNum);
	public String getEmpNum(String empId);
}
