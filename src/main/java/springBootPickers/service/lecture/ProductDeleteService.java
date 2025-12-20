package springBootPickers.service.lecture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springBootPickers.mapper.LectureMapper;

@Service
public class ProductDeleteService {
	@Autowired
	LectureMapper lectureMapper;
	public void execute(String memDels[]) {
		lectureMapper.productDelete(memDels);
	}
}
