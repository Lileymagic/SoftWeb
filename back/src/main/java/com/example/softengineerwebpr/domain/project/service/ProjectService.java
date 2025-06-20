package com.example.softengineerwebpr.domain.project.service;

import com.example.softengineerwebpr.domain.project.dto.ProjectCreateRequestDto;
import com.example.softengineerwebpr.domain.project.dto.ProjectInviteRequestDto;
import com.example.softengineerwebpr.domain.project.dto.ProjectMemberResponseDto;
import com.example.softengineerwebpr.domain.project.dto.ProjectResponseDto;
import com.example.softengineerwebpr.domain.project.dto.ProjectUpdateRequestDto;
import com.example.softengineerwebpr.domain.project.entity.ProjectMemberRole; // 경로 확인
import com.example.softengineerwebpr.domain.user.entity.User;

import java.util.List;

public interface ProjectService {
    ProjectResponseDto createProject(ProjectCreateRequestDto requestDto, User currentUser);
    List<ProjectResponseDto> getMyProjects(User currentUser);
    void inviteUserToProject(Long projectId, ProjectInviteRequestDto inviteDto, User inviter);
    List<ProjectMemberResponseDto> getProjectMembers(Long projectId, User currentUser);
    void updateProjectMemberRole(Long projectId, Long memberUserId, ProjectMemberRole newRole, User currentUser);
    void removeProjectMember(Long projectId, Long memberUserId, User currentUser);

    /**
     * 특정 프로젝트의 상세 정보를 조회합니다.
     *
     * @param projectId   조회할 프로젝트의 ID
     * @param currentUser 현재 작업을 수행하는 사용자 (프로젝트 접근 권한 검사)
     * @return 프로젝트 상세 정보를 담은 ProjectResponseDto
     */
    ProjectResponseDto getProjectDetails(Long projectId, User currentUser);

    // ... 기존 getProjectDetails 메소드 등 ...

    /**
     * 특정 프로젝트의 상세 정보(제목, 설명 등)를 수정합니다.
     *
     * @param projectId   수정할 프로젝트의 ID
     * @param requestDto  수정할 프로젝트 정보를 담은 DTO
     * @param currentUser 현재 작업을 수행하는 사용자 (권한 검사)
     * @return 수정된 프로젝트 정보를 담은 ProjectResponseDto
     */
    ProjectResponseDto updateProjectDetails(Long projectId, ProjectUpdateRequestDto requestDto, User currentUser);

    // --- 초대 수락/거절 기능 추가 ---
    void acceptProjectInvitation(Long projectId, User currentUser);
    void rejectProjectInvitation(Long projectId, User currentUser);

    /**
     * 현재 사용자가 받은 모든 보류 중(PENDING)인 프로젝트 초대 목록을 조회합니다. (새로 추가)
     * @param currentUser 현재 사용자
     * @return 초대받은 프로젝트의 정보가 담긴 ProjectResponseDto 리스트
     */
    List<ProjectResponseDto> getPendingInvitations(User currentUser);
}