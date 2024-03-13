package com.kh.thymeleaf8.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KakaoReadyResponse {
	private String tid; //결제 고유 번호
	private String next_redirect_app_url;//모바일 앱일 경우  카카오톡 결제 페이지 Redirect URL
	private String next_redirect_mobile_url; // 모바일 웹일 경우 카카오톡 결제 페이지 Redirect URL
	private String next_redirect_pc_url;// pc 웹일 경우 카카오톡 결제 페이지 Redirect URL
	private String created_at;// 결제 준비 요청 시간
	
}
