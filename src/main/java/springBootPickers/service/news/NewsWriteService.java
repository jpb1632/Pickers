package springBootPickers.service.news;

import java.io.File;
import java.net.URL;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import springBootPickers.command.NewsCommand;
import springBootPickers.domain.AuthInfoDTO;
import springBootPickers.domain.NewsDTO;
import springBootPickers.mapper.EmployeeMapper;
import springBootPickers.mapper.NewsMapper;

@Service
public class NewsWriteService {
	@Autowired
	NewsMapper newsMapper;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	EmployeeMapper employeeMapper;

	public void execute(NewsCommand newsCommand, HttpSession session) {
		NewsDTO dto = new NewsDTO();
		dto.setNewsTitle(newsCommand.getNewsTitle());
		dto.setNewsContent(newsCommand.getNewsContent());
		dto.setNewsWriterDate(newsCommand.getNewsWriterDate());
		dto.setNewsWriterName(newsCommand.getNewsWriterName());
//		dto.setNewsMainImage(newsCommand.getNewsMainImage());
//		dto.setNewsDetailImage(newsCommand.getNewsDetailImage());
		dto.setEmpNum(newsCommand.getEmpNum());
		dto.setNewsRegisterDate(newsCommand.getNewsRegisterDate());
		dto.setNewsNum(newsCommand.getNewsNum());

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
		MultipartFile mf = newsCommand.getNewsMainImage();
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

		dto.setNewsMainImage(originalFile);
		dto.setNewsMainStoreImage(storeFileName);

		// 상세 이미지 저장
		if (newsCommand.getNewsDetailImage() != null && newsCommand.getNewsDetailImage().length > 0) {
			StringBuilder originalTotal = new StringBuilder();
			StringBuilder storeTotal = new StringBuilder();

			for (MultipartFile mpf : newsCommand.getNewsDetailImage()) {
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

			dto.setNewsDetailImage(originalTotal.toString());
			dto.setNewsDetailStoreImage(storeTotal.toString());
		}

		newsMapper.newsInsert(dto);

	}

}