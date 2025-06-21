package com.example.softengineerwebpr.domain.history.service;

import com.example.softengineerwebpr.domain.history.dto.HistoryResponseDto;
import com.example.softengineerwebpr.domain.history.entity.HistoryActionType;
import com.example.softengineerwebpr.domain.project.entity.Project;
import com.example.softengineerwebpr.domain.user.entity.User;

import java.util.List;

public interface HistoryService {

    /**
     * 프로젝트에 대한 히스토리(활동 로그)를 기록합니다.
     * @param project 히스토리가 속한 프로젝트
     * @param createdBy 행동을 수행한 사용자
     * @param actionType 행동 유형 (Enum)
     * @param description 활동에 대한 상세 설명
     * @param targetId 행동 대상의 ID (선택적, 예: 업무 ID, 사용자 ID)
     */
    void recordHistory(Project project, User createdBy, HistoryActionType actionType, String description, Long targetId);

    /**
     * 특정 프로젝트의 히스토리 목록을 조회합니다.
     * @param projectId 히스토리를 조회할 프로젝트의 ID
     * @param currentUser 현재 작업을 요청한 사용자 (권한 확인용)
     * @return 해당 프로젝트의 히스토리 DTO 목록
     */
    List<HistoryResponseDto> getHistoryForProject(Long projectId, User currentUser);
}