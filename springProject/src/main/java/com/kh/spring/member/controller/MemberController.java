package com.kh.spring.member.controller;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kh.spring.member.model.service.MemberServiceImpl;
import com.kh.spring.member.model.vo.Member;

@Controller // Controller 타입의 어노테이션을 붙여주면 bean 스캐닝을 통해 자동으로 bean 등록
public class MemberController {
	
	@Autowired // DI (Dependency Injection) 특징
	private MemberServiceImpl mService; // Spring 기능 생성
	
	/*
	// RequestMapping 타입의 어노테이션을 붙여줌으로써 HanlerMapping 효과
	@RequestMapping(value="login.me")
	public void loginMember() {
		
		
	}
	
	@RequestMapping(value="insert.me")
	public void insertMember() {
		
		
	}
	
	public void updateMember() {
		
		
	}
	*/
	
	/*
	 * * 파라미터(요청시 전달값)를 받는 방법 (요청시 전달되는 값들 처리하는 방법)
	 * 
	 * 	1. HttpServletRequest를 이용해서 전달받기 (기존의 jsp/servlet 때 방식)
	 * 		해당 메소드의 매개변수로 HttpServletRequest를 작성해두면
	 * 		스프링 컨테이너가 해당 메소드 호출시(실행시) 자동으로 객체를 생성해서 인자로 주입해줌
	 */
	
	/*
	@RequestMapping("login.me")
	public String loginMember(HttpServletRequest request) {
		String userId = request.getParameter("id");
		String userPwd = request.getParameter("pwd");
		
		System.out.println("ID : " + userId);
		System.out.println("PWD : " + userPwd);
		
		return "main";
	}
	*/
	
	/*
	 * 2. @RequestParam 어노테이션을 이용하는 방버
	 * 		request.getparameter("키") : 밸류의 역할을 대신해주는 어노테이션
	 */

	/*
	@RequestMapping("login.me")
	public String loginMember( @RequestParam(value="id", defaultValue = "aaa") String userId, 
								@RequestParam(value="pwd") String userPwd) {
		System.out.println("ID : " + userId);
		System.out.println("PWD : " + userPwd);
		
		return "main";
	}
	*/
	
	/*
	 * 3. @RequestParam 어노테이션을 생략하는 방법
	 * 	*** 단, 매개변수명은 jsp의 name값과 동일하게 셋팅해줘야 자동으로 값이 주입된다.
	 */
	
	/*
	@RequestMapping("login.me")
	public String loginMember(String id, String pwd) {
		// 반드시 jsp의 name과 같은 값으로 넣어야 한다.
//		System.out.println("ID : " + id);
//		System.out.println("PWD : " + pwd);
		Member m = new Member();
		m.setUserId(id);
		m.setUserPwd(pwd);
		
		return "main";
	}
	*/
	
	/*
	 * 4. 커맨드 객체 방식
	 * 		해당 메소드의 매개변수로
	 * 		요청시 전달값을 담고자 하는 vo클래스 타입을 세팅 후
	 * 		요청시 전달값의 키값(jsp의 name값)을 vo클래스에 담고자하는 필드명으로 작성
	 *		
	 *		스프링 컨테이너가 해당 객체를 기본생성자로 생성 후 setter 메소드를 찾아서
	 *		요청시 전달값을 해당 필드에 담아주는 내부적인 원리
	 *		** 반드시 name 속성값(키값)과 담고자하는 필드명 동일해야 함.
	 */
	
	/*
	@RequestMapping("login.me")
	public String loginMember(Member m) {
		// jsp에서 name값을 Member vo랑 맞춰서 적어야 한다.
		
		Member loginUser = mService.loginMember(m);
		
		if(loginUser == null) { // 로그인 실패 => requestScope에 담고, 에러페이지로 포워딩
			System.out.println("로그인실패");
		}else { // 로그인 성공 => loginUser를 sessionScope에 담고, 메인페이지로 url 재요청
			System.out.println("로그인성공");
		}
		
		return "main";
	}
	*/
	
	/*
	 * * 요청 처리 후 응답데이터로 포워딩 또는 url 재요청, 응답데이터 담는 방법
	 * 1. 스프링에서 제공하는 Model 객체를 사용하는 방법
	 * 	포워딩할 뷰로 전달하고자 하는 데이터를 맵형식(key-value)으로 담을 수 있는 영역
	 * 	Model 객체는 requestScope이다.
	 * 	단, setAttribute()가 아닌 addAttribute() 메소드 이용
	 * 	
	 */
	
	@RequestMapping("login.me")
	public String loginMember(Member m, Model model, HttpSession session) {
		
		Member loginUser = mService.loginMember(m);
		
		if(loginUser == null) { // 로그인 실패 => 에러페이지로 포워딩
			model.addAttribute("errorMsg", "로긴실패"); 
			return "common/errorPage";
		}else { // 로그인 성공 => loginUser를 sessionScope에 담고, 메인페이지로 url 재요청
			session.setAttribute("loginUser", loginUser);
			return "redirect:/";
		}

	}
	// return "main"이 가능했던 이유는 servlet-context.xml덕분이었다.

}
