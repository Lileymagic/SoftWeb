package com.example.softengineerwebpr.domain.dashboard.service.impl;

import com.example.softengineerwebpr.common.exception.BusinessLogicException;
import com.example.softengineerwebpr.common.exception.ErrorCode;
import com.example.softengineerwebpr.domain.dashboard.dto.DeadlineComplianceStatsDto;
import com.example.softengineerwebpr.domain.dashboard.dto.ProjectDashboardStatsDto;
import com.example.softengineerwebpr.domain.dashboard.dto.TaskProgressStatsDto;
import com.example.softengineerwebpr.domain.dashboard.repository.DashboardRepository;
import com.example.softengineerwebpr.domain.dashboard.service.DashboardService;
import com.example.softengineerwebpr.domain.project.entity.Project;
import com.example.softengineerwebpr.domain.project.entity.ProjectMemberStatus;
import com.example.softengineerwebpr.domain.project.repository.ProjectMemberRepository;
import com.example.softengineerwebpr.domain.project.repository.ProjectRepository;
import com.example.softengineerwebpr.domain.task.entity.Task;
import com.example.softengineerwebpr.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {
    private final DashboardRepository dashboardRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Override
    public ProjectDashboardStatsDto getProjectDashboardStats(Long projectId, User currentUser) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.PROJECT_NOT_FOUND));

        projectMemberRepository.findByUserAndProject(currentUser, project)
                .filter(pm -> pm.getStatus() == ProjectMemberStatus.ACCEPTED)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.NOT_PROJECT_MEMBER));

        TaskProgressStatsDto taskProgress = getTaskProgressStats(project);

        DeadlineComplianceStatsDto deadlineCompliance = getDeadlineComplianceStats(project);

        log.info("프로젝트 {} 대시보드 통계 조회 완료 - 사용자: {}", projectId, currentUser.getNickname());

        return ProjectDashboardStatsDto.builder()
                .taskProgress(taskProgress)
                .deadlineCompliance(deadlineCompliance)
                .build();
    }

    private TaskProgressStatsDto getTaskProgressStats(Project project) {
        List<Object[]> statusCounts = dashboardRepository.findTaskStatusCountsByProject(project);

        int todoCount = 0, inProgressCount = 0, doneCount = 0;

        for(Object[] result : statusCounts) {
            Task.TaskStatus status = (Task.TaskStatus) result[0];
            Long count = (Long) result[1];

            switch (status) {
                case ToDo:
                    todoCount = count.intValue();
                    break;
                case InProgress:
                    inProgressCount = count.intValue();
                    break;
                case Done:
                    doneCount = count.intValue();
                    break;
            }
        }

        return TaskProgressStatsDto.builder()
                .todoCount(todoCount)
                .inProgressCount(inProgressCount)
                .doneCount(doneCount).build();
    }

    private DeadlineComplianceStatsDto getDeadlineComplianceStats(Project project) {
        List<Object[]> complianceCounts = dashboardRepository.findDeadlineComplianceByProject(project);

        int onTimeCount = 0, overdueCount = 0;

        for(Object[] result : complianceCounts) {
            String compliance = (String) result[0];
            Long count = (Long) result[1];

            if ("ON_TIME".equals(compliance)) {
                onTimeCount = count.intValue();
            }
            else if ("OVERDUE".equals(compliance)) {
                overdueCount = count.intValue();
            }
        }

        return DeadlineComplianceStatsDto.builder()
                .onTimeCount(onTimeCount)
                .overdueCount(overdueCount).build();
    }
}
