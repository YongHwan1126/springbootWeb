package com.example.board4.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.board4.entity.Board;
import com.example.board4.repository.BoardRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class BoardRestController {

	@Autowired
	private BoardRepository respository;

	// ResponseEntity<?> 메서드 실행마다 타입을 지정해서 보낼수 있다,
	// ResponseEntity<Board> 이렇게 쓰면 타입이 지정되서 Board 타입이 아니면 에러가 난다!
	@GetMapping("/board/detailModal/{boardNo}")
	public ResponseEntity<?> getBoardDetail(@PathVariable("boardNo") Long id) {
		// 1. 로그 출력
		log.info("id : {} ", id);

		// 2. 아이디를 기준으로 Board엔티티를 데이터베이스에서 찾는다.
		Board board = respository.findById(id).orElse(null);

		// 조회수를 올려주는 코드
		board.setReadCount(board.getReadCount() + 1);
		respository.save(board);
		log.info("board : {}", board);

		// 찾는 board가 없는 경우
		if (board == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found id : " + id);
		}
		return ResponseEntity.ok(board);

	}

	// 전체 내용을 가져가는 메서드
	@GetMapping("/board/all")
	public ResponseEntity<?> boardAll() {
		// 모든 Board 엔티티를 조회한다
		List<Board> list = respository.findAll();

		// 조회된 list가 없는 경우
		if (list == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("없는데?");
		}
		log.info("board/all : {} ", list);
		return ResponseEntity.ok(list);
	}

	//
	@PostMapping("/board/searchModal")
	public ResponseEntity<?> searchModal(@RequestParam("keyword") String keyword) {
		log.info("searchModal() keyword : {}  실행", keyword);
		// 제목 또는 내용에 keyword값이 포함하는 board 엔티티를 만든다.
		List<Board> list = respository.findByTitleContainingOrContentContaining(keyword, keyword);
		// 조회된 list가 없는 경우
		if (list == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found keyword:" +keyword);
		}
		log.info("board/all : {} ", list);
		return ResponseEntity.ok(list);
	}
}
