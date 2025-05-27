package com.example.softengineerwebpr.domain.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProjectUpdateRequestDto {

    @NotBlank(message = "프로젝트 제목은 필수입니다.")
    @Size(max = 255, message = "프로젝트 제목은 255자를 넘을 수 없습니다.")
    private String title;

    @Size(max = 65535, message = "프로젝트 설명은 65535자를 넘을 수 없습니다.") // TEXT 타입 고려
    private String description;

    public ProjectUpdateRequestDto(String title, String description) {
        this.title = title;
        this.description = description;
    }
}