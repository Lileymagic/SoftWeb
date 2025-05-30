package com.example.softengineerwebpr.domain.post.controller;

import com.example.softengineerwebpr.common.dto.ApiResponse;
import com.example.softengineerwebpr.common.util.AuthenticationUtil;
import com.example.softengineerwebpr.domain.post.dto.PostBasicResponseDto;
import com.example.softengineerwebpr.domain.post.dto.PostDetailResponseDto; // PostDetailResponseDto 임포트
import com.example.softengineerwebpr.domain.post.service.PostService;
import com.example.softengineerwebpr.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.example.softengineerwebpr.domain.post.dto.PostCreateRequestDto; // 추가
import jakarta.validation.Valid; // 추가

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final AuthenticationUtil authenticationUtil;

    /**
     * 특정 업무(Task)에 속한 모든 게시글 목록을 조회합니다.
     */
    @GetMapping("/tasks/{taskId}/posts")
    public ResponseEntity<ApiResponse<List<PostBasicResponseDto>>> getPostsByTask(
            @PathVariable Long taskId,
            Authentication authentication) {

        User currentUser = authenticationUtil.getCurrentUser(authentication);
        List<PostBasicResponseDto> posts = postService.getPostsByTaskId(taskId, currentUser);

        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "업무 관련 게시글 목록 조회 성공", posts));
    }

    /**
     * 특정 게시글의 상세 정보를 ID로 조회합니다.
     *
     * @param postId      상세 정보를 조회할 게시글의 ID
     * @param authentication 현재 사용자의 인증 정보
     * @return 게시글 상세 정보 (PostDetailResponseDto)
     */
    @GetMapping("/posts/{postId}") // 새로 추가된 엔드포인트
    public ResponseEntity<ApiResponse<PostDetailResponseDto>> getPostDetailsById(
            @PathVariable Long postId,
            Authentication authentication) {

        User currentUser = authenticationUtil.getCurrentUser(authentication);
        PostDetailResponseDto postDetails = postService.getPostDetails(postId, currentUser);

        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "게시글 상세 정보 조회 성공", postDetails));
    }

    /**
     * 특정 업무(Task)에 새 게시글을 작성합니다.
     *
     * @param taskId      게시글이 속할 업무의 ID
     * @param requestDto  게시글 생성 요청 데이터 (제목, 내용)
     * @param authentication 현재 사용자의 인증 정보
     * @return 생성된 게시글의 상세 정보
     */
    @PostMapping("/tasks/{taskId}/posts")
    public ResponseEntity<ApiResponse<PostDetailResponseDto>> createPost(
            @PathVariable Long taskId,
            @Valid @RequestBody PostCreateRequestDto requestDto,
            Authentication authentication) {

        User currentUser = authenticationUtil.getCurrentUser(authentication);
        PostDetailResponseDto createdPost = postService.createPost(taskId, requestDto, currentUser);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "게시글이 성공적으로 작성되었습니다.", createdPost));
    }
    // TODO: 여기에 추후 게시글 생성(POST /tasks/{taskId}/posts 또는 POST /api/posts),
    //       게시글 수정(PUT /api/posts/{postId}),
    //       게시글 삭제(DELETE /api/posts/{postId}) 등의 엔드포인트 추가 예정
}