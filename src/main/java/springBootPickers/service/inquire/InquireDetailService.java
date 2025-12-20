package springBootPickers.service.inquire;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springBootPickers.domain.InquireDTO;
import springBootPickers.mapper.InquireMapper;

@Service
public class InquireDetailService {
	@Autowired
	InquireMapper inquireMapper;

	public InquireDTO execute(String inquireNum) {
	    if (inquireNum == null || inquireNum.isEmpty()) {
	        return null;
	    }
	    InquireDTO dto = inquireMapper.inquireSelectOne(inquireNum);
	    if (dto == null) {
	        // 해당 inquireNum에 대한 데이터가 없을 경우 null 반환
	        return null;
	    }
	    return dto;
	}


//	public InquireDTO execute(String inquireNum) {
//		InquireDTO dto = inquireMapper.inquireSelectOne(inquireNum);
//		if (dto == null) {
//			throw new IllegalArgumentException("해당 문의가 존재하지 않습니다.");
//		}
//		return dto; // DTO를 Controller로 반환
//		//return inquireMapper.inquireSelectOne(inquireNum);
//	}
//	
//	

}
