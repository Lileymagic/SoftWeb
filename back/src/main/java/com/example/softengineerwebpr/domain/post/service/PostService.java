package com.example.softengineerwebpr.domain.post.service;

import com.example.softengineerwebpr.domain.post.dto.PostBasicResponseDto;
import com.example.softengineerwebpr.domain.post.dto.PostCreateRequestDto;
import com.example.softengineerwebpr.domain.post.dto.PostDetailResponseDto;
import com.example.softengineerwebpr.domain.post.dto.PostUpdateRequestDto;
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

    // ... 기존 getPostsByTaskId 메소드 등 ...

    /**
     * 특정 게시글의 상세 정보를 조회합니다.
     * (제목, 본문 내용, 작성자, 작성일, 수정일, 첨부파일 목록 등)
     *
     * @param postId      상세 정보를 조회할 게시글의 ID
     * @param currentUser 현재 작업을 수행하는 사용자 (게시글 접근 권한 검사)
     * @return 게시글 상세 정보를 담은 PostDetailResponseDto
     */
    PostDetailResponseDto getPostDetails(Long postId, User currentUser);

    /**
     * 특정 업무(Task)에 새로운 게시글을 생성합니다.
     *
     * @param taskId      게시글이 속할 업무의 ID
     * @param requestDto  게시글 생성 요청 데이터 (제목, 내용 등)
     * @param currentUser 현재 작업을 수행하는 사용자 (게시글 작성자)
     * @return 생성된 게시글의 상세 정보를 담은 PostDetailResponseDto
     */
    PostDetailResponseDto createPost(Long taskId, PostCreateRequestDto requestDto, User currentUser);

    // PostService.java 에 추가
    /**
     * 특정 게시글을 삭제합니다.
     * (게시글에 속한 댓글, 첨부파일 등도 함께 삭제 처리 필요 - DB cascade 또는 서비스 로직)
     *
     * @param postId      삭제할 게시글의 ID
     * @param currentUser 현재 작업을 수행하는 사용자 (게시글 삭제 권한 검사)
     */
    void deletePost(Long postId, User currentUser);

    /**
     * 특정 게시글의 내용을 수정합니다.
     *
     * @param postId      수정할 게시글의 ID
     * @param requestDto  수정할 게시글 정보 (제목, 내용 등)
     * @param currentUser 현재 작업을 수행하는 사용자 (게시글 수정 권한 검사)
     * @return 수정된 게시글의 상세 정보를 담은 PostDetailResponseDto
     */
    PostDetailResponseDto updatePost(Long postId, PostUpdateRequestDto requestDto, User currentUser);
}