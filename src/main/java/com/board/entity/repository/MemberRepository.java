package com.board.entity.repository;

import java.util.Optional;
import java.util.OptionalLong;

import org.springframework.data.convert.Jsr310Converters.ZoneIdToStringConverter;
import org.springframework.data.jpa.repository.JpaRepository;

import com.board.entity.MemberEntity;

public interface MemberRepository extends JpaRepository<MemberEntity, String>{

	
	//select userid from tbl_member where username = #{username} and telno = #{telno}
	//아이디 찾기
	public Optional<MemberEntity> findByUsernameAndTelno(String member,String telno);

	
}
