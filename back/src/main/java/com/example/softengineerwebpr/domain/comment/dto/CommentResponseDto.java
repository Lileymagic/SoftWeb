package com.example.softengineerwebpr.domain.comment.dto; //

import com.example.softengineerwebpr.domain.comment.entity.Comment; //
import com.example.softengineerwebpr.domain.file.dto.FileResponseDto; //
import com.example.softengineerwebpr.domain.user.dto.UserBasicInfoDto; //
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CommentResponseDto {
    private Long commentIdx;
    private Long postId;
    private UserBasicInfoDto author;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long parentCommentIdx;
    private List<CommentResponseDto> childComments;
    private List<FileResponseDto> files; // 파일 목록 필드 추가

    @Builder
    public CommentResponseDto(Long commentIdx, Long postId, UserBasicInfoDto author, String content,
                              LocalDateTime createdAt, LocalDateTime updatedAt, Long parentCommentIdx,
                              List<CommentResponseDto> childComments, List<FileResponseDto> files /* 빌더에 추가 */) {
        this.commentIdx = commentIdx;
        this.postId = postId;
        this.author = author;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.parentCommentIdx = parentCommentIdx;
        this.childComments = childComments;
        this.files = files; // 필드 초기화
    }

    // fromEntity 메소드 수정: List<FileResponseDto>를 인자로 받도록 하거나,
    // 서비스 레이어에서 Comment와 File 목록을 따로 가져와서 빌더로 직접 DTO를 생성합니다.
    // 여기서는 빌더를 직접 사용하는 것을 권장하는 형태로 두겠습니다.
    // 아래 fromEntity는 파일 없이 Comment 정보만으로 DTO를 만드는 예시입니다.
    // 파일 포함 시 서비스에서 빌더를 직접 사용해주세요.
    public static CommentResponseDto fromEntity(Comment comment, List<FileResponseDto> files) {
        if (comment == null) {
            return null;
        }
        return CommentResponseDto.builder()
                .commentIdx(comment.getIdx())
                .postId(comment.getPost() != null ? comment.getPost().getIdx() : null)
                .author(comment.getUser() != null ? UserBasicInfoDto.fromEntity(comment.getUser()) : null) //
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .parentCommentIdx(comment.getParentComment() != null ? comment.getParentComment().getIdx() : null)
                .childComments(comment.getChildComments() != null ?
                        comment.getChildComments().stream()
                                // 자식 댓글의 파일은 재귀적으로 가져오지 않거나, 필요시 로직 추가
                                .map(child -> CommentResponseDto.fromEntity(child, List.of()))
                                .collect(Collectors.toList()) :
                        null)
                .files(files) // 전달받은 파일 DTO 목록 설정
                .build();
    }
}