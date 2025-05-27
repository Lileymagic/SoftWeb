package com.example.softengineerwebpr.domain.task.dto;

import com.example.softengineerwebpr.domain.task.entity.Task; // Task.TaskStatus를 참조하기 위함
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
// import java.util.List; // 담당자 목록 업데이트가 필요하다면 추가

@Getter
@Setter
@NoArgsConstructor
public class TaskUpdateRequestDto {

    @Size(max = 255, message = "업무 제목은 255자를 넘을 수 없습니다.")
    private String title; // 변경할 제목 (선택적)

    private String description; // 변경할 설명 (선택적)

    private Task.TaskStatus status; // 변경할 상태 (선택적)

    private LocalDateTime deadline; // 변경할 마감 기한 (선택적)

    // 담당자 목록을 이 DTO에서 직접 변경하게 할 수도 있습니다.
    // private List<Long> assigneeUserIds;

    // 모든 필드를 받는 생성자 (필요에 따라)
    public TaskUpdateRequestDto(String title, String description, Task.TaskStatus status, LocalDateTime deadline) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.deadline = deadline;
    }
}