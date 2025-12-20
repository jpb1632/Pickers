package springBootPickers.service.lecture;

import java.io.File;
import java.net.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springBootPickers.domain.LectureDTO;
import springBootPickers.mapper.LectureMapper;

@Service
public class LectureDeleteService {
    @Autowired
    LectureMapper lectureMapper;

    public void execute(String lectureNum) {
        // 1. 강의 정보를 가져옵니다.
        LectureDTO dto = lectureMapper.lectureSelectOne(lectureNum);

        // 2. 강의 삭제를 위한 DB 처리
        int i = lectureMapper.lectureDelete(lectureNum);

        if (i > 0) {
            // 3. 이미지 파일 삭제
            // 기본 경로 (실제 경로로 수정 필요)
            URL resource = getClass().getClassLoader().getResource("static/upload");
            String fileDir = resource != null ? resource.getFile() : "";  // null 체크 추가

            // 4. 메인 이미지 삭제
            if (dto.getLectureMainImage() != null && !dto.getLectureMainImage().isEmpty()) {
                File mainImageFile = new File(fileDir + "/" + dto.getLectureMainImage());
                if (mainImageFile.exists()) {
                    mainImageFile.delete();  // 메인 이미지 삭제
                }
            }

            // 5. 추가 이미지 삭제 (여러 이미지가 있을 경우)
            if (dto.getLectureDetailStoreImage() != null && !dto.getLectureDetailStoreImage().isEmpty()) {
                String[] imageNames = dto.getLectureDetailStoreImage().split("/"); // 이미지 경로를 /로 구분
                for (String imageName : imageNames) {
                    if (!imageName.isEmpty()) {
                        File detailImageFile = new File(fileDir + "/" + imageName);
                        if (detailImageFile.exists()) {
                            detailImageFile.delete();  // 디테일 이미지 삭제
                        }
                    }
                }
            }
        }
    }
}
