package com.example.softengineerwebpr.domain.dashboard.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProjectDashboardStatsDto {
    private TaskProgressStatsDto taskProgress;
    private DeadlineComplianceStatsDto deadlineCompliance;

    @Builder
    public ProjectDashboardStatsDto(TaskProgressStatsDto taskProgress, DeadlineComplianceStatsDto deadlineCompliance) {
        this.taskProgress = taskProgress;
        this.deadlineCompliance = deadlineCompliance;
    }
}
