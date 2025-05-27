package com.example.softengineerwebpr.domain.task.dto;

import com.example.softengineerwebpr.domain.task.entity.Task.TaskStatus; // TaskStatus enum 임포트
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor // 요청 본문 바인딩을 위해 기본 생성자 필요
public class TaskStatusUpdateRequestDto {

    @NotNull(message = "새로운 업무 상태는 필수입니다.")
    private TaskStatus status; // 변경하려는 새로운 업무 상태

    // 단일 필드를 위한 생성자 (필요시)
    public TaskStatusUpdateRequestDto(TaskStatus status) {
        this.status = status;
    }
}