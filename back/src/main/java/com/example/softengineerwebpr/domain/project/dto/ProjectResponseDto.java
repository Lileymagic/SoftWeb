package com.example.softengineerwebpr.domain.project.dto;

import com.example.softengineerwebpr.domain.project.entity.Project;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProjectResponseDto {
    private Long idx;
    private String title;
    private String description;
    private String createdByNickname; // 생성자 닉네임
    private String createdByEmail;    // 생성자 이메일 (필요시)
    private LocalDateTime createdAt;

    @Builder
    public ProjectResponseDto(Long idx, String title, String description, String createdByNickname, String createdByEmail, LocalDateTime createdAt) {
        this.idx = idx;
        this.title = title;
        this.description = description;
        this.createdByNickname = createdByNickname;
        this.createdByEmail = createdByEmail;
        this.createdAt = createdAt;
    }

    // 메서드 이름을 fromEntity 로 수정하고, static 제어자는 그대로 유지합니다.
    public static ProjectResponseDto fromEntity(Project project) {
        if (project == null) return null;
        // User 엔티티가 LAZY 로딩일 경우, getCreatedBy() 접근 시점에 실제 쿼리가 발생할 수 있습니다.
        // 서비스 계층에서 DTO로 변환할 때 트랜잭션 범위 내에서 처리하는 것이 안전합니다.
        if (project.getCreatedBy() == null) {
            // createdBy User가 없는 경우에 대한 처리 (예: 예외 발생 또는 기본값 설정)
            // 여기서는 닉네임과 이메일을 null 또는 기본 문자열로 설정할 수 있습니다.
            return ProjectResponseDto.builder()
                    .idx(project.getIdx())
                    .title(project.getTitle())
                    .description(project.getDescription())
                    .createdByNickname("N/A") // 또는 null
                    .createdByEmail("N/A")    // 또는 null
                    .createdAt(project.getCreatedAt())
                    .build();
        }
        return ProjectResponseDto.builder()
                .idx(project.getIdx())
                .title(project.getTitle())
                .description(project.getDescription())
                .createdByNickname(project.getCreatedBy().getNickname())
                .createdByEmail(project.getCreatedBy().getEmail())
                .createdAt(project.getCreatedAt())
                .build();
    }
}
