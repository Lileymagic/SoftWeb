package com.example.softengineerwebpr.domain.project.service;

import com.example.softengineerwebpr.domain.project.dto.ProjectCreateRequestDto;
import com.example.softengineerwebpr.domain.project.dto.ProjectInviteRequestDto;
import com.example.softengineerwebpr.domain.project.dto.ProjectMemberResponseDto;
import com.example.softengineerwebpr.domain.project.dto.ProjectResponseDto;
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

    // --- 초대 수락/거절 기능 추가 ---
    void acceptProjectInvitation(Long projectId, User currentUser);
    void rejectProjectInvitation(Long projectId, User currentUser);
}