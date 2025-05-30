package com.example.softengineerwebpr.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor // 프레임워크에서 객체 생성을 위해 기본 생성자가 필요할 수 있습니다.
public class CommentCreateRequestDto {

    @NotBlank(message = "댓글 내용은 필수입니다.")
    @Size(max = 65535, message = "댓글 내용은 65535자를 넘을 수 없습니다.") // TEXT 타입 고려
    private String content; // 댓글 내용

    private Long parentCommentId; // 부모 댓글 ID (대댓글인 경우, 없으면 null)

    // 모든 필드를 받는 생성자 (필요에 따라)
    public CommentCreateRequestDto(String content, Long parentCommentId) {
        this.content = content;
        this.parentCommentId = parentCommentId;
    }

    // content만 받는 생성자 (최상위 댓글용)
    public CommentCreateRequestDto(String content) {
        this.content = content;
        this.parentCommentId = null;
    }
}