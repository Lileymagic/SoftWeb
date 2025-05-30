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
import com.example.softengineerwebpr.domain.user.entity.User;
// import com.example.softengineerwebpr.domain.notification.service.NotificationService; // 알림 기능 연동 시
// import com.example.softengineerwebpr.domain.notification.common.NotificationType; // 알림 타입
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    // private final NotificationService notificationService; // 알림 기능 연동 시

    private Post findPostOrThrow(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.RESOURCE_NOT_FOUND, "게시글을 찾을 수 없습니다. ID: " + postId));
    }

    private Comment findCommentOrThrow(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.RESOURCE_NOT_FOUND, "댓글을 찾을 수 없습니다. ID: " + commentId));
    }

    private void checkProjectMembership(Post post, User user) {
        Project project = post.getTask().getProject(); // 게시글 -> 업무 -> 프로젝트 정보 가져오기
        if (!projectMemberRepository.findByUserAndProject(user, project)
                .filter(pm -> pm.getStatus() == ProjectMemberStatus.ACCEPTED)
                .isPresent()) {
            throw new BusinessLogicException(ErrorCode.NOT_PROJECT_MEMBER, "해당 프로젝트의 멤버가 아니므로 이 작업(댓글 조회/작성 등)을 수행할 수 없습니다.");
        }
    }

    @Override
    public CommentResponseDto createComment(Long postId, CommentCreateRequestDto requestDto, User currentUser) {
        Post post = findPostOrThrow(postId);
        checkProjectMembership(post, currentUser); // 댓글 작성 권한 확인 (프로젝트 멤버 여부)

        Comment parentComment = null;
        if (requestDto.getParentCommentId() != null) {
            parentComment = findCommentOrThrow(requestDto.getParentCommentId());
            // 부모 댓글이 같은 게시글에 속하는지 확인 (선택적이지만 중요)
            if (!parentComment.getPost().getIdx().equals(postId)) {
                throw new BusinessLogicException(ErrorCode.INVALID_INPUT_VALUE, "부모 댓글이 현재 게시글에 속해있지 않습니다.");
            }
        }

        Comment comment = Comment.builder()
                .post(post)
                .user(currentUser)
                .content(requestDto.getContent())
                .parentComment(parentComment)
                .build();

        Comment savedComment = commentRepository.save(comment);
        log.info("새 댓글 작성: postId={}, commentId={}, 작성자: {}", postId, savedComment.getIdx(), currentUser.getNickname());

        // TODO: 알림 기능 연동
        // if (parentComment == null) { // 최상위 댓글
        //    if (!post.getUser().equals(currentUser)) { // 자신의 게시글에 단 댓글이 아닐 경우
        //        notificationService.createNotification(post.getUser(), NotificationType.NEW_COMMENT_ON_MY_POST,
        //                                            currentUser.getNickname() + "님이 회원님의 게시글에 댓글을 남겼습니다.",
        //                                            post.getIdx(), "POST");
        //    }
        // } else { // 대댓글
        //    if (!parentComment.getUser().equals(currentUser)) { // 자신의 댓글에 단 대댓글이 아닐 경우
        //         notificationService.createNotification(parentComment.getUser(), NotificationType.NEW_REPLY_TO_MY_COMMENT,
        //                                            currentUser.getNickname() + "님이 회원님의 댓글에 답글을 남겼습니다.",
        //                                            post.getIdx(), "POST"); // postId 또는 parentCommentId를 참조로
        //    }
        // }


        return CommentResponseDto.fromEntity(savedComment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentsByPostId(Long postId, User currentUser) {
        Post post = findPostOrThrow(postId);
        checkProjectMembership(post, currentUser); // 댓글 조회 권한 확인

        List<Comment> comments = commentRepository.findByPostAndParentCommentIsNullOrderByCreatedAtAsc(post);

        return comments.stream()
                .map(CommentResponseDto::fromEntity) // fromEntity가 재귀적으로 자식 댓글도 변환
                .collect(Collectors.toList());
    }

    @Override
    public CommentResponseDto updateComment(Long commentId, String content, User currentUser) {
        Comment comment = findCommentOrThrow(commentId);
        checkProjectMembership(comment.getPost(), currentUser); // 댓글이 속한 게시글의 프로젝트 멤버인지 확인

        // 댓글 수정 권한 확인 (작성자 본인만)
        if (!comment.getUser().getIdx().equals(currentUser.getIdx())) {
            throw new BusinessLogicException(ErrorCode.ACCESS_DENIED, "댓글을 수정할 권한이 없습니다.");
        }

        comment.updateContent(content); // Comment 엔티티에 updateContent 메소드 사용
        Comment updatedComment = commentRepository.save(comment); // 변경 감지에 의해 저장될 수도 있지만 명시적 save
        log.info("댓글 수정: commentId={}, 수정자: {}", commentId, currentUser.getNickname());

        return CommentResponseDto.fromEntity(updatedComment);
    }

    @Override
    public void deleteComment(Long commentId, User currentUser) {
        Comment comment = findCommentOrThrow(commentId);
        Post post = comment.getPost();
        checkProjectMembership(post, currentUser); // 댓글이 속한 게시글의 프로젝트 멤버인지 확인

        // 댓글 삭제 권한 확인 (작성자 본인 또는 프로젝트 관리자 또는 게시글 작성자 등 - 정책에 따라)
        // 여기서는 작성자 본인만 삭제 가능하도록 설정
        boolean isAuthor = comment.getUser().getIdx().equals(currentUser.getIdx());
        // boolean isPostAuthor = post.getUser().getIdx().equals(currentUser.getIdx());
        // boolean isProjectAdmin = /* 프로젝트 관리자 확인 로직 */;

        if (!isAuthor /* && !isPostAuthor && !isProjectAdmin */) {
            throw new BusinessLogicException(ErrorCode.ACCESS_DENIED, "댓글을 삭제할 권한이 없습니다.");
        }

        // 대댓글이 있는 경우 실제 삭제 대신 내용 변경 (예: "삭제된 댓글입니다.") 또는 soft delete 처리도 고려 가능
        // 현재는 DB 스키마에 ON DELETE CASCADE가 Comment의 parent_comment_idx에 설정되어 있으므로,
        // 부모 댓글 삭제 시 자식 댓글도 DB 레벨에서 연쇄적으로 삭제됨.
        // 애플리케이션 레벨에서 대댓글이 있다면 삭제를 막거나 내용을 변경하는 정책을 추가할 수 있습니다.
        // 여기서는 orphanRemoval=true 및 cascade=ALL이 Comment 엔티티의 childComments에 설정되어 있으므로,
        // JPA를 통해 부모를 삭제하면 자식들도 함께 처리됩니다.

        commentRepository.delete(comment);
        log.info("댓글 삭제: commentId={}, 삭제자: {}", commentId, currentUser.getNickname());
    }
}