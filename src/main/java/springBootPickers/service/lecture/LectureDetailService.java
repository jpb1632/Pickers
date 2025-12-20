package springBootPickers.service.lecture;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import springBootPickers.domain.LectureDTO;
import springBootPickers.mapper.LectureMapper;

@Service
public class LectureDetailService {
    @Autowired
    LectureMapper lectureMapper;

    public void execute(String lectureNum, Model model) {
        // LectureDTO 객체를 가져옴
        LectureDTO dto = lectureMapper.lectureSelectOne(lectureNum);
        
        // 기존 이미지(디테일 이미지)와 새로운 이미지로 분리
        String[] originalImages = dto.getLectureDetailImage().split("/");
        String[] storeImages = dto.getLectureDetailStoreImage().split("/");

        // 필터링된 새로운 이미지 리스트만 모델에 추가
        List<String> newImages = new ArrayList<>();
        for (String image : originalImages) {
            if (!image.isEmpty()) {
                newImages.add(image);
            }
        }

        // 모델에 새로운 디테일 이미지만 전달
        model.addAttribute("lectureCommand", dto);
        model.addAttribute("newDetailImages", newImages);  // 새로 수정된 이미지들만
        model.addAttribute("newLine", "\n");
    }
}
