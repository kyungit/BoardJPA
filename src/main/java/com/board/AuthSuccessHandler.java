package com.board;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.board.entity.MemberEntity;
import com.board.entity.repository.MemberRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Component
@Log4j2
public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	private final MemberRepository memberRepository;

	//로그인 성공 시 해야 할 명령문
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		//authentication.getName() -> 로그인 시 입력된 email 값을 가져옴.
		MemberEntity member = memberRepository.findById(authentication.getName()).get();				
		//service.memberInfo(authentication.getName());
		
		//마지막 로그인 날짜 등록
		MemberEntity memberEntity = memberRepository.findById(authentication.getName()).get();
		memberEntity.setLastlogindate(LocalDateTime.now());
		
		memberRepository.save(memberEntity);
		
		
		//패스워드 확인 후 마지막 패스워드 변경일이 30일이 경과 되었을 경우
		
		HttpSession session = request.getSession();
		//세션 생성
		session.setMaxInactiveInterval(3600*24*7);//세션 유지 기간 설정
		session.setAttribute("email", member.getEmail());
		session.setAttribute("username",member.getUsername());
		session.setAttribute("role", member.getRole());
		session.setAttribute("fromSocial", member.getFromSocial());
		
		log.info("*********authsuccessHandler formlogin 성공");
		
		//성공시 가야 할 url
		setDefaultTargetUrl("/board/list?page=1");
		
		super.onAuthenticationSuccess(request, response, authentication);
		
	}
	
	
}
