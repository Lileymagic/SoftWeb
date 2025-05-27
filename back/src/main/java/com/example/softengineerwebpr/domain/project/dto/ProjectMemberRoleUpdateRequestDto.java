package com.example.softengineerwebpr.domain.project.dto;

import com.example.softengineerwebpr.domain.project.entity.ProjectMemberRole;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProjectMemberRoleUpdateRequestDto {
    @NotNull(message = "역할은 필수입니다.")
    private ProjectMemberRole role;
}