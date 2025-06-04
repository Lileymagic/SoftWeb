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
import com.example.softengineerwebpr.domain.file.dto.FileResponseDto; //
import com.example.softengineerwebpr.domain.file.entity.FileReferenceType; //
import com.example.softengineerwebpr.domain.file.service.FileService; // FileService 주입
import com.example.softengineerwebpr.domain.user.dto.UserBasicInfoDto; //
import java.util.Collections; // 필요시
import java.util.List; // 필요시
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
    private final FileService fileService;       // 새로 주입

    private Task findTaskOrThrow(Long taskId) { //
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.TASK_NOT_FOUND));
    }

    private void checkProjectMembership(Project project, User user) { //
        if (!projectMemberRepository.findByUserAndProject(user, project)
                .filter(pm -> pm.getStatus() == ProjectMemberStatus.ACCEPTED)
                .isPresent()) {
            throw new BusinessLogicException(ErrorCode.NOT_PROJECT_MEMBER, "해당 프로젝트의 멤버가 아니므로 게시글을 조회할 수 없습니다.");
        }
    }

    private Post findPostOrThrow(Long postId) { //
        return postRepository.findById(postId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.RESOURCE_NOT_FOUND, "게시글을 찾을 수 없습니다. ID: " + postId));
    }

    private void checkPostViewPermission(Post post, User user) { //
        Project project = post.getTask().getProject();
        if (!projectMemberRepository.findByUserAndProject(user, project)
                .filter(pm -> pm.getStatus() == ProjectMemberStatus.ACCEPTED)
                .isPresent()) {
            throw new BusinessLogicException(ErrorCode.NOT_PROJECT_MEMBER, "해당 프로젝트의 멤버가 아니므로 게시글을 조회할 수 없습니다.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PostDetailResponseDto getPostDetails(Long postId, User currentUser) {
        Post post = findPostOrThrow(postId);
        checkPostViewPermission(post, currentUser);

        List<FileResponseDto> fileDtos = fileService.getFilesForReference(FileReferenceType.POST, postId); // 파일 목록 조회

        log.info("게시글 상세 조회: postId={}, 첨부파일 {}개, 조회자: {}", postId, fileDtos.size(), currentUser.getNickname());

        // PostDetailResponseDto 빌더 직접 사용
        return PostDetailResponseDto.builder()
                .idx(post.getIdx())
                .title(post.getTitle())
                .content(post.getContent())
                .author(UserBasicInfoDto.fromEntity(post.getUser())) //
                .taskId(post.getTask().getIdx())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .files(fileDtos) // 조회된 파일 DTO 목록 설정
                .build(); //
    }

    @Override
    public PostDetailResponseDto createPost(Long taskId, PostCreateRequestDto requestDto, User currentUser) {
        Task task = findTaskOrThrow(taskId);
        Project project = task.getProject();
        checkProjectMembership(project, currentUser);

        Post post = Post.builder()
                .task(task)
                .user(currentUser)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .build(); //

        Post savedPost = postRepository.save(post);
        log.info("새 게시글 생성: postId={}, title='{}', taskId={}, byUser={}",
                savedPost.getIdx(), savedPost.getTitle(), taskId, currentUser.getNickname());

        // 파일은 별도 API(/api/files/upload)를 통해 이 savedPost.getIdx()를 referenceIdx로 하여 업로드됨.
        // 따라서 이 메소드에서는 직접적인 파일 처리 로직은 없음 (흐름 가정에 따라).
        // 생성 시점에는 첨부 파일이 없으므로 빈 리스트로 DTO 생성.
        return PostDetailResponseDto.builder()
                .idx(savedPost.getIdx())
                .title(savedPost.getTitle())
                .content(savedPost.getContent())
                .author(UserBasicInfoDto.fromEntity(savedPost.getUser())) //
                .taskId(savedPost.getTask().getIdx())
                .createdAt(savedPost.getCreatedAt())
                .updatedAt(savedPost.getUpdatedAt())
                .files(Collections.emptyList()) // 생성 시점에는 파일 없음
                .build(); //
    }

    @Override
    public PostDetailResponseDto updatePost(Long postId, PostUpdateRequestDto requestDto, User currentUser) {
        Post post = findPostOrThrow(postId);
        checkPostViewPermission(post, currentUser);

        boolean isAuthor = post.getUser().getIdx().equals(currentUser.getIdx());
        if (!isAuthor) {
            throw new BusinessLogicException(ErrorCode.ACCESS_DENIED, "게시글을 수정할 권한이 없습니다.");
        }

        post.update(requestDto.getTitle(), requestDto.getContent()); //
        Post updatedPost = postRepository.save(post);
        log.info("게시글 수정 완료: postId={}, newTitle='{}', byUser={}",
                updatedPost.getIdx(), updatedPost.getTitle(), currentUser.getNickname());

        // 파일 수정은 보통 기존 파일 삭제 후 새 파일 업로드로 처리되거나, 파일 추가/삭제 API를 별도로 호출.
        // 이 메소드에서는 게시글 내용만 수정하고, 파일 목록은 현재 상태 그대로 가져옴.
        List<FileResponseDto> fileDtos = fileService.getFilesForReference(FileReferenceType.POST, updatedPost.getIdx());
        return PostDetailResponseDto.builder()
                .idx(updatedPost.getIdx())
                .title(updatedPost.getTitle())
                .content(updatedPost.getContent())
                .author(UserBasicInfoDto.fromEntity(updatedPost.getUser())) //
                .taskId(updatedPost.getTask().getIdx())
                .createdAt(updatedPost.getCreatedAt())
                .updatedAt(updatedPost.getUpdatedAt())
                .files(fileDtos)
                .build(); //
    }

    @Override
    public void deletePost(Long postId, User currentUser) {
        Post post = findPostOrThrow(postId);
        Project project = post.getTask().getProject();

        boolean isAuthor = post.getUser().getIdx().equals(currentUser.getIdx());
        boolean isAdmin = projectMemberRepository.findByUserAndProject(currentUser, project) //
                .filter(pm -> pm.getStatus() == ProjectMemberStatus.ACCEPTED)
                .map(pm -> pm.getRole() == ProjectMemberRole.관리자) //
                .orElse(false);

        if (!isAuthor && !isAdmin) {
            throw new BusinessLogicException(ErrorCode.ACCESS_DENIED, "게시글을 삭제할 권한이 없습니다.");
        }

        // 1. 게시글에 연결된 댓글에 첨부된 파일들 삭제 (CommentService에서 처리하도록 위임하거나 직접 처리)
        //    (CommentService의 deleteCommentsForPost와 같은 메소드가 있다면 그 안에서 파일삭제까지 담당할 수 있음)
        //    여기서는 Post 삭제 시 Comment도 DB Cascade로 삭제된다고 가정하고,
        //    Comment에 연결된 파일은 Comment 삭제 로직에서 처리한다고 가정합니다.
        //    만약 Comment 삭제 시 파일 처리가 안된다면 여기서 Comment의 파일도 지워야 합니다.

        // 2. 게시글에 직접 첨부된 파일들 삭제
        fileService.deleteFilesForReference(FileReferenceType.POST, postId, currentUser);

        // 3. 게시글 삭제 (게시글 삭제 시 DB 설정에 따라 관련 댓글도 Cascade 삭제될 수 있음)
        //    Comment 테이블의 post_idx 외래키에 ON DELETE CASCADE가 설정되어 있다면 댓글은 자동 삭제.
        //    (DB 생성 코드 확인 결과: comment 테이블의 fk_comment_post에 ON DELETE CASCADE 있음)
        postRepository.delete(post);
        log.info("게시글 삭제 완료: postId={}, 삭제자: {}", postId, currentUser.getNickname());
    }

    // getPostsByTaskId는 PostBasicResponseDto를 반환하므로 파일 정보 불필요
    @Override
    @Transactional(readOnly = true)
    public List<PostBasicResponseDto> getPostsByTaskId(Long taskId, User currentUser) { //
        Task task = findTaskOrThrow(taskId);
        Project project = task.getProject();
        checkProjectMembership(project, currentUser);
        List<Post> posts = postRepository.findByTaskOrderByCreatedAtDesc(task); //
        return posts.stream()
                .map(PostBasicResponseDto::fromEntity) //
                .collect(Collectors.toList());
    }
}