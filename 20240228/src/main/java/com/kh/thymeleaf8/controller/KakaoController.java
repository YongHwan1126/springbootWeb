package com.kh.thymeleaf8.controller;

import java.net.URI;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import com.kh.thymeleaf8.vo.KakaoApproveResponse;
import com.kh.thymeleaf8.vo.KakaoReadyResponse;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class KakaoController {
	
	// tid 변수를 다른 메서드에서 가지고 사용하기 위해서
	// 변수 생성됨 
	KakaoReadyResponse vo;
	
	@GetMapping("/")
	public String kakaoPay(Model model) {
		log.info("kakaoPay() 메서드 실행");
		// 전송을 하기 위해서 필요한 resttemplate
		RestTemplate rt = new RestTemplate();
		
		// 서버로 요청할 header
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "KakaoAK 68aa14df9951b3b57b1ff86c24828014");
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		// 서버로 요청할 Body
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		params.add("cid", "TC0ONETIME");
		params.add("partner_order_id", "1004");
		params.add("partner_user_id", "test01");
		// item_name 부분은 나중에 상품명으로 변경
		params.add("item_name", "초코파이");
		params.add("quantity", "5");
		params.add("total_amount", "2200");
		params.add("tax_free_amount", "0");
		params.add("approval_url", "http://localhost:9090/payment/success");
		params.add("fail_url", "http://localhost:9090/payment/fail");
		params.add("cancel_url", "http://localhost:9090/payment/cancel");
		params.add("msg", "결제가 완료되었습니다.");
		
		// 위에 params랑 header 묶어서 전송해야된다 바로 httpEntity
		HttpEntity<MultiValueMap<String, Object>> body = new HttpEntity<>(params,headers);
		
		System.out.println(body);
		
		try {
			// 전송 vo에 저장해라
			vo = rt.postForObject(new URI("https://kapi.kakao.com/v1/payment/ready"),body,KakaoReadyResponse.class);
			
			log.info("전달됐니? {}" , vo);
			
			return "redirect:"+vo.getNext_redirect_pc_url();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "kakao";
	}
	@GetMapping("/payment/success")
	public String success(String pg_token,Model model) {
		log.info("success get...........()");
		log.info("pg_token {}",pg_token);
		// 1. 전송하기 위해서 RestTemplate 
		RestTemplate rt = new RestTemplate();
		
		// 2. 헤더 고대로 가져오기
		// 서버로 요청할 header
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "KakaoAK 68aa14df9951b3b57b1ff86c24828014");
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		// 3. 서버로 요청할 Body
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		params.add("cid", "TC0ONETIME");
		params.add("partner_order_id", "1004");	
		params.add("partner_user_id", "test01");
		params.add("tid", vo.getTid());
		params.add("pg_token", pg_token);
		
		// 4. 위에 header와 body를 합쳐서 전송해야됨
		HttpEntity<MultiValueMap<String, Object>> body = new HttpEntity<>(params,headers);
		
		System.out.println(body);
		
		// 전송 
		KakaoApproveResponse app =  rt.postForObject("https://kapi.kakao.com/v1/payment/approve", body, KakaoApproveResponse.class);
		log.info("approveResponse {}",app);
		model.addAttribute("info", app);
		return "kakaoPaySuccess";
	}
}
