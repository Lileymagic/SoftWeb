package com.example.softengineerwebpr.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostCreateRequestDto {

    @NotBlank(message = "게시글 제목은 필수입니다.")
    @Size(max = 255, message = "게시글 제목은 255자를 넘을 수 없습니다.")
    private String title;

    @NotBlank(message = "게시글 내용은 필수입니다.")
    // TEXT 타입이므로 DB 레벨에서 길이 제한, 여기서는 NotBlank만 확인
    private String content;

    // 첨부파일 ID 목록 등은 추후 파일 업로드 기능 구현 시 추가
    // private List<Long> fileIds;

    public PostCreateRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}