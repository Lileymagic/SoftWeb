package com.example.softengineerwebpr.domain.group.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GroupPermissionUpdateRequestDto {

    @NotNull(message = "권한 비트마스크 값은 필수입니다.")
    private Integer permissionBitmask; // 새로운 권한 비트마스크 값

    public GroupPermissionUpdateRequestDto(Integer permissionBitmask) {
        this.permissionBitmask = permissionBitmask;
    }
}