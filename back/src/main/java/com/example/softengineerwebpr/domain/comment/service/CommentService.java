package com.example.softengineerwebpr.domain.comment.service;

import com.example.softengineerwebpr.domain.comment.dto.CommentCreateRequestDto;
import com.example.softengineerwebpr.domain.comment.dto.CommentResponseDto;
import com.example.softengineerwebpr.domain.user.entity.User;

import java.util.List;

public interface CommentService {

    /**
     * 특정 게시글에 새 댓글을 작성합니다.
     *
     * @param postId      댓글을 작성할 게시글의 ID
     * @param requestDto  댓글 생성 요청 데이터 (내용, 부모 댓글 ID 등)
     * @param currentUser 현재 작업을 수행하는 사용자 (댓글 작성자)
     * @return 생성된 댓글 정보를 담은 CommentResponseDto
     */
    CommentResponseDto createComment(Long postId, CommentCreateRequestDto requestDto, User currentUser);

    /**
     * 특정 게시글에 속한 모든 댓글 목록을 조회합니다. (계층형 구조)
     *
     * @param postId      댓글 목록을 조회할 게시글의 ID
     * @param currentUser 현재 작업을 수행하는 사용자 (게시글 접근 권한 검사)
     * @return 해당 게시글의 댓글 목록 (최상위 댓글 및 대댓글 포함, CommentResponseDto 리스트)
     */
    List<CommentResponseDto> getCommentsByPostId(Long postId, User currentUser);

    /**
     * 특정 댓글을 수정합니다.
     *
     * @param commentId   수정할 댓글의 ID
     * @param content     새로운 댓글 내용
     * @param currentUser 현재 작업을 수행하는 사용자 (댓글 수정 권한 검사)
     * @return 수정된 댓글 정보를 담은 CommentResponseDto
     */
    CommentResponseDto updateComment(Long commentId, String content, User currentUser);

    /**
     * 특정 댓글을 삭제합니다.
     *
     * @param commentId   삭제할 댓글의 ID
     * @param currentUser 현재 작업을 수행하는 사용자 (댓글 삭제 권한 검사)
     */
    void deleteComment(Long commentId, User currentUser);

}