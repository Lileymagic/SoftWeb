package com.example.softengineerwebpr.domain.comment.service.impl;

import com.example.softengineerwebpr.common.exception.BusinessLogicException;
import com.example.softengineerwebpr.common.exception.ErrorCode;
import com.example.softengineerwebpr.domain.comment.dto.CommentCreateRequestDto;
import com.example.softengineerwebpr.domain.comment.dto.CommentResponseDto;
import com.example.softengineerwebpr.domain.comment.entity.Comment;
import com.example.softengineerwebpr.domain.comment.repository.CommentRepository;
import com.example.softengineerwebpr.domain.comment.service.CommentService;
import com.example.softengineerwebpr.domain.post.entity.Post;
import com.example.softengineerwebpr.domain.post.repository.PostRepository;
import com.example.softengineerwebpr.domain.project.entity.Project;
import com.example.softengineerwebpr.domain.project.entity.ProjectMemberStatus;
import com.example.softengineerwebpr.domain.project.repository.ProjectMemberRepository;
import com.example.softengineerwebpr.domain.user.dto.UserBasicInfoDto;
import com.example.softengineerwebpr.domain.user.entity.User;
// import com.example.softengineerwebpr.domain.notification.service.NotificationService; // 알림 기능 연동 시
// import com.example.softengineerwebpr.domain.notification.common.NotificationType; // 알림 타입
import com.example.softengineerwebpr.domain.file.dto.FileResponseDto; //
import com.example.softengineerwebpr.domain.file.entity.FileReferenceType; //
import com.example.softengineerwebpr.domain.file.service.FileService; // FileService 주입
import com.example.softengineerwebpr.domain.comment.dto.CommentResponseDto; //
import java.util.Collections; // 필요시
import java.util.List; // 필요시
import java.util.stream.Collectors; // 필요시
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.softengineerwebpr.common.entity.NotificationType; // 알림 타입 임포트
import com.example.softengineerwebpr.domain.notification.service.NotificationService; // 알림 서비스 임포트

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final NotificationService notificationService;
    private final FileService fileService; // 새로 주입

    // Helper 메소드 (findPostOrThrow, findCommentOrThrow, checkProjectMembership)는 기존과 동일
    private Post findPostOrThrow(Long postId) { //
        return postRepository.findById(postId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.RESOURCE_NOT_FOUND, "게시글을 찾을 수 없습니다. ID: " + postId));
    }

    private Comment findCommentOrThrow(Long commentId) { //
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.RESOURCE_NOT_FOUND, "댓글을 찾을 수 없습니다. ID: " + commentId));
    }

    private void checkProjectMembership(Post post, User user) { //
        Project project = post.getTask().getProject();
        if (!projectMemberRepository.findByUserAndProject(user, project)
                .filter(pm -> pm.getStatus() == ProjectMemberStatus.ACCEPTED)
                .isPresent()) {
            throw new BusinessLogicException(ErrorCode.NOT_PROJECT_MEMBER, "해당 프로젝트의 멤버가 아니므로 이 작업을 수행할 수 없습니다.");
        }
    }

    // 댓글 엔티티를 CommentResponseDto로 변환하는 재귀 함수 (파일 정보 포함)
    private CommentResponseDto convertToDtoRecursive(Comment comment, User currentUser) {
        if (comment == null) {
            return null;
        }

        // 현재 댓글(comment)에 대한 파일 목록 조회
        List<FileResponseDto> fileDtos = fileService.getFilesForReference(FileReferenceType.COMMENT, comment.getIdx()); //

        // 자식 댓글(대댓글) 목록도 재귀적으로 DTO 변환 및 파일 정보 포함
        List<CommentResponseDto> childCommentDtos = null;
        if (comment.getChildComments() != null && !comment.getChildComments().isEmpty()) {
            childCommentDtos = comment.getChildComments().stream()
                    .map(child -> convertToDtoRecursive(child, currentUser)) // 자식 댓글도 재귀적으로 변환
                    .collect(Collectors.toList());
        }

        return CommentResponseDto.builder()
                .commentIdx(comment.getIdx())
                .postId(comment.getPost() != null ? comment.getPost().getIdx() : null)
                .author(comment.getUser() != null ? UserBasicInfoDto.fromEntity(comment.getUser()) : null) //
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .parentCommentIdx(comment.getParentComment() != null ? comment.getParentComment().getIdx() : null)
                .childComments(childCommentDtos != null ? childCommentDtos : List.of()) // null 대신 빈 리스트
                .files(fileDtos != null ? fileDtos : List.of()) // null 대신 빈 리스트
                .build(); //
    }


    @Override
    public CommentResponseDto createComment(Long postId, CommentCreateRequestDto requestDto, User currentUser) {
        Post post = findPostOrThrow(postId);
        checkProjectMembership(post, currentUser);

        Comment parentComment = null;
        if (requestDto.getParentCommentId() != null) {
            parentComment = findCommentOrThrow(requestDto.getParentCommentId());
            if (!parentComment.getPost().getIdx().equals(postId)) {
                throw new BusinessLogicException(ErrorCode.INVALID_INPUT_VALUE, "부모 댓글이 현재 게시글에 속해있지 않습니다.");
            }
        }

        Comment comment = Comment.builder()
                .post(post)
                .user(currentUser)
                .content(requestDto.getContent())
                .parentComment(parentComment)
                .build(); //

        Comment savedComment = commentRepository.save(comment);
        log.info("새 댓글 작성: postId={}, commentId={}, 작성자: {}", postId, savedComment.getIdx(), currentUser.getNickname());

        // ===== 알림 기록 로직 추가 시작 =====
        if (parentComment == null) { // 최상위 댓글인 경우 -> 게시글 작성자에게 알림
            User postAuthor = post.getUser();
            if (!postAuthor.equals(currentUser)) { // 자신의 게시글에 댓글 단 경우는 제외
                String content = String.format("'%s'님이 회원님의 게시글 '%s'에 댓글을 남겼습니다.",
                        currentUser.getNickname(), post.getTitle());
                notificationService.createAndSendNotification(postAuthor, NotificationType.NEW_COMMENT_ON_MY_POST,
                        content, post.getIdx(), "POST");
            }
        } else { // 대댓글인 경우 -> 부모 댓글 작성자에게 알림
            User parentCommentAuthor = parentComment.getUser();
            if (!parentCommentAuthor.equals(currentUser)) { // 자신의 댓글에 답글 단 경우는 제외
                String content = String.format("'%s'님이 회원님의 댓글에 답글을 남겼습니다.",
                        currentUser.getNickname());
                notificationService.createAndSendNotification(parentCommentAuthor, NotificationType.NEW_REPLY_TO_MY_COMMENT,
                        content, post.getIdx(), "POST"); // postId 또는 parentCommentId를 참조로 사용 가능
            }
        }
        // ===== 알림 기록 로직 추가 끝 =====

        // 파일은 별도 API로 업로드. 생성 시점에는 파일 없음.
        return CommentResponseDto.fromEntity(savedComment, Collections.emptyList()); //
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentsByPostId(Long postId, User currentUser) {
        Post post = findPostOrThrow(postId); //
        checkProjectMembership(post, currentUser); //

        List<Comment> topLevelComments = commentRepository.findByPostAndParentCommentIsNullOrderByCreatedAtAsc(post); //

        return topLevelComments.stream()
                .map(comment -> convertToDtoRecursive(comment, currentUser)) // 수정된 메소드 호출
                .collect(Collectors.toList());
    }

    @Override
    public CommentResponseDto updateComment(Long commentId, String content, User currentUser) {
        Comment comment = findCommentOrThrow(commentId);
        checkProjectMembership(comment.getPost(), currentUser);

        if (!comment.getUser().getIdx().equals(currentUser.getIdx())) {
            throw new BusinessLogicException(ErrorCode.ACCESS_DENIED, "댓글을 수정할 권한이 없습니다.");
        }

        comment.updateContent(content); //
        Comment updatedComment = commentRepository.save(comment);
        log.info("댓글 수정: commentId={}, 수정자: {}", commentId, currentUser.getNickname());

        List<FileResponseDto> fileDtos = fileService.getFilesForReference(FileReferenceType.COMMENT, updatedComment.getIdx());
        return CommentResponseDto.fromEntity(updatedComment, fileDtos); //
    }

    @Override
    public void deleteComment(Long commentId, User currentUser) {
        Comment comment = findCommentOrThrow(commentId);
        Post post = comment.getPost();
        checkProjectMembership(post, currentUser);

        boolean isAuthor = comment.getUser().getIdx().equals(currentUser.getIdx());
        // TODO: 프로젝트 관리자 등 추가 삭제 권한 확인
        if (!isAuthor) {
            throw new BusinessLogicException(ErrorCode.ACCESS_DENIED, "댓글을 삭제할 권한이 없습니다.");
        }

        // 1. 해당 댓글에 첨부된 파일 삭제
        fileService.deleteFilesForReference(FileReferenceType.COMMENT, commentId, currentUser);

        // 2. 댓글 삭제 (자식 댓글은 DB의 ON DELETE CASCADE 설정으로 자동 삭제됨)
        commentRepository.delete(comment);
        log.info("댓글 삭제: commentId={}, 삭제자: {}", commentId, currentUser.getNickname());
    }
}