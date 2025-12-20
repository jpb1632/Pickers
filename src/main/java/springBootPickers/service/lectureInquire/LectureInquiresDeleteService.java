package springBootPickers.service.lectureInquire;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springBootPickers.mapper.AutoNumMapper;

@Service
public class LectureInquiresDeleteService {
	@Autowired
	AutoNumMapper autoNumMapper;

	public void execute(String[] lectureQNums) {
		autoNumMapper.numsDelete(lectureQNums, "lecture_q", "lecture_q_num");
	}
}
