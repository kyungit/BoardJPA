package com.board.controller;

import java.io.File;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.board.util.TempPasswordMaker;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.board.dto.AddressDTO;
import com.board.dto.MemberDTO;
import com.board.entity.AddressEntity;
import com.board.service.MemberService;
import com.board.util.PageUtil;

@Controller
public class MemberController {

	@Autowired
	MemberService service;
	
	@Autowired
	private BCryptPasswordEncoder pwdEncoder;
	
	//로그인 화면 보기 GET
	@GetMapping("/member/login")
	public void getLogin() { }
	
	//로그인 하기 -> 오버로딩, 인자가 달라서 메소드 명이 같아도 괜찮음
	@PostMapping("/member/login")
	public void postLogin() {
		
	}
	
	//POST 로그인 하기
	@ResponseBody
	@PostMapping("/member/loginCheck")
	public String postLogin(MemberDTO member,HttpSession session) {
		
		//아이디 존재 여부 확인
		if(service.idCheck(member.getEmail()) == 0) {
			return "{\"message\":\"ID_NOT_FOUND\"}";
		}
		
		//패스워드가 올바르게 들어 왔는지 확인
		if(!pwdEncoder.matches(member.getPassword(), service.memberInfo(member.getEmail()).getPassword())) {
			//잘못된 패스워드 일때
			return "{\"message\":\"PASSWORD_NOT_FOUND\"}";
		}	
		return "{\"message\":\"GOOD\"}";
	}
	
	//회원 등록 화면 보기
	@GetMapping("/member/signup")
	public void getSignup() {	}
	
