package com.example.softengineerwebpr.domain.history.service.impl;

import com.example.softengineerwebpr.common.exception.BusinessLogicException;
import com.example.softengineerwebpr.common.exception.ErrorCode;
import com.example.softengineerwebpr.domain.history.dto.HistoryResponseDto;
import com.example.softengineerwebpr.domain.history.entity.History;
import com.example.softengineerwebpr.domain.history.entity.HistoryActionType;
import com.example.softengineerwebpr.domain.history.repository.HistoryRepository;
import com.example.softengineerwebpr.domain.history.service.HistoryService;
import com.example.softengineerwebpr.domain.project.entity.Project;
import com.example.softengineerwebpr.domain.project.entity.ProjectMemberStatus;
import com.example.softengineerwebpr.domain.project.repository.ProjectMemberRepository;
import com.example.softengineerwebpr.domain.project.repository.ProjectRepository;
import com.example.softengineerwebpr.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Override
    public void recordHistory(Project project, User createdBy, HistoryActionType actionType, String description, Long targetId) {
        History history = History.builder()
                .project(project)
                .createdBy(createdBy)
                .actionType(actionType)
                .description(description)
                .targetId(targetId)
                .build();
        historyRepository.save(history);
        log.info("히스토리 기록: projectId={}, user={}, action={}, description='{}'",
                project.getIdx(), createdBy.getNickname(), actionType.name(), description);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoryResponseDto> getHistoryForProject(Long projectId, User currentUser) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.PROJECT_NOT_FOUND));

        // 히스토리 조회 권한 확인 (프로젝트 멤버인지)
        projectMemberRepository.findByUserAndProject(currentUser, project)
                .filter(pm -> pm.getStatus() == ProjectMemberStatus.ACCEPTED)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.NOT_PROJECT_MEMBER, "히스토리를 조회할 권한이 없습니다."));

        List<History> histories = historyRepository.findByProjectOrderByCreatedAtDesc(project);

        return histories.stream()
                .map(HistoryResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
}