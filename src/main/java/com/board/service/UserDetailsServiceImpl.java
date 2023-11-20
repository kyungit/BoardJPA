package com.board.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.board.dto.MemberDTO;
import com.board.entity.MemberEntity;
import com.board.entity.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldNameConstants;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService{

	//private final MemberService service;
	private final MemberRepository memberRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		//username은 스프링 시큐리티가 필터로 작동하면서 로그인 요청에서 가로채 온 userid임...
		Optional<MemberEntity> optionalMember = memberRepository.findById(username);
		MemberEntity memberInfo = optionalMember.orElseThrow(() -> new UsernameNotFoundException("아이디가 존재하지 않습니다."));
		
		
		if(memberInfo == null) {
			throw new UsernameNotFoundException("아이디가 존재하지 않습니다.");
		}
		//SimpleGrantedAuthority : 여러 개의 사용자 Role값을 받는 객체
		List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();	//그냥 list..
		SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(memberInfo.getRole());	//list 타입
		//Role = ["admin","master","dba"]
		grantedAuthorities.add(grantedAuthority);
		
		
		
		//사용자 인증 관련된 최상위 객체
		User user = new User(username, memberInfo.getPassword(), grantedAuthorities);
		
		return user;
	}

}
