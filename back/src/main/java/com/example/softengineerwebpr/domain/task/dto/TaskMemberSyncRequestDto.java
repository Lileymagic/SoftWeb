package com.example.softengineerwebpr.domain.task.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TaskMemberSyncRequestDto {
    /**
     * 이 업무에 할당될 모든 개별 사용자의 ID 목록
     */
    private List<Long> userIds;

    /**
     * 이 업무에 할당될 모든 그룹의 ID 목록
     * (이 그룹들에 속한 모든 사용자가 업무에 할당됩니다)
     */
    private List<Long> groupIds;
}