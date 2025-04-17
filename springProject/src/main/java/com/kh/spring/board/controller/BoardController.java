package com.kh.spring.board.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
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
	
	@RequestMapping("enrollForm.bo")
	public String enrollForm() {
		// /WEB-INF-views/  board/boardEnrollForm  .jsp
		return "board/boardEnrollForm";
	}
	
	/*
	 * * 다중파일 업로드 시?
	 * 	 여러 개의 input type="file" 요소에 다 동일한 키값으로 부여(upfile)
	 * 	 MultipartFile[] upfile로 받으면 된다 (0번 인덱스부터 차곡차곡 첨부파일부터 쌓인다.)
	 */	 
	@RequestMapping("insert.bo")
	public String insertBoard(Board b, MultipartFile upfile, HttpSession session, Model model) { // session이 필요하면 HttpSession을 적어라
		// name값을 vo값이랑 맞춰서 적어야 한다.
		// 업로드 파일값은 MultipartFile 매개변수의 name에 맞춰서 적는다.
		
//		System.out.println("보드 : " + b);
//		System.out.println("파일 : " + upfile);
		// maven에서 라이브러리를 추가로 받아야 한다. (pom.xml)
		// bean을 설정해야 한다. (root-context.xml)
		
		// 첨부파일은 어쨌든 생성된 객체
		// (다만, filename에 원본명의 유무, 사이즈가 0 또는 있는가로 구분)
		// 전달된 파일이 있을 경우 => 파일명 수정 작업 후 서버에 업로드 => 원본명, 서버업로드 된 경로를 b에 이어서 담기
		
		if(!upfile.getOriginalFilename().equals("")) {
			// 파일명 수정 작업 후 서버에 업로드 시키기
			// (flower.png => "2025041709362258741.png")
			/*
			String originName = upfile.getOriginalFilename(); // flower.png
			String currentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()); // 오늘 날짜, 현재 시간
			int ranNum = (int)(Math.random() * 90000 + 10000); // 5자리 랜덤값
			String ext = originName.substring(originName.lastIndexOf(".")); // 문자에서 마지막의 .
			
			// 결합하기
			String changeName = currentTime + ranNum + ext;
			
			// 업로드 하고자 하는 폴더의 물리적인 경로 알아내기
			String savePath = session.getServletContext().getRealPath("/resources/uploadFiles");
//			try {
//				upfile.transferTo(new File(savePath + changeName));
//			} catch (IllegalStateException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
			try {
				upfile.transferTo(new File(savePath + changeName));
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
			*/
			
			String changeName = saveFile(upfile, session);
			
			b.setOriginName(upfile.getOriginalFilename());
			b.setChangeName("resources/uploadFiles/" + changeName);

		}
		
		// 첨부파일이 있을 경우 b : 제목, 작성자, 내용, 파일 원본명, 파일 저장경로
		// 첨부파일이 없을 경우 b : 제목, 작성자, 내용
		int result = bService.insertBoard(b);
		
		if(result > 0) { // 성공 => 게시글 리스트페이지 (list.bo url재요청)
			session.setAttribute("alertMsg", "게시글 등록 성공");
			
			return "redirect:list.bo";
		}else { // 실패 => 에러페이지 포워딩
			model.addAttribute("errorMsg", "게시글 실패");
			return "common/errorPage";
		}
	}
	
	
	/*
	 * 현재 넘어온 첨부파일 그 자첼흘 서버의 폴더에 저장시키는 역할
	 */
	public String saveFile(MultipartFile upfile, HttpSession session) {
	// changeName을 리턴해야 하는 게 주목적이기 때문에 return을 그걸로!	
		String originName = upfile.getOriginalFilename(); // flower.png
		String currentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()); // 오늘 날짜, 현재 시간
		int ranNum = (int)(Math.random() * 90000 + 10000); // 5자리 랜덤값
		String ext = originName.substring(originName.lastIndexOf(".")); // 문자에서 마지막의 .
		
		// 결합하기
		String changeName = currentTime + ranNum + ext;
		
		// 업로드 하고자 하는 폴더의 물리적인 경로 알아내기
		String savePath = session.getServletContext().getRealPath("/resources/uploadFiles/");

		try {
			upfile.transferTo(new File(savePath + changeName));
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
		return changeName;
	}
	
	@RequestMapping("detail.bo")
