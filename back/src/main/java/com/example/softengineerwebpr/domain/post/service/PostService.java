package com.example.softengineerwebpr.domain.post.service;

import com.example.softengineerwebpr.domain.post.dto.PostBasicResponseDto;
import com.example.softengineerwebpr.domain.user.entity.User;

import java.util.List;

public interface PostService {

    /**
     * 특정 업무(Task)에 속한 모든 게시글 목록을 조회합니다.
     * (목록에는 기본적인 정보만 포함: ID, 제목, 작성자 닉네임, 생성일)
     *
     * @param taskId      게시글 목록을 조회할 업무의 ID
     * @param currentUser 현재 작업을 수행하는 사용자 (업무 접근 권한 검사)
     * @return 해당 업무의 게시글 목록 (PostBasicResponseDto 리스트)
     */
    List<PostBasicResponseDto> getPostsByTaskId(Long taskId, User currentUser);

    // 여기에 추후 게시글 생성, 상세 조회, 수정, 삭제 등의 메소드 시그니처 추가 예정
    // 예: PostDetailResponseDto createPost(Long taskId, PostCreateRequestDto requestDto, User currentUser);
    // 예: PostDetailResponseDto getPostById(Long postId, User currentUser);
    // 예: PostDetailResponseDto updatePost(Long postId, PostUpdateRequestDto requestDto, User currentUser);
    // 예: void deletePost(Long postId, User currentUser);
}