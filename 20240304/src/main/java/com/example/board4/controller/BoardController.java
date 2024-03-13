package com.example.board4.controller;


import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.board4.entity.Board;
import com.example.board4.entity.Reply;
import com.example.board4.repository.BoardRepository;
import com.example.board4.repository.ReplyRepository;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class BoardController {

	@Autowired
	private BoardRepository boardRepository;

	@Autowired
	private ReplyRepository replyRepository;
	@GetMapping("/")
	public String index(Model model, @RequestParam(defaultValue = "0") int page) {
		log.info("index() 실행");

		// 페이지 네이션을 위한 페이지 및 사이즈 설정
		// 페이지 사이즈 지정
		// @RequestParam(defaultValue="0") int page
		// 우리가 버튼을 눌러서 1페이지 2페이지 넘어가서 글을 확인하려고
		// 근데 시작하자마자 page값이 들어올 일이 없다 버튼을 안누르기때문에
		// 그럼 에러가 발생한다 그거 없애기 위해서 defaultValue에 기본값 설정을 한다.
		int pageSize = 5;

		// 스프링 jpa에서 페이지를 쿼리하는데 사용되는 Pageable
		// 안에 페이지 번호, 페이지 크기 , 정렬순서 등과 같은
		// 페이지에 관련된 정보를 제공하는 인터페이스!

		// PageRequest.of(매서드)를 이용해서 PageRequest
		// 객체를 생성할 수 있다.
		Pageable pageable = PageRequest.of(page, pageSize);
		// 페이지로부터 게시글 목록을 가져오기
		Page<Board> boardList = boardRepository.findAll(pageable);

		// 리스트 자체로 넘겨도 상관없지만
		// 페이지 인터페이스로 객체를 반환 받으면 그 내용들이 대부분
		// getContent에 리스트 형태로 저장이 되어있다.
		log.info("{}", boardList.getContent());
		log.info("{}", boardList);

		model.addAttribute("boardList", boardList.getContent());
		// page 변수를 이용해서 페이징 버튼을 만들 것!
		model.addAttribute("page", boardList);
		return "board/index";
	}

	@GetMapping("/board/detail/{no}")
	public String detail(@PathVariable("no") Long no, Model model) {
		log.info("no : {} ", no);
		// 데이터베이스에서 데이터 꺼내기
		Board board = boardRepository.findById(no).orElse(null);
		// 조회수 증가시키는 코드
		// null 값이라서 + 못함
		// 데이터베이스의 insert 값들을 확인해서 처리하면 된다.
		board.setReadCount(board.getReadCount()+1);
		boardRepository.save(board);
		// 로그 출력
				log.info("{}", board);
				log.info("{}", board.getReadCount());
		// 댓글을 조회해서 뷰페이지로 ㄱ같이 넘겨줘야한다.
		List<Reply> replyList = replyRepository.findByBoardNoAndWriterNo(no, board.getWriterNo());
		
		model.addAttribute("board", board);
		model.addAttribute("replyList", replyList);
		return "board/detail";
	}

	// 검색을 위한 메서드
	// 제목 또는 내용 또는 제목+내용으로 검색을 할 떄 사용하는 메서드
	@PostMapping("/board/search")
	public String boardSearch(@RequestParam(value = "searchOption", required = false) String searchOption,
			@RequestParam(value = "search", required = false) String keyword, Model model) {
		// 1. 페이지네이션 설정
		Page<Board> searchResult = null;
		Pageable pageable = PageRequest.of(0, 5);

		// 2. 내용, 제목,내용+제목 구별을 해야한다.
		if (searchOption != null && keyword != null) {
			switch (searchOption) {

			case "title":
				searchResult = boardRepository.findByTitleContaining(keyword, pageable);
				break;
			case "content":
				searchResult = boardRepository.findByContentContaining(keyword, pageable);
				break;

			case "titleContent":
				searchResult = boardRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable);
				break;
			}

		} else {
			// 위에 내용들이 모두 null 일 경우는 전체 선택!
			// findAll(페이지 설정 정보만 pageable을 보내준다.)
			searchResult = boardRepository.findAll(pageable);

		}
		// boards 현재 페이지 설정 정보랑 + db 모든 내용이 List형태로 담겨있다 그중에 getContent하면
		// db에 있는 내용만 뷰페이지로 전송된다.
		model.addAttribute("boardList", searchResult.getContent());
		model.addAttribute("page", searchResult);
		model.addAttribute("loginMember", "admin");
		return "board/index";
	}
	
	// 댓글 추가하는 내용
	@PostMapping("/board/reply")
	public String reply(@RequestParam(value="boardNo",required=false) Long boardNo,
			@RequestParam(value="writerId",required=false) String writerId,
			@RequestParam String content,
			RedirectAttributes redirect) {
		//1. 게시글 가져오기
		Board board = boardRepository.findById(boardNo).orElse(null);
		
		//2. 로그 출력
		log.info("reply()");
		log.info("board {}", board);
		log.info("reply()",boardNo);
		
		//3. 댓글 생성
		Reply reply = new Reply();
		reply.setBoardNo(boardNo);
		reply.setWriterNo(board.getWriterNo());
		reply.setContent(content);
		reply.setCreateDate(Date.valueOf(LocalDateTime.now().toLocalDate()));
		
		//4. db에 추가
		replyRepository.save(reply);
		//5.
		// 리다이렉트시 게시글 상세 페이지로 이동 할 수있도록 값을 저장할 수 있다.
		redirect.addAttribute("no", boardNo);
		return "redirect:/board/detail/{no}";
		
	}
}

//// 기본적으로 게시글을 가져오는 명령문!
//List<Board> boardList= boardRepository.findAll();
