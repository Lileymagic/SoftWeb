package com.example.softengineerwebpr.domain.comment.controller;

import com.example.softengineerwebpr.common.dto.ApiResponse;
import com.example.softengineerwebpr.common.util.AuthenticationUtil;
import com.example.softengineerwebpr.domain.comment.dto.CommentCreateRequestDto;
import com.example.softengineerwebpr.domain.comment.dto.CommentResponseDto;
import com.example.softengineerwebpr.domain.comment.dto.CommentUpdateRequestDto; // 방금 정의한 DTO
import com.example.softengineerwebpr.domain.comment.service.CommentService;
import com.example.softengineerwebpr.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api") // API 공통 기본 경로
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final AuthenticationUtil authenticationUtil;

    /**
     * 특정 게시글에 새 댓글 또는 대댓글을 작성합니다.
     *
     * @param postId      댓글을 작성할 게시글의 ID
     * @param requestDto  댓글 생성 요청 데이터 (내용, 부모 댓글 ID 등)
     * @param authentication 현재 사용자의 인증 정보
     * @return 생성된 댓글 정보
     */
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<CommentResponseDto>> createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentCreateRequestDto requestDto,
            Authentication authentication) {
        User currentUser = authenticationUtil.getCurrentUser(authentication);
        CommentResponseDto createdComment = commentService.createComment(postId, requestDto, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "댓글이 성공적으로 작성되었습니다.", createdComment));
    }

    /**
     * 특정 게시글의 모든 댓글 목록을 조회합니다. (계층형)
     *
     * @param postId      댓글 목록을 조회할 게시글의 ID
     * @param authentication 현재 사용자의 인증 정보
     * @return 해당 게시글의 댓글 목록
     */
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<List<CommentResponseDto>>> getCommentsByPost(
            @PathVariable Long postId,
            Authentication authentication) {
        User currentUser = authenticationUtil.getCurrentUser(authentication);
        List<CommentResponseDto> comments = commentService.getCommentsByPostId(postId, currentUser);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "게시글의 댓글 목록 조회 성공", comments));
    }

    /**
     * 특정 댓글의 내용을 수정합니다.
     *
     * @param commentId   수정할 댓글의 ID
     * @param requestDto  수정할 내용 (content)
     * @param authentication 현재 사용자의 인증 정보
     * @return 수정된 댓글 정보
     */
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponseDto>> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentUpdateRequestDto requestDto,
            Authentication authentication) {
        User currentUser = authenticationUtil.getCurrentUser(authentication);
        CommentResponseDto updatedComment = commentService.updateComment(commentId, requestDto.getContent(), currentUser);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "댓글이 성공적으로 수정되었습니다.", updatedComment));
    }

    /**
     * 특정 댓글을 삭제합니다.
     *
     * @param commentId   삭제할 댓글의 ID
     * @param authentication 현재 사용자의 인증 정보
     * @return 성공 응답 (내용 없음)
     */
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long commentId,
            Authentication authentication) {
        User currentUser = authenticationUtil.getCurrentUser(authentication);
        commentService.deleteComment(commentId, currentUser);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "댓글이 성공적으로 삭제되었습니다."));
    }
}