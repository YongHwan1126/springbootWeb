package com.example.board4.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.board4.entity.Reply;


@Repository
public interface ReplyRepository  extends JpaRepository<Reply, Long> {
	List<Reply> findByBoardNoAndWriterNo(Long boardNo,Long WriterNo);
}
