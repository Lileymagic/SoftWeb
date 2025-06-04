package com.example.softengineerwebpr.domain.task.dto; //

import com.example.softengineerwebpr.domain.file.dto.FileResponseDto; //
import com.example.softengineerwebpr.domain.task.entity.Task; //
import com.example.softengineerwebpr.domain.user.dto.UserBasicInfoDto; //
import com.example.softengineerwebpr.domain.user.entity.User; //
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
    private String status;
    private LocalDateTime deadline;
    private LocalDateTime createdAt;
    private UserBasicInfoDto createdBy;
    private List<UserBasicInfoDto> assignedMembers;
    private List<FileResponseDto> files; // 파일 목록 필드 추가

    @Builder
    public TaskResponseDto(Long idx, String title, String description, String status,
                           LocalDateTime deadline, LocalDateTime createdAt, UserBasicInfoDto createdBy,
                           List<UserBasicInfoDto> assignedMembers, List<FileResponseDto> files /* 빌더에 추가 */) {
        this.idx = idx;
        this.title = title;
        this.description = description;
        this.status = status;
        this.deadline = deadline;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.assignedMembers = assignedMembers;
        this.files = files; // 필드 초기화
    }

    // fromEntity 메소드 수정
    public static TaskResponseDto fromEntity(Task task, List<User> members, List<FileResponseDto> files) {
        if (task == null) return null;
        return TaskResponseDto.builder()
                .idx(task.getIdx())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus().name())
                .deadline(task.getDeadline())
                .createdAt(task.getCreatedAt())
                .createdBy(UserBasicInfoDto.fromEntity(task.getCreatedBy())) //
                .assignedMembers(members.stream()
                        .map(UserBasicInfoDto::fromEntity) //
                        .collect(Collectors.toList()))
                .files(files) // 전달받은 파일 DTO 목록 설정
                .build();
    }
}