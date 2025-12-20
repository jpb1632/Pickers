package springBootPickers.service.lectureInquire;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springBootPickers.mapper.LectureInquireMapper;

@Service
public class LectureInquireDeleteService {
	@Autowired
	LectureInquireMapper lectureInquireMapper;

	public void execute(String lectureQNum) {
		lectureInquireMapper.lectureInquireDelete(lectureQNum);

	}

}
