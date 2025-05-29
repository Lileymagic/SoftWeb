package com.example.softengineerwebpr.domain.post.controller;

import com.example.softengineerwebpr.common.dto.ApiResponse;
import com.example.softengineerwebpr.common.util.AuthenticationUtil; // 이전 단계에서 생성한 AuthenticationUtil
import com.example.softengineerwebpr.domain.post.dto.PostBasicResponseDto;
import com.example.softengineerwebpr.domain.post.service.PostService;
import com.example.softengineerwebpr.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api") // API 공통 기본 경로
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final AuthenticationUtil authenticationUtil;

    /**
     * 특정 업무(Task)에 속한 모든 게시글 목록을 조회합니다.
     *
     * @param taskId      게시글 목록을 조회할 업무의 ID
     * @param authentication 현재 사용자의 인증 정보
     * @return 해당 업무의 게시글 목록 (PostBasicResponseDto 리스트)
     */
    @GetMapping("/tasks/{taskId}/posts")
    public ResponseEntity<ApiResponse<List<PostBasicResponseDto>>> getPostsByTask(
            @PathVariable Long taskId,
            Authentication authentication) {

        User currentUser = authenticationUtil.getCurrentUser(authentication); // 현재 사용자 정보 가져오기
        List<PostBasicResponseDto> posts = postService.getPostsByTaskId(taskId, currentUser);

        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "업무 관련 게시글 목록 조회 성공", posts));
    }

    // TODO: 여기에 추후 게시글 생성(POST /tasks/{taskId}/posts),
    //       게시글 상세 조회(GET /posts/{postId}),
    //       게시글 수정(PUT /posts/{postId}),
    //       게시글 삭제(DELETE /posts/{postId}) 등의 엔드포인트 추가 예정
}