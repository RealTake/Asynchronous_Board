package com.finotek.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.finotek.board.dto.BoardDTO;
import com.finotek.board.service.BoardService;

import java.util.stream.Stream;

import javax.servlet.http.HttpSession;

@Controller
public class BoardController {

	@Autowired
	public BoardService service; 	// BoardService 객체 바인딩

    // 매인페이지를 불러오는 메소드
	@RequestMapping(value="/board")
    public String boardPage() { return "board/boardList"; }

	// 매인페이지를 불러오는 메소드
    @RequestMapping(value="/board2")
	public String boardPage2(Model model, Authentication authentication) {
    	String auth = authentication.getAuthorities().parallelStream().findAny().toString();
    	String name = authentication.getName();
    	model.addAttribute("AccountInfo", service.getAccountInfo_S(name));
    	return "board/boardList2";
    }

    // 매인페이지를 불러오는 메소드
    @RequestMapping(value="/")
	public String mainPage2(Model model, Authentication authentication) {
		String name = authentication.getName();
		model.addAttribute("AccountInfo", service.getAccountInfo_S(name));
		return "board/boardList2";
	}


//	//  포스팅된 글을 불러올 메소드
//	@GetMapping(value="/viewPost/{bid}")
//	public String boardList(@PathVariable(value = "bid") int bid, Authentication authentication, Model model)
//	{ service.getPost_S(bid, model, authentication);	return "board/postView"; }

	//  포스팅된 글을 불러올 메소드
	@GetMapping(value="/viewPost2/{bid}")
	public String boardList2(@PathVariable int bid, Authentication authentication, Model model)
	{
		String name = authentication.getName();
		model.addAttribute("AccountInfo", service.getAccountInfo_S(name));
		service.getPost_S(bid, model, authentication);
		
		return "board/postView2";
	}

	// 작성된 글을 저장하는 메소드
	@ResponseBody
	@PostMapping(value="/writePost")
	public String writePost(BoardDTO dto, Authentication authentication)
	{ return service.writePost_S(dto, authentication); }

	// 작성글을 삭제하는 메소드
	@ResponseBody
	@GetMapping(value="/deletePost/{bid}")
	public String deletePost(@PathVariable(value = "bid") int bid, Authentication authentication)
	{ return service.deletePost_S(bid, authentication); }

	// 작성글들을 찾아주는 메소드
	@ResponseBody
	@GetMapping(value="/searchPost.json/{pageNum}", produces = "application/json; charset=UTF-8")
	public String searchPost(@RequestParam String content, @PathVariable(value = "pageNum") int pageNum, Authentication authentication)
	{ return service.getSearchPostList_S(content, pageNum, authentication); }

//	// 글 수정 페이지를 반환
//	@GetMapping(value="/modifyPage/{bid}")
//	public String modifyPage(@ModelAttribute @PathVariable(value = "bid") int bid, Model model, Authentication authentication)
//	{
//		service.getPost_S(bid, model, authentication);
//		return "board/modifyPage";
//	}

	// 새로운 수정기능을 위해 글 내용을 JSON으로 반환
	@ResponseBody
	@GetMapping(value="/Post/{bid}", produces = "application/json; charset=UTF-8")
	public Object modifyP(@ModelAttribute @PathVariable(value = "bid") int bid, Authentication authentication)
	{
		return service.getPostA_S(bid, authentication);
	}

	// 글 수정 메소드
	@ResponseBody
	@PostMapping(value="/modifyPost/{bid}")
	public String modifyPost(@PathVariable(value = "bid") int bid, BoardDTO dto, Authentication authentication)
	{
	    return service.modifyPost_S(bid, dto, authentication);
    }

    // 게시글의 개수를 알려주는 메소드
    @ResponseBody
    @GetMapping(value="/count")
    public int getCount(@RequestParam String content, Authentication authentication){
        return service.getCount_S(content, authentication);
    }
    
    @GetMapping(value="/write/{count}")
    public void getCount(@PathVariable int count){
        service.writeTest(count);
        
    }

}
