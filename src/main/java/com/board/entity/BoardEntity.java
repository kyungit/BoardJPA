package com.board.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name="board")
@Table(name="tbl_board")
public class BoardEntity {
	
	@Id
	@Column(name="seqno", nullable=false)
	private Long seqno;
	
	@Column(name="writer",length=50, nullable = false)	
	private String writer;
	
	@Column(name="title",length=200, nullable = false)
	private String title;
	
	@Column(name="content",length=2000, nullable = false)
	private String content;
	
	@Column(name="regdate", nullable = false)
	private LocalDateTime regdate;
	
	@Column(name="hitno", nullable = true)
	private int hitno;
	
	@Column(name="likecnt", nullable = true)
	private int likecnt;
	
	@Column(name="dislikecnt", nullable = true)
	private int dislikecnt;
	
	//FK 만들기
	//FK 읽어 올 때 Eager, Lazy 두가지 타입이 있음.
	//Eager : 부모키가 있는 테이블부터 검사해서 부모키가 제대로 되어있는지 확인하고 자식 키를 읽음. -> 정확도는 높지만 성능 저하
	//Lazy : 자식 키가 있는 테이블만 읽음 -> 정확도는 떨어지지만 성능이 향상
	//fetch->db에서 값읽어오는거??
	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action =OnDeleteAction.CASCADE)
	@JoinColumn(name = "email", nullable = false)
	private MemberEntity email;
	

}
