package com.example.softengineerwebpr.domain.dashboard.service;

import com.example.softengineerwebpr.domain.dashboard.dto.ProjectDashboardStatsDto;
import com.example.softengineerwebpr.domain.user.entity.User;

public interface DashboardService {
    ProjectDashboardStatsDto getProjectDashboardStats(Long projectId, User currentUser);
}
