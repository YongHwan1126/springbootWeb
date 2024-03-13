package com.kh.thymeleaf8.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.kh.thymeleaf8.dto.MemberDTO;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class HomeController {
	@GetMapping("/home")
	public String home(Model model) {
		return "layouts/main";
	}

	@GetMapping("/thymeleaf-test3")
	public ModelAndView formPage(ModelAndView mav) {
		// 뷰페이지하고 데이터가 하나의 객체로 넘어간다.
		// 데이터 저장시에는 addObject("변수명","값");
		mav.addObject("msg", "아래 폼 값을 입력해주시고 Send 버튼을 클릭해주세요");
		mav.setViewName("thymeleaf-test3");
		return mav;
	}

	@PostMapping("/thymeleaf-test3")
	public ModelAndView formSend(ModelAndView mav, @RequestParam("data1") String data1) {
		log.info("formSend()실행");
		mav.addObject("msg", "회원님이 입력하신 값은 <span style = 'color:crimson'>" + "버튼을 클릭하세요" + data1 + "</span>");
		mav.addObject("data1", data1);
		// 뷰페이지를 저장
		mav.setViewName("thymeleaf-test3");
		return mav;
	}

	@GetMapping("/thymeleaf-test4")
	public ModelAndView multiFormPage(ModelAndView mav) {

		mav.addObject("msg", "아래 여러개의 폼 값을 입력하고 전송버튼을 누르세요!");
		mav.addObject("formData", new MemberDTO());
		mav.setViewName("thymeleaf-test4");
		// th:Object객체를 생성해서 넘겨주던지
		// 아니면 아에 없애고 처음부터 각각의 페이지를 만들어서
		// 분리시키던지! object 사용X

		return mav; // 객체 형식으로 리턴한다. (그안에 뷰페이지+객체)
	}

	@PostMapping("/thymeleaf-test4")
	public ModelAndView multiFormPageDto(MemberDTO dto, ModelAndView mav) {
		log.info("dto {} : ", dto);

		mav.addObject("msg", "잘 저장된 듯!");
		mav.addObject("formData", dto);
		mav.setViewName("thymeleaf-test4");

		return mav; // 객체 형식으로 리턴한다. (그안에 뷰페이지+객체)
	}

}
/*
 * spring.jpa.hibernate-ddl-auto=update create,alter,drop 정의시 DB의 고유의 기능을 사용할
 * 수있다. create: 기존 테이블을 삭제하고 새로생성[drop+create] create-drop:create속성에 추가로 어플리케이션을
 * 종료할 때 생성된 DDL을 제거 [drop+create+drop]
 * 
 * update : DB테이블과 엔티티 매핑 정보를 비교해서 변경 사항만 수정[테이블이 없을 경우 create]
 * 
 * validate : DB테이블과 엔티티 매핑 정보를 비교해서 차이가 있으면 경고를 남기고 어플리케이션을 실행하지 않음
 * 
 * none: 자동생성 기능을 사용하지 않음
 * 
 * sprign.jpa.generate-ddl=true - 엔티티 어노테이션을찾아서 ddl을 생성후 실행할 것인지 여부
 * 
 */