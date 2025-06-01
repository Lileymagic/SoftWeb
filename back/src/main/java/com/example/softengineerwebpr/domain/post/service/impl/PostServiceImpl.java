package com.example.softengineerwebpr.domain.post.service.impl;

import com.example.softengineerwebpr.common.exception.BusinessLogicException;
import com.example.softengineerwebpr.common.exception.ErrorCode;
import com.example.softengineerwebpr.domain.post.dto.PostBasicResponseDto;
import com.example.softengineerwebpr.domain.post.dto.PostCreateRequestDto;
import com.example.softengineerwebpr.domain.post.dto.PostDetailResponseDto;
import com.example.softengineerwebpr.domain.post.dto.PostUpdateRequestDto;
import com.example.softengineerwebpr.domain.post.entity.Post;
import com.example.softengineerwebpr.domain.post.repository.PostRepository;
import com.example.softengineerwebpr.domain.post.service.PostService;
import com.example.softengineerwebpr.domain.project.entity.Project;
import com.example.softengineerwebpr.domain.project.entity.ProjectMemberRole;
import com.example.softengineerwebpr.domain.project.entity.ProjectMemberStatus;
import com.example.softengineerwebpr.domain.project.repository.ProjectMemberRepository;
import com.example.softengineerwebpr.domain.task.entity.Task;
import com.example.softengineerwebpr.domain.task.repository.TaskRepository;
import com.example.softengineerwebpr.domain.user.entity.User;
import com.example.softengineerwebpr.domain.file.entity.File; // File 엔티티 임포트
import com.example.softengineerwebpr.domain.file.entity.FileReferenceType; // FileReferenceType Enum 임포트
import com.example.softengineerwebpr.domain.file.repository.FileRepository; // FileRepository 임포트
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    private final FileRepository fileRepository; // FileRepository 인스턴스 주입
    private final PostRepository postRepository;
    private final TaskRepository taskRepository; // Task 엔티티 조회를 위해 필요
    private final ProjectMemberRepository projectMemberRepository; // 프로젝트 멤버십 확인을 위해 필요

    private Task findTaskOrThrow(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.TASK_NOT_FOUND));
    }

    private void checkProjectMembership(Project project, User user) {
        if (!projectMemberRepository.findByUserAndProject(user, project)
                .filter(pm -> pm.getStatus() == ProjectMemberStatus.ACCEPTED)
                .isPresent()) {
            throw new BusinessLogicException(ErrorCode.NOT_PROJECT_MEMBER, "해당 프로젝트의 멤버가 아니므로 게시글을 조회할 수 없습니다.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostBasicResponseDto> getPostsByTaskId(Long taskId, User currentUser) {
        Task task = findTaskOrThrow(taskId);
        Project project = task.getProject(); // Task 엔티티에서 Project 정보를 가져옴

        // 현재 사용자가 해당 업무가 속한 프로젝트의 멤버인지 확인
        checkProjectMembership(project, currentUser);

        List<Post> posts = postRepository.findByTaskOrderByCreatedAtDesc(task);

        return posts.stream()
                .map(PostBasicResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    private Post findPostOrThrow(Long postId) { // 이 메소드가 이미 있다면 재사용, 없다면 추가
        return postRepository.findById(postId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.RESOURCE_NOT_FOUND, "게시글을 찾을 수 없습니다. ID: " + postId));
    }

    // 게시글을 볼 수 있는 권한이 있는지 확인하는 메소드 (예: 프로젝트 멤버인지)
    private void checkPostViewPermission(Post post, User user) {
        Project project = post.getTask().getProject(); // 게시글 -> 업무 -> 프로젝트
        if (!projectMemberRepository.findByUserAndProject(user, project)
                .filter(pm -> pm.getStatus() == ProjectMemberStatus.ACCEPTED) // 정식 멤버인지 확인
                .isPresent()) {
            throw new BusinessLogicException(ErrorCode.NOT_PROJECT_MEMBER, "해당 프로젝트의 멤버가 아니므로 게시글을 조회할 수 없습니다.");
        }
    }


    @Override
    @Transactional(readOnly = true)
    public PostDetailResponseDto getPostDetails(Long postId, User currentUser) {
        Post post = findPostOrThrow(postId);
        checkPostViewPermission(post, currentUser);

        // 주입된 fileRepository 인스턴스를 통해 메소드 호출
        List<File> postFiles = fileRepository.findByReferenceTypeAndReferenceIdxOrderByUploadedAtAsc(
                FileReferenceType.POST,
                postId
        );

        log.info("게시글 상세 조회: postId={}, 첨부파일 {}개, 조회자: {}", postId, postFiles.size(), currentUser.getNickname());

        return PostDetailResponseDto.fromEntity(post, postFiles);
    }

    @Override
    public PostDetailResponseDto createPost(Long taskId, PostCreateRequestDto requestDto, User currentUser) {
        Task task = findTaskOrThrow(taskId); // 기존 헬퍼 메소드 사용
        Project project = task.getProject();

        // 권한 검사: 현재 사용자가 해당 업무가 속한 프로젝트의 멤버인지 확인
        // 설계 문서에 "업무 담당 유저들만 쓰기 가능" 조건이 있으므로[cite: 126],
        // 추가적으로 현재 유저가 해당 업무(task)의 담당자인지 확인하는 로직이 필요할 수 있습니다.
        // 여기서는 일단 프로젝트 멤버십만 확인합니다.
        checkProjectMembership(project, currentUser); // 기존 헬퍼 메소드 (post 조회 시 사용했던 것과 유사)

        Post post = Post.builder()
                .task(task)
                .user(currentUser)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .build();

        Post savedPost = postRepository.save(post);
        log.info("새 게시글 생성: postId={}, title='{}', taskId={}, byUser={}",
                savedPost.getIdx(), savedPost.getTitle(), taskId, currentUser.getNickname());

        // TODO: 첨부파일 처리 로직 (requestDto에 fileIds가 있다면)
        // TODO: 히스토리 기록 (예: "게시글 생성")
        // TODO: 알림 발생 (예: 업무 담당자들에게 새 게시글 알림)

        // 생성된 게시글의 상세 정보를 반환 (첨부파일은 현재 빈 리스트로 반환됨)
        return PostDetailResponseDto.fromEntity(savedPost , Collections.emptyList() );
    }

    @Override
    public void deletePost(Long postId, User currentUser) {
        Post post = findPostOrThrow(postId); // 기존 _헬퍼_메소드 사용
        Project project = post.getTask().getProject();

        // 게시글 삭제 권한 확인 (예: 게시글 작성자 본인 또는 프로젝트 관리자)
        boolean isAuthor = post.getUser().getIdx().equals(currentUser.getIdx());
        boolean isAdmin = projectMemberRepository.findByUserAndProject(currentUser, project)
                .filter(pm -> pm.getStatus() == ProjectMemberStatus.ACCEPTED)
                .map(pm -> pm.getRole() == ProjectMemberRole.관리자)
                .orElse(false);

        if (!isAuthor && !isAdmin) {
            // ErrorCode.NO_AUTHORITY_TO_MANAGE_POST 와 같은 더 구체적인 에러 코드 정의 및 사용 권장
            throw new BusinessLogicException(ErrorCode.ACCESS_DENIED, "게시글을 삭제할 권한이 없습니다.");
        }

        // Post 엔티티에 댓글(comments)과 파일(files)에 대한 연관관계가
        // cascade = CascadeType.ALL, orphanRemoval = true 로 설정되어 있다면,
        // postRepository.delete(post) 호출 시 관련 댓글과 파일도 함께 삭제됩니다.
        //
        // 그렇지 않다면, 여기서 commentRepository.deleteByPost(post) 와
        // fileRepository.deleteByReferenceTypeAndReferenceIdx(FileReferenceType.POST, postId) 등을
        // 명시적으로 호출해야 합니다.

        postRepository.delete(post);
        log.info("게시글 삭제 완료: postId={}, 삭제자: {}", postId, currentUser.getNickname());

        // TODO: 히스토리 기록 (예: "게시글 삭제")
        // TODO: 관련자에게 알림 (필요시)
    }

    @Override
    public PostDetailResponseDto updatePost(Long postId, PostUpdateRequestDto requestDto, User currentUser) {
        Post post = findPostOrThrow(postId); // 기존 헬퍼 메소드 사용

        // 게시글 수정 권한 확인 (예: 작성자 본인 또는 프로젝트 관리자)
        checkPostViewPermission(post, currentUser); // 일단 게시글을 볼 수 있는 권한이 있는지 확인 (기본)

        boolean isAuthor = post.getUser().getIdx().equals(currentUser.getIdx());
        // boolean isAdmin = isUserProjectAdmin(currentUser, post.getTask().getProject()); // 필요시 프로젝트 관리자 권한 추가

        if (!isAuthor /* && !isAdmin */) {
            // ErrorCode.NO_AUTHORITY_TO_MANAGE_POST 와 같은 더 구체적인 에러 코드 사용 권장
            throw new BusinessLogicException(ErrorCode.ACCESS_DENIED, "게시글을 수정할 권한이 없습니다.");
        }

        // Post 엔티티의 update 메소드 활용
        post.update(requestDto.getTitle(), requestDto.getContent());
        Post updatedPost = postRepository.save(post); // 변경 감지로 저장될 수도 있지만 명시적 save

        log.info("게시글 수정 완료: postId={}, newTitle='{}', byUser={}",
                updatedPost.getIdx(), updatedPost.getTitle(), currentUser.getNickname());

        // TODO: 첨부파일 변경 로직 (새 파일 추가, 기존 파일 삭제 등)
        // TODO: 히스토리 기록 (예: "게시글 수정")
        // TODO: 알림 발생 (예: 게시글 구독자에게 수정 알림)

        // 수정된 게시글의 상세 정보를 반환 (첨부파일은 현재 DTO에서 로드하는 방식 그대로)
        List<File> postFiles = fileRepository.findByReferenceTypeAndReferenceIdxOrderByUploadedAtAsc(
                FileReferenceType.POST,
                updatedPost.getIdx()
        );
        return PostDetailResponseDto.fromEntity(updatedPost, postFiles);
    }
    // TODO: getPostById, updatePost 메소드 구현
}