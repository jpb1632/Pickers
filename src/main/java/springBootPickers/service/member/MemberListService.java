package springBootPickers.service.member;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import springBootPickers.domain.MemberDTO;
import springBootPickers.domain.StartEndPageDTO;
import springBootPickers.mapper.MemberMapper;
import springBootPickers.service.StartEndPageService;

@Service
public class MemberListService {
	@Autowired
	MemberMapper memberMapper;
	@Autowired
	StartEndPageService startEndPageService;

	public void execute(Integer page, String searchWord, Model model) {
		Integer limit = 10;

		StartEndPageDTO sepDTO = startEndPageService.execute(page, limit, searchWord);
		List<MemberDTO> list = memberMapper.memberSelectList(sepDTO);

		Integer count = memberMapper.memberCount();
		startEndPageService.execute(page, limit, count, searchWord, list, model);

	}

}
