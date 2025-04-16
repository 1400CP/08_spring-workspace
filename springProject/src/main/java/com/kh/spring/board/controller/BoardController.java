package com.kh.spring.board.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.kh.spring.board.model.service.BoardServiceImpl;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.common.model.vo.PageInfo;
import com.kh.spring.common.template.Pagination;

@Controller
public class BoardController {
	
	@Autowired
	private BoardServiceImpl bService;
	// 메뉴바 클릭시 /list.bo (기본적으로 1페이지 요청)
	// 페이징바 클릭시 /list.bo?cpage=요청하는 페이지수
	@RequestMapping("list.bo")
	
//	public String selectListCount(@RequestParam(value="cpage", defaultValue="1") int currentPage, Model model) {
//	int listCount = bService.selectListCount();
//	
//	// 우리가 common에 만든 클래스와 메소드(공식)
//	PageInfo pi = Pagination.getPageInfo(listCount, currentPage, 5, 5);
//	
//	ArrayList<Board> list = bService.selectList(pi);

	// Model은 응답데이터를 위해서 사용하는 것.
//	model.addAttribute("pi", pi);
//	model.addAttribute("list", list);
	
	// 포워딩할 view (/WEB-INF/views/board/boardListView.jsp)
//	return "board/boardListView";
	
	public ModelAndView selectListCount(@RequestParam(value="cpage", defaultValue="1") int currentPage, ModelAndView mv) {	
		int listCount = bService.selectListCount();
		
		// 우리가 common에 만든 클래스와 메소드(공식)
		PageInfo pi = Pagination.getPageInfo(listCount, currentPage, 5, 5);
		
		ArrayList<Board> list = bService.selectList(pi);
		
		// ModelAndView를 활용하기
//		mv.addObject("pi", pi);
//		mv.addObject("list", list);
//		mv.setViewName("board/boardListView");
		
		// 반환형이 ModelAndView이다.
		// 따라서 아래와 같이 활용이 가능하다.
		mv.addObject("pi", pi)
		  .addObject("list", list)
		  .setViewName("board/boardListView");
		return mv;
	}
	
}