	//회원 등록 하기
	@ResponseBody
	@PostMapping("/member/signup")
	public Map<String,String> postSignup(MemberDTO member, @RequestParam("fileUpload") MultipartFile multipartFile) throws Exception {
		
		String path = "c:\\Repository\\profile\\";
		File targetFile;
		
		if(!multipartFile.isEmpty()) {
			
			String org_filename = multipartFile.getOriginalFilename();
			String org_fileExtension = org_filename.substring(org_filename.lastIndexOf("."));			
			String stored_filename = UUID.randomUUID().toString().replaceAll("-", "") + org_fileExtension;
			
			try {
				targetFile = new File(path + stored_filename);				
				multipartFile.transferTo(targetFile);
				
				member.setOrg_filename(org_filename);
				member.setStored_filename(stored_filename);
				member.setFilesize(multipartFile.getSize());				
				
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			String inputPassword = member.getPassword();
			String pwd = pwdEncoder.encode(inputPassword); //단방향 암호화 
			member.setPassword(pwd);
			member.setLastpwdate(LocalDateTime.now());
			
		}
		
		service.memberInfoRegistry(member);
		
		Map<String,String> data = new HashMap<>();
		data.put("message", "GOOD");
		data.put("username", URLEncoder.encode(member.getUsername(),"UTF-8"));
		
		return data;
		
		//return "{\"message\":\"GOOD\",\"username\":\"" + URLEncoder.encode(member.getUsername(),"UTF-8") + "\"}";
	}
	
	//회원 가입 시 아이디 중복 확인
	@ResponseBody
	@PostMapping("/member/idCheck")
	public int postIdCheck(@RequestBody String email) throws Exception{
		
		int result = service.idCheck(email);
		return result;
	}
	
	//회원 정보 보기
	@GetMapping("/board/memberInfo")
	public void getMemberInfo(HttpSession session, Model model) throws Exception {
		String email = (String)session.getAttribute("email");
		model.addAttribute("memberInfo", service.memberInfo(email));
	}
	
	//회원 패스워드 변경 GET
	@GetMapping("/member/memberPasswordModify")
	public void getMemberPasswordModify() throws Exception { 
	}
	
	//회원 기본 정보 변경
	@GetMapping("/board/memberInfoModify")
	public void getMemberInfoModify(Model model) {
		
		
	}
	
	//회원 패스워드 변경
	@ResponseBody
	@PostMapping("/member/memberPasswordModify")
	public String postMemberPasswordModify(@RequestParam("old_password") String old_password, 
				@RequestParam("new_password") String new_password, HttpSession session) throws Exception { 
		
		String email = (String)session.getAttribute("email");		
		
		//패스워드가 올바르게 들어 왔는지 확인
		if(!pwdEncoder.matches(old_password, service.memberInfo(email).getPassword())) {
			return "{\"message\":\"PASSWORD_NOT_FOUND\"}";
		}
		
		//신규 패스워드로 업데이트
		MemberDTO member = new MemberDTO();
		member.setEmail(email);		
		member.setPassword(new_password);
		member.setLastpwdate(LocalDateTime.now());
		System.out.println("패스워드 변경 member.email : " + member.getEmail());
		System.out.println("패스워드 변경 new_password : " + new_password);
		
		service.memberPasswordModify(member);
		
		return "{\"message\":\"GOOD\"}";
	}
	
	//로그아웃
	@GetMapping("/member/logout")
	public String getLogout(HttpSession session,Model model) {
//		String email = (String)session.getAttribute("email");
//		String username = (String)session.getAttribute("username");
//		
//		System.out.println("session:email => " + email);
//		
//		MemberDTO member = new MemberDTO();
//		member.setEmail(email);
//		member.setLastlogoutdate(LocalDateTime.now());
//		
//		service.lastlogoutdateUpdate(member);
//				
//		
//		model.addAttribute("email", email);
//		model.addAttribute("username", username);
//		//session.invalidate(); //모든 세션 종료 --> 로그아웃...
		
		return "redirect:/";
	}
	
	//패스워드 변경 후 세션 종료
	@GetMapping("/member/memberSessionOut")
	public String getMemberSessionOut(HttpSession session) {
		
		MemberDTO member = new MemberDTO();
		member.setEmail((String)session.getAttribute("email"));
		System.out.println("email => " + member.getEmail());
		member.setLastlogoutdate(LocalDateTime.now());
		service.lastlogoutdateUpdate(member);
		session.invalidate();	
		
		return "redirect:/";
	}
	
	//아이디 찾기
	@GetMapping("/member/searchID")
	public void getSearchID() {}
	
	//아이디 찾기
	@ResponseBody
	@PostMapping("/member/searchID")
	public String postSearchID(MemberDTO member) {
		
		String email = service.searchID(member) == null?"ID_NOT_FOUND":service.searchID(member);		
		return "{\"message\":\"" + email + "\"}";
	}
	
	//임시 패스워드 생성
	@GetMapping("/member/searchPassword")
	public void getSearchPassword() {}
	
	//임시 패스워드 발급
	@ResponseBody
	@PostMapping("/member/searchPassword")
	public String postSearchPassword(MemberDTO member) throws Exception{
		//아이디 존재 여부 확인
		if(service.idCheck(member.getEmail()) == 0)
			return "{\"status\":\"ID_NOT_FOUND\"}";
		//TELNO 확인
		if(!service.memberInfo(member.getEmail()).getTelno().equals(member.getTelno()))
			return "{\"status\":\"TELNO_NOT_FOUND\"}";
		
		//임시 패스워드 생성	
		TempPasswordMaker pwdTemp = new TempPasswordMaker(); 
		String newPwdTemp = pwdTemp.tempPasswordMaker();
		member.setPassword(newPwdTemp);
		member.setLastpwdate(LocalDateTime.now());
		service.memberPasswordModify(member);

		return "{\"status\":\"GOOD\",\"password\":\"" + newPwdTemp + "\"}";
	}
	
	//로그인 시 패스워드 변경 여부 확인
	@GetMapping("/member/pwCheckNotice")
	public void getPWCheckNotice() throws Exception {
		
	}
	
	//주소 검색
	@GetMapping("/member/addrSearch")
	public void getAddrsearch(@RequestParam("addrSearch") String addrSearch, 
			@RequestParam("page") int pageNum, Model model) {
				
		int postNum = 5; //한 화면에 보여지는 게시물 행의 갯수
		int pageListCount = 5; //화면 하단에 보여지는 페이지리스트의 페이지 갯수		
		
		Page<AddressEntity> list = service.addrSearch(pageNum, postNum, addrSearch);
		
		int totalCount = (int)list.getTotalElements(); //전체 게시물 갯수
		
		PageUtil page = new PageUtil();
		model.addAttribute("list", service.addrSearch(pageNum, postNum, addrSearch));
		model.addAttribute("pageList", page.getPageAddress(pageNum, postNum, pageListCount, totalCount, addrSearch));
		
	}
	
	//before logout
	@GetMapping("/member/beforeLogout")
	public String getBeforeLogout(HttpSession session) {
		String email = (String)session.getAttribute("email");
		//String username = (String)session.getAttribute("username");
		
		System.out.println("session:email => " + email);
		
		MemberDTO member = new MemberDTO();
		member.setEmail(email);
		member.setLastlogoutdate(LocalDateTime.now());
		
		service.lastlogoutdateUpdate(member);
				
		return "redirect:/member/logout";
	}
	
	//invalidate
	@PostMapping("/member/invalidate")
	@ResponseBody
	public void getInvalidate(HttpSession session) {
		session.invalidate();
	}
	
}
