package com.example.softengineerwebpr.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor // JSON 바인딩을 위해 기본 생성자 필요
public class PostUpdateRequestDto {

    @NotBlank(message = "게시글 제목은 필수입니다.")
    @Size(max = 255, message = "게시글 제목은 255자를 넘을 수 없습니다.")
    private String title;

    @NotBlank(message = "게시글 내용은 필수입니다.")
    private String content; // Quill 에디터의 HTML 내용

    // 필요한 경우 다른 수정 가능 필드 추가
    // private List<Long> fileIdsToAttach; // 새로 첨부할 파일 ID 목록
    // private List<Long> fileIdsToRemove; // 삭제할 기존 첨부 파일 ID 목록

    public PostUpdateRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}