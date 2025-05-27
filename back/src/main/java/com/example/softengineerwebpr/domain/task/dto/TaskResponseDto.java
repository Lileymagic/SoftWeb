package com.example.softengineerwebpr.domain.task.dto;

import com.example.softengineerwebpr.domain.task.entity.Task;
import com.example.softengineerwebpr.domain.user.dto.UserBasicInfoDto;
import com.example.softengineerwebpr.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class TaskResponseDto {
    private Long idx;
    private String title;
    private String description;
    private String status; // Task.TaskStatus.name() 사용
    private LocalDateTime deadline;
    private LocalDateTime createdAt;
    private UserBasicInfoDto createdBy; // 작성자 정보
    private List<UserBasicInfoDto> assignedMembers; // 담당자 목록

    @Builder
    public TaskResponseDto(Long idx, String title, String description, String status,
                           LocalDateTime deadline, LocalDateTime createdAt, UserBasicInfoDto createdBy,
                           List<UserBasicInfoDto> assignedMembers) {
        this.idx = idx;
        this.title = title;
        this.description = description;
        this.status = status;
        this.deadline = deadline;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.assignedMembers = assignedMembers;
    }

    // "TaskResponseDtofromEntity"에서 "TaskResponseDto fromEntity"로 수정
    public static TaskResponseDto fromEntity(Task task, List<User> members) {
        if (task == null) return null;
        return TaskResponseDto.builder()
                .idx(task.getIdx())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus().name()) // Enum의 이름을 문자열로
                .deadline(task.getDeadline())
                .createdAt(task.getCreatedAt())
                .createdBy(UserBasicInfoDto.fromEntity(task.getCreatedBy()))
                .assignedMembers(members.stream()
                        .map(UserBasicInfoDto::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}