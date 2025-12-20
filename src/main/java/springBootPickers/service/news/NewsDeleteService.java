package springBootPickers.service.news;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springBootPickers.mapper.NewsMapper;

//@Service
//public class NewsDeleteService {
//	@Autowired
//	NewsMapper newsMapper;
//
//	public void execute(String newsNum) {
//		  System.out.println("NewsDeleteService / newsDelete 실행됨: " + newsNum);
//		newsMapper.newsDelete(newsNum);
//	}
//}

@Service
public class NewsDeleteService {
	@Autowired
	NewsMapper newsMapper;

	public void execute(String newsNum) {
		System.out.println("🛠 삭제할 뉴스 번호: " + newsNum);

		if (newsNum == null || newsNum.trim().isEmpty() || newsNum.equalsIgnoreCase("newsList")) {
			System.out.println("⚠️ 잘못된 뉴스 번호로 삭제 요청 차단");
			return;
		}

		int result = newsMapper.newsDelete(newsNum);
		if (result == 0) {
			System.out.println("⚠️ 뉴스 삭제 실패! newsNum = " + newsNum);
		} else {
			System.out.println("✅ 뉴스 삭제 성공! newsNum = " + newsNum);
		}
	}
}
