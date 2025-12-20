package springBootPickers.service.news;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import springBootPickers.command.NewsCommand;
import springBootPickers.domain.AuthInfoDTO;
import springBootPickers.domain.FileDTO;
import springBootPickers.domain.NewsDTO;
import springBootPickers.mapper.EmployeeMapper;
import springBootPickers.mapper.NewsMapper;

@Service
public class NewsUpdateService {

	@Autowired
	NewsMapper newsMapper;
	@Autowired
	EmployeeMapper employeeMapper;

	public void execute(NewsCommand newsCommand, HttpSession session, Model model) {
		NewsDTO dto = new NewsDTO();
		dto.setEmpNum(newsCommand.getEmpNum());
		dto.setNewsContent(newsCommand.getNewsContent());
		dto.setNewsNum(newsCommand.getNewsNum());
		dto.setNewsRegisterDate(newsCommand.getNewsRegisterDate());
		dto.setNewsTitle(newsCommand.getNewsTitle());
		dto.setNewsWriterDate(newsCommand.getNewsWriterDate());
		dto.setNewsWriterName(newsCommand.getNewsWriterName());

		AuthInfoDTO auth = (AuthInfoDTO) session.getAttribute("auth");
		String empNum = employeeMapper.getEmpNum(auth.getUserId());
		dto.setEmpNum(empNum);

//		System.out.println("DTO 상태: " + dto);
//		newsMapper.newsUpdate(dto);

		// 파일시스템에서 삭제
		// 1. 디렉터리 정보
		URL resource = getClass().getClassLoader().getResource("static/upload");
		String fileDir = resource.getFile();
		System.out.println(fileDir);
		if (newsCommand.getNewsMainImage() != null) {
			// 2. 파일객체를 불러오기
			MultipartFile mf = newsCommand.getNewsMainImage();
			// 3. 파일이름 가져오기
			String originalFile = mf.getOriginalFilename();
			// 4. 확장자 불리하기
			String extension = originalFile.substring(originalFile.lastIndexOf("."));
			// 5. 새로운 파일명 만들기
			String storeName = UUID.randomUUID().toString().replace("-", "");
			// 6. 새로운 파일명과 확장자 붙이기
			String storeFileName = storeName + extension;
			System.out.println("storeFileName : " + storeFileName);
			// 7. 파일객체 만들기
			File file = new File(fileDir + "/" + storeFileName);
			// 8. 파일 저장
			try {
				mf.transferTo(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 9. dto에 파일이름 저장
			dto.setNewsMainImage(originalFile);
			dto.setNewsMainStoreImage(storeFileName);
		}
		String originalTotal = "";
		String storeTotal = "";
		if (!newsCommand.getNewsDetailImage()[0].getOriginalFilename().isEmpty()) {
			// 1. 디렉터리 정보
			// 2. 파일객체를 불러오기
			for (MultipartFile mf : newsCommand.getNewsDetailImage()) {
				// 3. 파일이름 가져오기 :
				String originalFile = mf.getOriginalFilename();
				// 4. 확장자 불리하기
				String extension = originalFile.substring(originalFile.lastIndexOf("."));
				// 5. 새로운 파일명 만들기
				String storeName = UUID.randomUUID().toString().replace("-", "");
				// 6. 새로운 파일명과 확장자 붙이기
				String storeFileName = storeName + extension;
				// 7. 파일객체 만들기
				File file = new File(fileDir + "/" + storeFileName);
				// 8. 파일 저장
				try {
					mf.transferTo(file);
				} catch (Exception e) {
					e.printStackTrace();
				}
				originalTotal += originalFile + "/";
				storeTotal += storeFileName + "/";
			}
		}

		///// session에 있는 값은 삭제 , 넘어온 파일은 추가
		// session정보를 가져온다.
		List<FileDTO> list = (List<FileDTO>) session.getAttribute("fileList");
		// 파일의 정보를 디비로 부터 가져오기
		NewsDTO newsDTO = newsMapper.newsSelectOne(newsCommand.getNewsNum());
		// session에 있는 정보를 디비로부터 제거
		if (newsDTO.getNewsDetailImage() != null) {
			// 배열을 리스트로 받아옴
			List<String> dbOrg = new ArrayList<>(Arrays.asList(newsDTO.getNewsDetailImage().split("[/`]")));
			List<String> dbStore = new ArrayList<>(Arrays.asList(newsDTO.getNewsDetailStoreImage().split("[/`]")));
			/// session에 있는 데이터를 디비와 비교헤ㅐ서 디비에 있는데이터 삭제
			if (list != null) {
				for (FileDTO fdto : list) {
					for (String img : dbOrg) {
						if (fdto.getOrgFile().equals(img)) {
							dbOrg.remove(fdto.getOrgFile());
							dbStore.remove(fdto.getStoreFile());
							break;
						}
					}
				}
			}
			for (String img : dbOrg)
				originalTotal += img + "/";
			for (String img : dbStore)
				storeTotal += img + "/";
		}
		dto.setNewsDetailStoreImage(storeTotal);
		dto.setNewsDetailImage(originalTotal);

		int i = newsMapper.newsUpdate(dto);
		if (i > 0) {
			if (list != null) {
				for (FileDTO fd : list) {
					File file = new File(fileDir + "/" + fd.getStoreFile());
					if (file.exists())
						file.delete();
				}
			}
		}
	}
}