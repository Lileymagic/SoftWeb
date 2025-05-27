package com.example.softengineerwebpr.domain.project.dto;

import com.example.softengineerwebpr.domain.project.entity.ProjectMember;
import com.example.softengineerwebpr.domain.project.entity.ProjectMemberRole;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProjectMemberResponseDto {
    private Long userId;
    private String nickname;
    private String identificationCode;
    private String email; // 필요시
    private ProjectMemberRole role;

    @Builder
    public ProjectMemberResponseDto(Long userId, String nickname, String identificationCode, String email, ProjectMemberRole role) {
        this.userId = userId;
        this.nickname = nickname;
        this.identificationCode = identificationCode;
        this.email = email;
        this.role = role;
    }

    public static ProjectMemberResponseDto fromEntity(ProjectMember projectMember) {
        if (projectMember == null || projectMember.getUser() == null) {
            // User 객체가 null인 경우, 또는 projectMember 자체가 null인 경우에 대한 방어 코드
            return null; // 또는 예외 처리, 또는 빈 DTO 반환 등 정책에 따라 결정
        }
        return ProjectMemberResponseDto.builder()
                .userId(projectMember.getUser().getIdx())
                .nickname(projectMember.getUser().getNickname())
                .identificationCode(projectMember.getUser().getIdentificationCode())
                .email(projectMember.getUser().getEmail()) // User 엔티티에 email 필드가 있다고 가정
                .role(projectMember.getRole())
                .build();
    }
}