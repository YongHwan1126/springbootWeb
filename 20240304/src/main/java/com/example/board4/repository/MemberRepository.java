package com.example.board4.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.board4.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
