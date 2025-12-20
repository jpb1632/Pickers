package springBootPickers.service.lecture;

import java.io.File;
import java.net.URL;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import springBootPickers.command.LectureCommand;
import springBootPickers.domain.AuthInfoDTO;
import springBootPickers.domain.LectureDTO;
import springBootPickers.mapper.EmployeeMapper;
import springBootPickers.mapper.LectureMapper;

@Service
public class LectureWriteService {
    @Autowired
    EmployeeMapper employeeMapper;
    @Autowired
    LectureMapper lectureMapper;

    public void execute(LectureCommand lectureCommand, HttpSession session) {
        LectureDTO dto = new LectureDTO();
        dto.setLectureNum(lectureCommand.getLectureNum());
        dto.setLectureTeacherName(lectureCommand.getLectureTeacherName());
        dto.setLectureTitle(lectureCommand.getLectureTitle());
        dto.setLectureTopic(lectureCommand.getLectureTopic());
        dto.setLecturePrice(lectureCommand.getLecturePrice());
        dto.setLectureDescription(lectureCommand.getLectureDescription());
        dto.setLectureStatus(lectureCommand.getLectureStatus());

        AuthInfoDTO auth = (AuthInfoDTO) session.getAttribute("auth");
        String empNum = employeeMapper.getEmpNum(auth.getUserId());
        dto.setEmpNum(empNum);

     // 경로 설정: static/upload
	    URL resource = getClass().getClassLoader().getResource("static/upload");
	    if (resource == null) {
	        throw new IllegalStateException("Upload directory does not exist.");
	    }
	    String fileDir = resource.getPath();

	    File uploadDir = new File(fileDir);
	    if (!uploadDir.exists()) {
	        uploadDir.mkdirs(); // 경로가 없으면 생성
	    }

	    // 메인 이미지 저장
	    MultipartFile mf = lectureCommand.getLectureMainImage();
	    String originalFile = mf.getOriginalFilename();
	    String extension = originalFile.substring(originalFile.lastIndexOf("."));
	    String storeName = UUID.randomUUID().toString().replace("-", "");
	    String storeFileName = storeName + extension;

	    File file = new File(uploadDir, storeFileName);
	    try {
	        mf.transferTo(file);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    dto.setLectureMainImage(originalFile);
	    dto.setLectureMainStoreImage(storeFileName);

	    // 상세 이미지 저장
	    if (lectureCommand.getLectureDetailImage() != null && lectureCommand.getLectureDetailImage().length > 0) {
	        StringBuilder originalTotal = new StringBuilder();
	        StringBuilder storeTotal = new StringBuilder();

	        for (MultipartFile mpf : lectureCommand.getLectureDetailImage()) {
	            originalFile = mpf.getOriginalFilename();
	            extension = originalFile.substring(originalFile.lastIndexOf("."));
	            storeName = UUID.randomUUID().toString().replace("-", "");
	            storeFileName = storeName + extension;

	            file = new File(uploadDir, storeFileName);
	            try {
	                mpf.transferTo(file);
	            } catch (Exception e) {
	                e.printStackTrace();
	            }

	            originalTotal.append(originalFile).append("/");
	            storeTotal.append(storeFileName).append("/");
	        }

	        dto.setLectureDetailImage(originalTotal.toString());
	        dto.setLectureDetailStoreImage(storeTotal.toString());
	    }

        lectureMapper.lectureInsert(dto);
    }
}
