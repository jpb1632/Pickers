package springBootPickers.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import springBootPickers.domain.EmployeeDTO;

@Mapper
public interface EmployeeInfoMapper {
	public EmployeeDTO employeeSelectOne(String empId);
	public Integer employeeUpdate(EmployeeDTO dto);
	public Integer empPwUpdate(@Param("_newPw") String pw, @Param("_empId") String empId);
	public Integer empDelete(String empId);
}
