package com.example.softengineerwebpr.domain.task.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor // 요청 본문 바인딩을 위해 기본 생성자 필요
public class MemberAssignmentRequestDto {

    @NotNull(message = "할당할 사용자의 ID는 필수입니다.")
    private Long userId; // 업무에 할당(추가)할 사용자의 ID

    // 단일 필드를 위한 생성자 (필요시)
    public MemberAssignmentRequestDto(Long userId) {
        this.userId = userId;
    }
}