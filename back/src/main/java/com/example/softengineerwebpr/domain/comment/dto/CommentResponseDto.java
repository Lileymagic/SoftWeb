package com.example.softengineerwebpr.domain.comment.dto;

import com.example.softengineerwebpr.domain.comment.entity.Comment;
import com.example.softengineerwebpr.domain.user.dto.UserBasicInfoDto; // 담당자, 작성자 등 간단한 유저 정보용 DTO 재활용
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CommentResponseDto {
    private Long commentIdx;        // 댓글 ID
    private Long postId;            // 게시글 ID (댓글이 속한)
    private UserBasicInfoDto author; // 작성자 정보 (UserBasicInfoDto 활용)
    private String content;         // 댓글 내용
    private LocalDateTime createdAt;   // 작성일
    private LocalDateTime updatedAt;   // 수정일 (있다면)
    private Long parentCommentIdx;  // 부모 댓글 ID (대댓글인 경우)
    private List<CommentResponseDto> childComments; // 대댓글 목록 (재귀적으로 표현)

    @Builder
    public CommentResponseDto(Long commentIdx, Long postId, UserBasicInfoDto author, String content,
                              LocalDateTime createdAt, LocalDateTime updatedAt, Long parentCommentIdx,
                              List<CommentResponseDto> childComments) {
        this.commentIdx = commentIdx;
        this.postId = postId;
        this.author = author;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.parentCommentIdx = parentCommentIdx;
        this.childComments = childComments;
    }

    public static CommentResponseDto fromEntity(Comment comment) {
        if (comment == null) {
            return null;
        }
        return CommentResponseDto.builder()
                .commentIdx(comment.getIdx())
                .postId(comment.getPost() != null ? comment.getPost().getIdx() : null)
                .author(comment.getUser() != null ? UserBasicInfoDto.fromEntity(comment.getUser()) : null)
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .parentCommentIdx(comment.getParentComment() != null ? comment.getParentComment().getIdx() : null)
                .childComments(comment.getChildComments() != null ?
                        comment.getChildComments().stream()
                                .map(CommentResponseDto::fromEntity) // 자식 댓글들도 DTO로 변환
                                .collect(Collectors.toList()) :
                        null) // 또는 Collections.emptyList()
                .build();
    }
}