package com.example.softengineerwebpr.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentUpdateRequestDto {

    @NotBlank(message = "댓글 내용은 필수입니다.")
    @Size(max = 65535, message = "댓글 내용은 65535자를 넘을 수 없습니다.") // TEXT 타입 고려
    private String content; // 수정할 댓글 내용

    public CommentUpdateRequestDto(String content) {
        this.content = content;
    }
}