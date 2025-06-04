package com.example.softengineerwebpr.domain.post.dto; //

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List; // List 임포트 추가

@Getter
@Setter
@NoArgsConstructor // JSON 바인딩을 위해 기본 생성자 필요
public class PostUpdateRequestDto {

    @NotBlank(message = "게시글 제목은 필수입니다.") //
    @Size(max = 255, message = "게시글 제목은 255자를 넘을 수 없습니다.") //
    private String title; //

    @NotBlank(message = "게시글 내용은 필수입니다.") //
    private String content; // Quill 에디터의 HTML 내용

    // 삭제할 기존 첨부 파일 ID 목록 (새로 추가 또는 주석 해제)
    private List<Long> fileIdsToRemove;

    // 모든 필드를 받는 생성자 (필요에 따라)
    public PostUpdateRequestDto(String title, String content, List<Long> fileIdsToRemove) { // 일부 수정
        this.title = title;
        this.content = content;
        this.fileIdsToRemove = fileIdsToRemove;
    }
}