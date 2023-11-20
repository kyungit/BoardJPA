package com.board.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.xml.crypto.dsig.XMLObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.board.dto.AddressDTO;
import com.board.dto.MemberDTO;
import com.board.entity.AddressEntity;
import com.board.entity.MemberEntity;
import com.board.entity.repository.AddressRepository;
import com.board.entity.repository.MemberRepository;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

	private final MemberRepository memberRepository;
	private final AddressRepository addressRepository;
	private final BCryptPasswordEncoder pwdEncoder;
	
	//회원 가입
	@Override
	public void memberInfoRegistry(MemberDTO member) {
		
		member.setRegdate(LocalDateTime.now());
		member.setLastpwdate(LocalDateTime.now());
		member.setPwcheck(1);
		member.setRole("USER");
		member.setFromSocial("N");
		
		memberRepository.save(member.dtoToEntity(member));
	}
	
	//회원 정보 가져 오기
	@Override
	public MemberDTO memberInfo(String email)  {
		return memberRepository.findById(email).map(member -> new MemberDTO(member)).get();
	}
	
	//아이디 중복 확인
	public int idCheck(String email) {
		return memberRepository.findById(email).isEmpty()?0:1;
	};
	
	//패스워드 수정
	public void memberPasswordModify(MemberDTO member) {
		MemberEntity memberEntity = memberRepository.findById(member.getEmail()).get();
		memberEntity.setPassword(pwdEncoder.encode(member.getPassword()));
		
		memberRepository.save(memberEntity);
	}
	
	//마지막 로그인 날짜 등록 하기
	@Override
	public void lastlogindateUpdate(MemberDTO member) {
		MemberEntity memberEntity = memberRepository.findById(member.getEmail()).get();
		memberEntity.setLastlogindate(LocalDateTime.now());
		
		memberRepository.save(memberEntity);
		
	}
	
	//마지막 로그아웃 날짜 등록 하기
	@Override
	public void lastlogoutdateUpdate(MemberDTO member) {
		MemberEntity memberEntity = memberRepository.findById(member.getEmail()).get();
		memberEntity.setLastlogoutdate(LocalDateTime.now());
		
		memberRepository.save(memberEntity);
	}
	
	//아이디 찾기
	public String searchID(MemberDTO member) {
//		MemberEntity memberEntity = memberRepository.findByUsernameContainingAndTelnoContaining(member.getUsername(), member.getTelno());
//		String email = memberEntity.getEmail();
		
		//에러나면 콘솔에 ID_NOT_FOUND나도록 설정
		String email = memberRepository.findByUsernameAndTelno(member.getUsername(), member.getTelno()).map(m->m.getEmail()).orElse("ID_NOT_FOUND");
		
		return email;
	}
	
	
	

	
	//주소 검색
	@Override
	public Page<AddressEntity> addrSearch(int pageNum, int postNum, String addrSearch) {
		PageRequest pageRequest = PageRequest.of(pageNum-1, postNum, Sort.by(Direction.ASC,"seqno"));
		
		
		return addressRepository.findByRoadContainingOrBuildingContaining(addrSearch, addrSearch,pageRequest);
		
	}



}
