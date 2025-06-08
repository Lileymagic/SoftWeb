package com.example.softengineerwebpr.domain.dashboard.controller;

import com.example.softengineerwebpr.common.dto.ApiResponse;
import com.example.softengineerwebpr.common.util.AuthenticationUtil;
import com.example.softengineerwebpr.domain.dashboard.dto.ProjectDashboardStatsDto;
import com.example.softengineerwebpr.domain.dashboard.service.DashboardService;
import com.example.softengineerwebpr.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/projects/{projectId}/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;
    private final AuthenticationUtil authenticationUtil;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<ProjectDashboardStatsDto>> getProjectDashboardStats(
            @PathVariable Long projectId, Authentication authentication) {
        User currentUser = authenticationUtil.getCurrentUser(authentication);
        log.info("프로젝트 {} 대시보드 통계 요청 - 사용자 {}", projectId, currentUser.getNickname());

        ProjectDashboardStatsDto stats = dashboardService.getProjectDashboardStats(projectId, currentUser);

        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK.value(),
                "프로젝트 대시보드 통계 조회 성공",
                stats
        ));
    }
}
