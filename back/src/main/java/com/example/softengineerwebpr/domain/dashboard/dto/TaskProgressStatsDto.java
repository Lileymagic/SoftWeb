package com.example.softengineerwebpr.domain.dashboard.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TaskProgressStatsDto {
    private int todoCount;
    private int inProgressCount;
    private int doneCount;
    private int totalCount;

    @Builder
    public TaskProgressStatsDto(int todoCount, int inProgressCount, int doneCount) {
        this.todoCount = todoCount;
        this.inProgressCount = inProgressCount;
        this.doneCount = doneCount;
        this.totalCount = todoCount + inProgressCount + doneCount;
    }
}
