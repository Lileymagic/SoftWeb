package com.example.softengineerwebpr.domain.group.dto;

import com.example.softengineerwebpr.domain.group.entity.Group;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GroupResponseDto {
    private Long idx;
    private String name;
    private String color;
    private Integer permissionBitmask; // 현재 그룹에 적용된 권한 비트마스크
    private Long projectId;

    @Builder
    public GroupResponseDto(Long idx, String name, String color, Integer permissionBitmask, Long projectId) {
        this.idx = idx;
        this.name = name;
        this.color = color;
        this.permissionBitmask = permissionBitmask;
        this.projectId = projectId;
    }

    public static GroupResponseDto fromEntity(Group group) {
        if (group == null) return null;
        return GroupResponseDto.builder()
                .idx(group.getIdx())
                .name(group.getName())
                .color(group.getColor())
                .permissionBitmask(group.getPermission())
                .projectId(group.getProject() != null ? group.getProject().getIdx() : null)
                .build();
    }
}