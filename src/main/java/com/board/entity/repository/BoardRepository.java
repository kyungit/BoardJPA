package com.board.entity.repository;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.event.PublicInvocationEvent;

import com.board.entity.BoardEntity;

import jakarta.persistence.criteria.From;
import jakarta.transaction.Transactional;
import oracle.net.aso.b;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
	

/*		
		select seq, seqno, title, writer, regdate,hitno from 
			(select row_number() over (order by seqno desc) as seq,seqno, title,
			 writer, hitno,to_char(regdate, 'YYYY-MM-DD HH24:MI:SS') as regdate from tbl_board 
	         where title like '%'||#{keyword}||'%' or writer like '%'||#{keyword}||'%' or 
	         content like '%'||#{keyword}||'%' )
   		where seq between #{startPoint} and #{endPoint}	
*/
	
	//게시물 목록 보기
	public Page<BoardEntity> findByTitleContainingOrWriterContainingOrContentContaining
										(String keyword1, String keyword2,String keyword3, Pageable pageable);
	
	

	//게시물 이전 보기 -> JPQL 
	//String q 해서 q넣어주면 안됨!!!!!!1 꼭 안에다가 넣어줘야 함.
	@Query("select max(b.seqno) from board b where b.seqno < :seqno and (b.writer like %:keyword1% or b.title like %:keyword2% or b.content like %:keyword3%)")
	public Long pre_seqno(@Param("seqno") Long seqno, @Param("keyword1") String keyword1, @Param("keyword2") String keyword2, @Param("keyword3")String keyword3);
	
	
	
	//게시물 다음 보기 -> JPQL
	@Query("select min(b.seqno) From board b where b.seqno > :seqno and (b.writer like %:keyword1% or b.title like %:keyword2% or b.content like %:keyword3%)")
	public Long next_seqno(@Param("seqno") Long seqno, @Param("keyword1") String keyword1, @Param("keyword2") String keyword2, @Param("keyword3")String keyword3);
	
	//??jpql을 통해 dml 사용하게 되면 엔터티 안에만 반영되고 실제 물리적 테이블(tbl_board)에는 반영이 안되어 있음.
	//그래서 엔터티에 있는 내용을 실제 테이블에적용시키라는 명령,
	
	//게시물 조회수 증가 -> Native Query
	@Transactional
	@Modifying	//테이블의 DML(insert,update,delete,update)을 실행시켜 변화를 주었을 경우 테이블에 반영된 내용을 엔티티 클래스에 반영
	@Query(value="update tbl_board set hitno = (select nvl(hitno,0) from tbl_board where seqno = :seqno) + 1 where seqno = :seqno", nativeQuery = true)
	public void hitno(@Param("seqno") Long seqno);
	
	//게시물 시퀀스 번호 가져오기
	@Query(value="select tbl_board_seq.nextval from dual", nativeQuery=true)
	public Long getSeqnoWithNextval();
	
	
	
	
	
	
	
	
	
	
	
	
}
