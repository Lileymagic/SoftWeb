package com.example.softengineerwebpr.domain.group.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GroupMemberManagementRequestDto {

    @NotEmpty(message = "사용자 ID 목록은 비워둘 수 없습니다.")
    private List<Long> userIds; // 그룹에 추가하거나 제외할 사용자들의 ID 목록

    public GroupMemberManagementRequestDto(List<Long> userIds) {
        this.userIds = userIds;
    }
}