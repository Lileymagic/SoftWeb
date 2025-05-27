package com.example.softengineerwebpr.domain.task.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
// import java.util.List; // 추후 담당자 ID 목록 추가 가능성

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TaskCreateRequestDto {
    @NotBlank(message = "업무 제목은 필수입니다.")
    @Size(max = 255, message = "업무 제목은 255자를 넘을 수 없습니다.")
    private String title;

    private String description; // TEXT 타입이므로 길이 제한은 서비스 로직이나 DB에 위임

    private LocalDateTime deadline; // 마감 기한

    // 담당자 ID 목록 (선택적 - 초기 할당용)
    // private List<Long> assigneeUserIds;
}