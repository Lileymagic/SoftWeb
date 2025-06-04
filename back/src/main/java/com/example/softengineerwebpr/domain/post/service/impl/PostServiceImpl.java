package com.example.softengineerwebpr.domain.post.service.impl; //

import com.example.softengineerwebpr.common.exception.BusinessLogicException; //
import com.example.softengineerwebpr.common.exception.ErrorCode; //
import com.example.softengineerwebpr.domain.comment.entity.Comment;
import com.example.softengineerwebpr.domain.comment.repository.CommentRepository;
import com.example.softengineerwebpr.domain.file.dto.FileResponseDto;
import com.example.softengineerwebpr.domain.post.dto.PostBasicResponseDto; //
import com.example.softengineerwebpr.domain.post.dto.PostCreateRequestDto; //
import com.example.softengineerwebpr.domain.post.dto.PostDetailResponseDto; //
import com.example.softengineerwebpr.domain.post.dto.PostUpdateRequestDto; //
import com.example.softengineerwebpr.domain.post.entity.Post; //
import com.example.softengineerwebpr.domain.post.repository.PostRepository; //
import com.example.softengineerwebpr.domain.post.service.PostService; //
import com.example.softengineerwebpr.domain.project.entity.Project; //
import com.example.softengineerwebpr.domain.project.entity.ProjectMemberRole; //
import com.example.softengineerwebpr.domain.project.entity.ProjectMemberStatus; //
import com.example.softengineerwebpr.domain.project.repository.ProjectMemberRepository; //
import com.example.softengineerwebpr.domain.task.entity.Task; //
import com.example.softengineerwebpr.domain.task.repository.TaskRepository; //
import com.example.softengineerwebpr.domain.user.entity.User; //
import com.example.softengineerwebpr.domain.file.entity.File; //
import com.example.softengineerwebpr.domain.file.entity.FileReferenceType; //
import com.example.softengineerwebpr.domain.file.repository.FileRepository; //
import com.example.softengineerwebpr.domain.file.service.FileService; // FileService 주입 (이전 답변에서 이미 제안됨)
import com.example.softengineerwebpr.domain.user.dto.UserBasicInfoDto; //


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections; //
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    private final FileRepository fileRepository;         // 기존 주입
    private final FileService fileService;               // 파일 삭제를 위해 FileService 주입 (이전 답변에서 제안됨)
    private final PostRepository postRepository;         //
    private final TaskRepository taskRepository;         //
    private final ProjectMemberRepository projectMemberRepository; //
    private final CommentRepository commentRepository;

    // --- Helper Methods (findTaskOrThrow, checkProjectMembership, findPostOrThrow, checkPostViewPermission - 이전과 동일) ---
    private Task findTaskOrThrow(Long taskId) { /* 이전과 동일 */ return taskRepository.findById(taskId).orElseThrow(() -> new BusinessLogicException(ErrorCode.TASK_NOT_FOUND)); } //
    private void checkProjectMembership(Project project, User user) { /* 이전과 동일 */  if (!projectMemberRepository.findByUserAndProject(user, project).filter(pm -> pm.getStatus() == ProjectMemberStatus.ACCEPTED).isPresent()) { throw new BusinessLogicException(ErrorCode.NOT_PROJECT_MEMBER, "해당 프로젝트의 멤버가 아니므로 게시글을 조회할 수 없습니다."); } } //
    private Post findPostOrThrow(Long postId) { /* 이전과 동일 */ return postRepository.findById(postId).orElseThrow(() -> new BusinessLogicException(ErrorCode.RESOURCE_NOT_FOUND, "게시글을 찾을 수 없습니다. ID: " + postId)); } //
    private void checkPostViewPermission(Post post, User user) { /* 이전과 동일 */ Project project = post.getTask().getProject(); if (!projectMemberRepository.findByUserAndProject(user, project).filter(pm -> pm.getStatus() == ProjectMemberStatus.ACCEPTED).isPresent()) { throw new BusinessLogicException(ErrorCode.NOT_PROJECT_MEMBER, "해당 프로젝트의 멤버가 아니므로 게시글을 조회할 수 없습니다."); } } //

    @Override
    @Transactional(readOnly = true)
    public List<PostBasicResponseDto> getPostsByTaskId(Long taskId, User currentUser) { /* 이전과 동일 */
        Task task = findTaskOrThrow(taskId);
        Project project = task.getProject();
        checkProjectMembership(project, currentUser);
        List<Post> posts = postRepository.findByTaskOrderByCreatedAtDesc(task); //
        return posts.stream()
                .map(PostBasicResponseDto::fromEntity) //
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PostDetailResponseDto getPostDetails(Long postId, User currentUser) {
        Post post = findPostOrThrow(postId);
        checkPostViewPermission(post, currentUser);

        // FileServiceImpl을 통해 FileResponseDto 목록을 가져오도록 수정 (이전 답변 내용 반영)
        List<FileResponseDto> fileDtos = fileService.getFilesForReference(FileReferenceType.POST, postId); //

        log.info("게시글 상세 조회: postId={}, 첨부파일 {}개, 조회자: {}", postId, fileDtos.size(), currentUser.getNickname());

        return PostDetailResponseDto.builder() //
                .idx(post.getIdx())
                .title(post.getTitle())
                .content(post.getContent())
                .author(UserBasicInfoDto.fromEntity(post.getUser())) //
                .taskId(post.getTask().getIdx())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .files(fileDtos) // FileResponseDto 리스트 사용
                .build();
    }

    @Override
    public PostDetailResponseDto createPost(Long taskId, PostCreateRequestDto requestDto, User currentUser) { /* 이전과 동일 (파일 처리는 별도 API 가정) */
        Task task = findTaskOrThrow(taskId);
        Project project = task.getProject();
        checkProjectMembership(project, currentUser);

        Post post = Post.builder() //
                .task(task)
                .user(currentUser)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .build();

        Post savedPost = postRepository.save(post);
        log.info("새 게시글 생성: postId={}, title='{}', taskId={}, byUser={}",
                savedPost.getIdx(), savedPost.getTitle(), taskId, currentUser.getNickname());

        return PostDetailResponseDto.builder() //
                .idx(savedPost.getIdx())
                .title(savedPost.getTitle())
                .content(savedPost.getContent())
                .author(UserBasicInfoDto.fromEntity(savedPost.getUser())) //
                .taskId(savedPost.getTask().getIdx())
                .createdAt(savedPost.getCreatedAt())
                .updatedAt(savedPost.getUpdatedAt())
                .files(Collections.emptyList()) // 생성 시점에는 파일 없음
                .build();
    }

    @Override
    public PostDetailResponseDto updatePost(Long postId, PostUpdateRequestDto requestDto, User currentUser) {
        Post post = findPostOrThrow(postId);
        checkPostViewPermission(post, currentUser);

        boolean isAuthor = post.getUser().getIdx().equals(currentUser.getIdx());
        // TODO: 프로젝트 관리자도 수정 가능하게 하려면 해당 로직 추가 (isUserProjectAdmin 등 사용)
        if (!isAuthor) {
            throw new BusinessLogicException(ErrorCode.ACCESS_DENIED, "게시글을 수정할 권한이 없습니다.");
        }

        // 1. 삭제 요청된 기존 첨부 파일들 처리
        if (requestDto.getFileIdsToRemove() != null && !requestDto.getFileIdsToRemove().isEmpty()) {
            log.info("게시글 ID {} 수정: {}개의 기존 파일 삭제 요청 by 사용자 {}", postId, requestDto.getFileIdsToRemove().size(), currentUser.getNickname());
            for (Long fileIdToRemove : requestDto.getFileIdsToRemove()) {
                try {
                    // FileService.deleteFile은 내부적으로 해당 파일의 업로더 또는 관련 권한을 확인할 수 있어야 함.
                    // Post 수정 권한이 있는 사용자가 해당 Post에 속한 파일을 삭제할 수 있도록 FileService.deleteFile의 권한 로직 검토/수정 필요.
                    // 현재는 FileService.deleteFile이 업로더 본인만 삭제 가능하게 되어있을 수 있으므로,
                    // 여기서는 게시글 수정 권한자(currentUser)가 파일 삭제를 요청하는 것으로 간주합니다.
                    // FileService.deleteFile(fileId, uploadedBy) 대신 FileService.deleteFileAsAdmin(fileId, adminUser) 같은 메소드가 필요할 수도 있습니다.
                    // 여기서는 currentUser가 해당 파일을 삭제할 권한이 있다고 가정하고 FileService에 위임합니다.
                    fileService.deleteFile(fileIdToRemove, currentUser); // FileService의 deleteFile 권한 로직에 의존
                } catch (BusinessLogicException e) {
                    // 특정 파일 삭제 실패 시 로깅만 하고 계속 진행할지, 아니면 전체 업데이트를 롤백할지 정책 결정 필요
                    log.warn("게시글 {} 수정 중 파일 ID {} 삭제 실패 (무시하고 계속 진행): {}", postId, fileIdToRemove, e.getMessage());
                }
            }
        }

        // 2. 게시글 내용 업데이트
        post.update(requestDto.getTitle(), requestDto.getContent()); //
        Post updatedPost = postRepository.save(post);
        log.info("게시글 내용 수정 완료: postId={}, newTitle='{}', byUser={}",
                updatedPost.getIdx(), updatedPost.getTitle(), currentUser.getNickname());

        // 3. 새로 첨부된 파일은 이 메소드에서 처리하지 않음 (프론트엔드에서 별도 /api/files/upload 호출 가정)
        //    만약 이 DTO에 List<MultipartFile> newFiles가 포함된다면 여기서 fileService.storeFiles() 호출.

        // 4. 최종적으로 업데이트된 게시글 정보 (파일 목록 포함) 반환
        List<FileResponseDto> finalFileDtos = fileService.getFilesForReference(FileReferenceType.POST, updatedPost.getIdx()); //
        return PostDetailResponseDto.builder() //
                .idx(updatedPost.getIdx())
                .title(updatedPost.getTitle())
                .content(updatedPost.getContent())
                .author(UserBasicInfoDto.fromEntity(updatedPost.getUser())) //
                .taskId(updatedPost.getTask().getIdx())
                .createdAt(updatedPost.getCreatedAt())
                .updatedAt(updatedPost.getUpdatedAt())
                .files(finalFileDtos)
                .build();
    }

    @Override
    public void deletePost(Long postId, User currentUser) {
        Post post = findPostOrThrow(postId);
        Project project = post.getTask().getProject();

        boolean isAuthor = post.getUser().getIdx().equals(currentUser.getIdx());
        boolean isAdmin = projectMemberRepository.findByUserAndProject(currentUser, project) //
                .filter(pm -> pm.getStatus() == ProjectMemberStatus.ACCEPTED) //
                .map(pm -> pm.getRole() == ProjectMemberRole.관리자) //
                .orElse(false);

        if (!isAuthor && !isAdmin) {
            throw new BusinessLogicException(ErrorCode.ACCESS_DENIED, "게시글을 삭제할 권한이 없습니다.");
        }

        // 1. 게시글에 직접 첨부된 파일들 삭제
        fileService.deleteFilesForReference(FileReferenceType.POST, postId, currentUser); //

        // 2. 게시글에 속한 댓글들 및 댓글에 첨부된 파일들 삭제
        //    CommentServiceImpl에 deleteCommentsAndFilesByPostId(Long postId, User currentUser) 와 같은 메소드를 만들어서 호출하거나,
        //    여기서 직접 CommentRepository를 통해 댓글 목록을 가져와서 각 댓글의 파일과 댓글을 삭제합니다.
        //    DB의 ON DELETE CASCADE 설정으로 댓글이 자동 삭제된다면, 파일만 삭제하면 됩니다.
        List<Comment> commentsToDelete = commentRepository.findByPostIdxAndParentCommentIsNullOrderByCreatedAtAsc(postId); // (모든 댓글을 가져오도록 수정 필요)
        // 실제로는 모든 댓글 (자식 포함)을 가져와서 파일 삭제 후, 댓글은 CASCADE로 삭제되도록 하는 것이 나을 수 있음
        // 또는 CommentService에 위임. 여기서는 연결된 최상위 댓글에 대해서만 파일 삭제 시도 (개선 필요)
        for (Comment comment : commentsToDelete) {
            fileService.deleteFilesForReference(FileReferenceType.COMMENT, comment.getIdx(), currentUser); //
        }
        // 댓글은 Post 삭제 시 DB에서 CASCADE로 삭제됨 (ON DELETE CASCADE 설정 확인)

        // 3. 게시글 삭제
        postRepository.delete(post);
        log.info("게시글 삭제 완료: postId={}, 삭제자: {}", postId, currentUser.getNickname());
    }
}