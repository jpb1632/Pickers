package springBootPickers.service.lecture;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import springBootPickers.command.LectureCommand;
import springBootPickers.domain.AuthInfoDTO;
import springBootPickers.domain.FileDTO;
import springBootPickers.domain.LectureDTO;
import springBootPickers.mapper.EmployeeMapper;
import springBootPickers.mapper.LectureMapper;

@Service
public class LectureUpdateService {
    @Autowired
    LectureMapper lectureMapper;
    @Autowired
    EmployeeMapper employeeMapper;

    public void execute(LectureCommand lectureCommand, HttpSession session, Model model) {
        LectureDTO dto = new LectureDTO();
        dto.setLectureDescription(lectureCommand.getLectureDescription());
        dto.setLectureTitle(lectureCommand.getLectureTitle());
        dto.setLectureNum(lectureCommand.getLectureNum());
        dto.setLecturePrice(lectureCommand.getLecturePrice());
        dto.setLectureStatus(lectureCommand.getLectureStatus());
        dto.setLectureTeacherName(lectureCommand.getLectureTeacherName());
        dto.setLectureTopic(lectureCommand.getLectureTopic());

        AuthInfoDTO auth = (AuthInfoDTO) session.getAttribute("auth");
        String empNum = employeeMapper.getEmpNum(auth.getUserId());
        dto.setEmpNum(empNum);

        // 디렉터리 경로 확인
        URL resource = getClass().getClassLoader().getResource("static/upload");
        String fileDir = resource != null ? resource.getFile() : "";
        System.out.println("파일 저장 경로: " + fileDir);

        // 기존 이미지 파일 처리 및 새 이미지 저장
        if (lectureCommand.getLectureMainImage() != null) {
            MultipartFile mf = lectureCommand.getLectureMainImage();
            String originalFile = mf.getOriginalFilename();
            String extension = originalFile.substring(originalFile.lastIndexOf("."));
            String storeName = UUID.randomUUID().toString().replace("-", "");
            String storeFileName = storeName + extension;
            File file = new File(fileDir + "/" + storeFileName);
            try {
                mf.transferTo(file);
                System.out.println("파일 저장 완료: " + storeFileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            dto.setLectureMainImage(originalFile);
            dto.setLectureMainStoreImage(storeFileName);
        }

        // 디테일 이미지 처리
        String originalTotal = "";
        String storeTotal = "";
        if (!lectureCommand.getLectureDetailImage()[0].getOriginalFilename().isEmpty()) {
            for (MultipartFile mf : lectureCommand.getLectureDetailImage()) {
                String originalFile = mf.getOriginalFilename();
                String extension = originalFile.substring(originalFile.lastIndexOf("."));
                String storeName = UUID.randomUUID().toString().replace("-", "");
                String storeFileName = storeName + extension;
                File file = new File(fileDir + "/" + storeFileName);
                try {
                    mf.transferTo(file);
                    System.out.println("파일 저장 완료: " + storeFileName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                originalTotal += originalFile + "/";
                storeTotal += storeFileName + "/";
            }
        }

        // 기존 디테일 이미지 삭제 (삭제된 이미지 파일 찾기)
        LectureDTO lectureDTO = lectureMapper.lectureSelectOne(lectureCommand.getLectureNum());
        if (lectureDTO.getLectureDetailImage() != null) {
            List<String> dbOrg = new ArrayList<>(Arrays.asList(lectureDTO.getLectureDetailImage().split("/")));
            List<String> dbStore = new ArrayList<>(Arrays.asList(lectureDTO.getLectureDetailStoreImage().split("/")));

            // 기존 상세 이미지 삭제
            for (String img : dbStore) {
                File file = new File(fileDir + "/" + img);
                if (file.exists()) {
                    boolean deleted = file.delete();
                    System.out.println("기존 파일 삭제: " + file.getName() + " - " + deleted);
                }
            }

            // 새로운 파일 경로 설정
            dto.setLectureDetailStoreImage(storeTotal);
            dto.setLectureDetailImage(originalTotal);
        }

        // 강의 업데이트
        int i = lectureMapper.lectureUpdate(dto);
        if (i > 0) {
            // 성공적으로 업데이트되면, 세션에 저장된 이미지 파일을 삭제
            List<FileDTO> list = (List<FileDTO>) session.getAttribute("fileList");
            if (list != null) {
                for (FileDTO fd : list) {
                    File file = new File(fileDir + "/" + fd.getStoreFile());
                    if (file.exists()) {
                        boolean deleted = file.delete();
                        System.out.println("세션 파일 삭제: " + file.getName() + " - " + deleted);
                    }
                }
            }
        }
    }
}
