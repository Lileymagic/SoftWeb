package com.example.softengineerwebpr.domain.comment.repository;

import com.example.softengineerwebpr.domain.comment.entity.Comment;
import com.example.softengineerwebpr.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * 특정 게시글(Post)에 속한 모든 최상위 댓글 (부모 댓글이 없는 댓글)을
     * 작성일자(createdAt) 기준 오름차순으로 조회합니다.
     * 대댓글은 Comment 엔티티 내 childComments 리스트를 통해 로드될 수 있습니다.
     *
     * @param post 댓글을 조회할 게시글 엔티티
     * @return 해당 게시글의 최상위 댓글 리스트
     */
    List<Comment> findByPostAndParentCommentIsNullOrderByCreatedAtAsc(Post post);

    /**
     * 특정 게시글 ID(post_idx)에 속한 모든 최상위 댓글을
     * 작성일자(createdAt) 기준 오름차순으로 조회합니다. (JPQL 예시)
     *
     * @param postId 댓글을 조회할 게시글의 ID
     * @return 해당 게시글의 최상위 댓글 리스트
     */
    @Query("SELECT c FROM Comment c WHERE c.post.idx = :postId AND c.parentComment IS NULL ORDER BY c.createdAt ASC")
    List<Comment> findByPostIdxAndParentCommentIsNullOrderByCreatedAtAsc(@Param("postId") Long postId);

    // 특정 게시글에 속한 모든 댓글(대댓글 포함)을 가져오고 싶다면 아래와 같이 할 수 있습니다.
    // List<Comment> findByPostOrderByCreatedAtAsc(Post post);
    // 이 경우 서비스 계층에서 계층 구조로 재구성해야 할 수 있습니다.
    // 현재 Comment 엔티티는 childComments를 LAZY 로딩하므로,
    // findByPostAndParentCommentIsNullOrderByCreatedAtAsc로 최상위 댓글을 가져온 후,
    // 필요에 따라 각 댓글의 자식 댓글에 접근하여 로드하는 방식이 일반적입니다.
}