package springBootPickers.service.lectureInquire;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springBootPickers.domain.LectureInquireDTO;
import springBootPickers.mapper.LectureInquireMapper;

@Service
public class LectureInquireDetailService {
	@Autowired
	LectureInquireMapper lectureInquireMapper;

	public LectureInquireDTO execute(String lectureQNum) {
		if (lectureQNum == null || lectureQNum.isEmpty()) {
			return null;
		}
		LectureInquireDTO dto = lectureInquireMapper.lectureInquireSelectOne(lectureQNum);
		if (dto == null) {
			// 해당 lectureQNum에 대한 데이터가 없을 경우 null 반환
			return null;
		}
		 System.out.println("질문 종류: " + dto.getLectureQType()); 
		return dto;
	}

}
