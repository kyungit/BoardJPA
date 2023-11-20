package com.board.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.board.entity.BoardEntity;
import com.board.entity.LikeEntity;
import com.board.entity.MemberEntity;
import com.board.entity.ReplyEntity;

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
public class ReplyDTO {
	private Long replyseqno;
	private Long seqno;
	private String email;
	private String replywriter;
	private String replycontent;
	private LocalDateTime replyregdate;
	
	// Entity -> DTO로 이동
	public ReplyDTO(ReplyEntity replyEntity) {
		super();
		this.replyseqno = replyEntity.getReplyseqno();
		this.seqno = replyEntity.getSeqno().getSeqno();
		this.email = replyEntity.getEmail().getEmail();
		this.replywriter = replyEntity.getReplywriter();
		this.replycontent = replyEntity.getReplycontent();
		this.replyregdate = replyEntity.getReplyregdate();
	}
	

	
}
