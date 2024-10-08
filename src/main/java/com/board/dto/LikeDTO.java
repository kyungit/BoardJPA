package com.board.dto;


import com.board.entity.BoardEntity;
import com.board.entity.LikeEntity;
import com.board.entity.MemberEntity;

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
public class LikeDTO {
	private BoardEntity seqno;
	private MemberEntity email;
	private String mylikecheck;
	private String mydislikecheck;
	private String likedate;
	private String dislikedate;
	
	
	public LikeDTO(LikeEntity likeEntity) {
		
		this.seqno = likeEntity.getSeqno();
		this.email = likeEntity.getEmail();
		this.mylikecheck = likeEntity.getMylikecheck();
		this.mydislikecheck = likeEntity.getMydislikecheck();
		this.likedate = likeEntity.getLikedate();
		this.dislikedate = likeEntity.getDislikedate();
	}
	
	//DTO -> Entity 이동
	public LikeEntity dtoToEntity(LikeDTO dto) {
		LikeEntity likeEntity =LikeEntity.builder()
											.seqno(dto.getSeqno())
											.email(dto.getEmail())
											.mylikecheck(dto.getMylikecheck())
											.mydislikecheck(dto.getMydislikecheck())
											.likedate(dto.getLikedate())
											.dislikedate(dto.getDislikedate())
											.build();
			
		return likeEntity;
	}
	

}
