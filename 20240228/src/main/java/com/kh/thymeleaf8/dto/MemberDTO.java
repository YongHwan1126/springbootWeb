package com.kh.thymeleaf8.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {
	private String id;
	private String name;
	private String email;
	private Integer age; // int 0 null 값을 가질수 없음. mull 값을 가지려면 Integer로 선언
	private String gender;
}
