package springBootPickers.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import springBootPickers.domain.FileDTO;

@Service
public class FileDelService {
    public int execute(String orgFile, HttpSession session) {
        int i = 0;
        FileDTO dto = new FileDTO(); // 여러개를 한꺼번에 묶기 위해 dto 필요
        dto.setOrgFile(orgFile);
        Boolean newFile = true; // 새로운 파일인지 확인

        // session에서 기존 파일 리스트 가져오기
        List<FileDTO> list = (List<FileDTO>) session.getAttribute("fileList");
        if (list == null) {
            list = new ArrayList<FileDTO>();
        }

        // session에 있는 파일인지 확인 후 처리
        for (FileDTO fd : list) {
            if (fd.getOrgFile().equals(orgFile)) { // orgFile로 비교
                list.remove(fd); // 파일 제거
                newFile = false;
                break;
            }
        }

        if (newFile) {
            list.add(dto); // 새로운 파일 추가
            i = 1; // 새로 추가된 경우
        }

        session.setAttribute("fileList", list); // 수정된 리스트 저장
        return i;
    }
}