//	public String detailView(int bno, Model model) {
	public ModelAndView detailView(int bno, ModelAndView mv) {
		// bno에는 상세조회 하고자하는 게시글 번호가 담겨있음
		
		// 해당 게시글 조회 증가용 서비스 호출 결과 받기
		int result = bService.increaseCount(bno);
		// >> 성공적 조회수 증가
		//		>> boardDetailView.jsp 상에 필요한 데이터 조회(게시글 상세정보 조회용 서비스 호출)
		if(result > 0) {
			Board b = bService.selectBoard(bno);		
//			model.addAttribute("result", result);
//			model.addAttribute("b", b);
//			
//			return "board/boardDetailView";
			mv.addObject("b", b)
			  .setViewName("board/boardDetailView");
		// >> 조회 증가 실패
		//		>> 에러문구 담아서 에러페이지 포워딩
		}else {
			mv.addObject("errorMsg", "조회수 증가 실패")
			  .setViewName("common/errorPage");
		}
		return mv;
	}
	
	@RequestMapping("delete.bo")
	public String deleteBoard(int bno, String filePath, HttpSession session, Model model) {
		// input에서 name의 bno로 매개변수 값을 받을 수 있다!
		int result = bService.deleteBoard(bno);
		
		if(result > 0) { // 삭제 => 리스트페이지로 이동
			// 첨부 파일이 있을 경우 => 파일 삭제
			if(!filePath.equals("")) { // 물리적인 경로는 application 단계로 넘어갈 수 있다. session에서 appl로
				new File(session.getServletContext().getRealPath(filePath)).delete();
			}
				// 게시판 리스트 페이지 list.bo url 재요청
				session.setAttribute("alertMsg", "게시글 삭제");
				return "redirect:list.bo";
		}else { // 실패 => 에러페이지
			model.addAttribute("errorMsg", "게시글 삭제 실패");
			return "common/errorPage";
		}
		
	}
	
	@RequestMapping("updateForm.bo")
	public String updateForm(int bno, Model model) {
		model.addAttribute("b", bService.selectBoard(bno));
		return "board/boardUpdateForm";
	}
	
	@RequestMapping("update.bo")
	public String updateBoard(Board b, MultipartFile reupfile, HttpSession session, Model model) {
	// 필요한 내용이 없기 때문에 input hidden을 통해서 추가한다.
		// name이 b.setBoardTitle 등과 같은 역할을 한다.
		// 첨부파일은 MultipartFile로 매개변수를 줘야 한다. (Board에 있지 않기 때문)
		if(!reupfile.getOriginalFilename().equals("")) { // 새 첨부파일이 있음
			
			// 기존 파일 있음 => 기존 첨부파일 삭제
			if(b.getOriginName() != null) {
				new File(session.getServletContext().getRealPath(b.getChangeName())).delete();
				// delete()를 통해서 삭제.
			}
				// 새로 넘어온 첨부파일 서버 업로드
				String changeName = saveFile(reupfile, session); // 반환형 String
				
					// b에 새로 넘어온 첨부파일에 대한 원본명, 저장경로 담기
					b.setOriginName(reupfile.getOriginalFilename());
					b.setChangeName("resources/uploadFiles/" + changeName);
					// 여기 잘못쓰면 db에 이상하게 들어갑니다.

			// 기존 파일 없음 => 작성하지 않겠음.
		}
		
		/*
		 * b에 boardNo, boardTitle, boardContent 무조건 담겨있음
		 * 
		 * 1. 새로 첨부된 파일 x, 기존 첨부파일 x
		 * 	=> originName : null, changeName : null
		 * 
		 * 2. 새로 첨부된 파일 x, 기존 첨부파일 o	
		 * 	=> originName : 기존파일원본명, changeName : 기존파일저장경로
		 * 
		 * 3. 새로 첨부된 파일 o, 기존 첨부파일 x
		 * 	=> 새로 첨부된 파일 서버 업로드
		 * 	=> originName : 새파일원본명, changeName : 새파일저장경로
		 * 
		 * 4. 새로 첨부된 파일 o, 기존 첨부파일 o
		 * 	=> 기존파일 삭제
		 * 	=> 새로 첨부된 파일 서버 업로드
		 * 	=> originName : 새파일원본명, changeName : 새파일저장경로
		 */
		
		int result = bService.updateBoard(b);
		
		if(result > 0) { // 성공 => 상세페이지
			session.setAttribute("alertMsg", "게시글 수정 성공");
			return "redirect:detail.bo?bno=" + b.getBoardNo();

		}else { // 실패 => 에러페이지
			model.addAttribute("errorMsg", "게시글 수정 실패");
			return "common/errorPage";

		}
	}
	
}
