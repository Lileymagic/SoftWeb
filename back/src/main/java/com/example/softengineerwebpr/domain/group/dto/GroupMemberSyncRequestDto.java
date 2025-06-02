package com.example.softengineerwebpr.domain.group.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GroupMemberSyncRequestDto {

    @NotNull(message = "사용자 ID 목록은 null일 수 없습니다. 빈 리스트일 수는 있습니다.")
    private List<Long> userIds; // 그룹에 포함될 전체 사용자 ID 목록

    public GroupMemberSyncRequestDto(List<Long> userIds) {
        this.userIds = userIds;
    }
}