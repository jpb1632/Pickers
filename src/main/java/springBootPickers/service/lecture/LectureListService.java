package springBootPickers.service.lecture;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import springBootPickers.domain.LectureDTO;
import springBootPickers.domain.StartEndPageDTO;
import springBootPickers.mapper.LectureMapper;
import springBootPickers.service.StartEndPageService;

@Service
public class LectureListService {
	@Autowired
	LectureMapper lectureMapper;
	@Autowired
	StartEndPageService startEndPageService;
	public void execute(String searchWord, int page, Model model) {
		int limit = 6;
		StartEndPageDTO sepDTO = startEndPageService.execute(page, limit, searchWord);
		List<LectureDTO> list = lectureMapper.allSelect(sepDTO);
		int count = lectureMapper.lectureCount(searchWord);
		startEndPageService.execute(page, limit, count, searchWord, list, model);
	}
}
