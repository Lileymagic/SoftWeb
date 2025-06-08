package com.example.softengineerwebpr.domain.dashboard.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DeadlineComplianceStatsDto {
    private int onTimeCount;
    private int overdueCount;
    private int totalCompletedCount;
    private double complianceRate;

    @Builder
    public DeadlineComplianceStatsDto(int onTimeCount, int overdueCount) {
        this.onTimeCount = onTimeCount;
        this.overdueCount = overdueCount;
        this.totalCompletedCount = onTimeCount + overdueCount;
        this.complianceRate = totalCompletedCount > 0 ?
            Math.round((double) onTimeCount / totalCompletedCount * 100.0) : 0.0;
    }
}
