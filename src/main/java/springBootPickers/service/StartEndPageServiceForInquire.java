package springBootPickers.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import springBootPickers.domain.StartEndPageForInquireDTO;

@Service
public class StartEndPageServiceForInquire {
	public StartEndPageForInquireDTO execute(int page, int limit, String searchWord, String memNum, String grade) {
		int startRow = ((page - 1) * limit) + 1;
		int endRow = (startRow + limit) - 1;
		StartEndPageForInquireDTO sepfiDTO = new StartEndPageForInquireDTO(startRow, endRow, searchWord, memNum, grade);
		return sepfiDTO;
	}

	public void execute(int page, int limit, int count, String searchWord, List list, Model model) {
		int limitPage = 10;
		int startPageNum = (int) ((double) page / limitPage + 0.95 - 1) * limitPage + 1;
		int endPageNum = startPageNum + limitPage - 1;
		int maxPage = (int) ((double) count / limit + 0.95);
		if (endPageNum > maxPage)
			endPageNum = maxPage;

		// 번호 시작 값 설정
		int startNum = (page - 1) * limit + 1;

		if (searchWord == null)
			searchWord = "";
		model.addAttribute("list", list);
		model.addAttribute("searchWord", searchWord);
		model.addAttribute("page", page);
		model.addAttribute("startPageNum", startPageNum);
		model.addAttribute("endPageNum", endPageNum);
		model.addAttribute("count", count);
		model.addAttribute("maxPage", maxPage);

		model.addAttribute("startNum", startNum); // 추가된 부분
	}

}
