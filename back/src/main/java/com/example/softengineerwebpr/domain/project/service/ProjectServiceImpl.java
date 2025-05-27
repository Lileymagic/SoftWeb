package com.example.softengineerwebpr.domain.project.service;

import com.example.softengineerwebpr.common.entity.Permission;
import com.example.softengineerwebpr.common.exception.BusinessLogicException;
import com.example.softengineerwebpr.common.exception.ErrorCode;
import com.example.softengineerwebpr.domain.group.entity.Group;
import com.example.softengineerwebpr.domain.group.repository.GroupMemberRepository;
import com.example.softengineerwebpr.domain.notification.service.NotificationService;
import com.example.softengineerwebpr.domain.project.dto.ProjectCreateRequestDto;
import com.example.softengineerwebpr.domain.project.dto.ProjectInviteRequestDto;
import com.example.softengineerwebpr.domain.project.dto.ProjectMemberResponseDto;
import com.example.softengineerwebpr.domain.project.dto.ProjectResponseDto;
import com.example.softengineerwebpr.domain.project.entity.Project;
import com.example.softengineerwebpr.domain.project.entity.ProjectMember;
import com.example.softengineerwebpr.domain.project.entity.ProjectMemberRole;
import com.example.softengineerwebpr.domain.project.entity.ProjectMemberStatus;
import com.example.softengineerwebpr.domain.project.repository.ProjectMemberRepository;
import com.example.softengineerwebpr.domain.project.repository.ProjectRepository;
import com.example.softengineerwebpr.domain.user.entity.User;
import com.example.softengineerwebpr.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final NotificationService notificationService;

    private boolean userHasProjectPermission(User user, Project project, Permission permissionToCheck) {
        Optional<ProjectMember> directMembershipOpt = projectMemberRepository.findByUserAndProject(user, project);
        if (directMembershipOpt.isPresent() && directMembershipOpt.get().getRole() == ProjectMemberRole.관리자 && directMembershipOpt.get().getStatus() == ProjectMemberStatus.ACCEPTED) {
            return true;
        }

        if(directMembershipOpt.isPresent() && directMembershipOpt.get().getStatus() == ProjectMemberStatus.ACCEPTED) {
            // 수정된 부분: GroupMemberRepository의 새로운 메서드 사용
            List<com.example.softengineerwebpr.domain.group.entity.GroupMember> groupMembershipsInProject =
                    groupMemberRepository.findByUserAndGroup_Project(user, project);

            for (com.example.softengineerwebpr.domain.group.entity.GroupMember gm : groupMembershipsInProject) {
                Group group = gm.getGroup();
                // 이미 특정 프로젝트의 그룹 멤버만 조회했으므로 group.getProject().equals(project) 조건은 필요 없어짐
                if (group.getPermission() != null && (group.getPermission() & permissionToCheck.getBit()) == permissionToCheck.getBit()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void checkUserProjectPermission(User user, Project project, Permission permissionToCheck, ErrorCode errorCodeOnFailure) {
        if (!userHasProjectPermission(user, project, permissionToCheck)) {
            throw new BusinessLogicException(errorCodeOnFailure);
        }
    }

    @Override
    public ProjectResponseDto createProject(ProjectCreateRequestDto requestDto, User currentUser) {
        log.info("프로젝트 생성 시도: title={}, byUser={}", requestDto.getTitle(), currentUser.getNickname());

        Project newProject = Project.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .createdBy(currentUser)
                .build();
        Project savedProject = projectRepository.save(newProject);

        ProjectMember projectCreatorAsMember = ProjectMember.builder()
                .project(savedProject)
                .user(currentUser)
                .role(ProjectMemberRole.관리자)
                .status(ProjectMemberStatus.ACCEPTED)
                .build();
        projectMemberRepository.save(projectCreatorAsMember);
        log.info("프로젝트 멤버 추가 (생성자): projectId={}, userId={}, role=관리자, status=ACCEPTED", savedProject.getIdx(), currentUser.getIdx());

        return ProjectResponseDto.fromEntity(savedProject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponseDto> getMyProjects(User currentUser) {
        log.info("사용자 {}의 프로젝트 목록 조회", currentUser.getNickname());
        List<ProjectMember> memberships = projectMemberRepository.findByUser(currentUser);
        List<Project> projects = memberships.stream()
                .filter(pm -> pm.getStatus() == ProjectMemberStatus.ACCEPTED)
                .map(ProjectMember::getProject)
                .distinct()
                .collect(Collectors.toList());

        return projects.stream()
                .map(ProjectResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void inviteUserToProject(Long projectId, ProjectInviteRequestDto inviteDto, User inviter) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.PROJECT_NOT_FOUND));

        checkUserProjectPermission(inviter, project, Permission.MANAGE_PROJECT_MEMBERS, ErrorCode.NO_AUTHORITY_TO_INVITE);

        User invitee = userRepository.findById(inviteDto.getInviteeUserId())
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.USER_NOT_FOUND));

        Optional<ProjectMember> existingMembershipOpt = projectMemberRepository.findByUserAndProject(invitee, project);
        if (existingMembershipOpt.isPresent()) {
            ProjectMember existingMembership = existingMembershipOpt.get();
            if (existingMembership.getStatus() == ProjectMemberStatus.ACCEPTED) {
                throw new BusinessLogicException(ErrorCode.ALREADY_PROJECT_MEMBER);
            } else if (existingMembership.getStatus() == ProjectMemberStatus.PENDING) {
                throw new BusinessLogicException(ErrorCode.PROJECT_INVITATION_PENDING);
            } else if (existingMembership.getStatus() == ProjectMemberStatus.REJECTED) {
                projectMemberRepository.delete(existingMembership);
                log.info("기존 거절된 초대 기록 삭제 후 재초대: projectId={}, inviteeId={}", projectId, invitee.getIdx());
            }
        }

        ProjectMember newInvitation = ProjectMember.builder()
                .project(project)
                .user(invitee)
                .role(ProjectMemberRole.일반사용자)
                .status(ProjectMemberStatus.PENDING)
                .build();
        projectMemberRepository.save(newInvitation);

        log.info("사용자 {}가 프로젝트 {}에 사용자 {}를 초대 (상태: PENDING)", inviter.getNickname(), projectId, invitee.getNickname());
        notificationService.createProjectInvitationNotification(inviter, invitee, project);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectMemberResponseDto> getProjectMembers(Long projectId, User currentUser) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.PROJECT_NOT_FOUND));

        projectMemberRepository.findByUserAndProject(currentUser, project)
                .filter(pm -> pm.getStatus() == ProjectMemberStatus.ACCEPTED)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.NOT_PROJECT_MEMBER));

        List<ProjectMember> members = projectMemberRepository.findByProject(project).stream()
                .filter(pm -> pm.getStatus() == ProjectMemberStatus.ACCEPTED)
                .collect(Collectors.toList());

        return members.stream()
                .map(ProjectMemberResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void updateProjectMemberRole(Long projectId, Long memberUserId, ProjectMemberRole newRole, User currentUser) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.PROJECT_NOT_FOUND));
        User memberToUpdateUserEntity = userRepository.findById(memberUserId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.USER_NOT_FOUND));

        checkUserProjectPermission(currentUser, project, Permission.MANAGE_PROJECT_MEMBERS, ErrorCode.NO_AUTHORITY_TO_MANAGE_MEMBERS);

        if (newRole == ProjectMemberRole.관리자 && !currentUser.getIdx().equals(project.getCreatedBy().getIdx()) ) {
            checkUserProjectPermission(currentUser, project, Permission.DELEGATE_PROJECT_PERMISSIONS, ErrorCode.NO_AUTHORITY_TO_MANAGE_MEMBERS);
        }

        if (currentUser.getIdx().equals(memberUserId)) {
            throw new BusinessLogicException(ErrorCode.CANNOT_MODIFY_OWN_ROLE_OR_REMOVE_ONESELF_AS_ADMIN);
        }

        ProjectMember targetMembership = projectMemberRepository.findByUserAndProject(memberToUpdateUserEntity, project)
                .filter(pm -> pm.getStatus() == ProjectMemberStatus.ACCEPTED)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.TARGET_USER_NOT_PROJECT_MEMBER));

        targetMembership.updateRole(newRole);
        log.info("프로젝트 {}의 멤버 {}의 역할을 {}로 변경 (수행자: {})", projectId, memberUserId, newRole, currentUser.getNickname());
    }

    @Override
    public void removeProjectMember(Long projectId, Long memberUserId, User currentUser) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.PROJECT_NOT_FOUND));
        User memberToRemoveUserEntity = userRepository.findById(memberUserId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.USER_NOT_FOUND));

        checkUserProjectPermission(currentUser, project, Permission.MANAGE_PROJECT_MEMBERS, ErrorCode.NO_AUTHORITY_TO_MANAGE_MEMBERS);

        if (currentUser.getIdx().equals(memberUserId)) {
            throw new BusinessLogicException(ErrorCode.CANNOT_MODIFY_OWN_ROLE_OR_REMOVE_ONESELF_AS_ADMIN);
        }

        ProjectMember targetMembership = projectMemberRepository.findByUserAndProject(memberToRemoveUserEntity, project)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.TARGET_USER_NOT_PROJECT_MEMBER));

        if (targetMembership.getRole() == ProjectMemberRole.관리자 && targetMembership.getStatus() == ProjectMemberStatus.ACCEPTED) {
            long adminCount = projectMemberRepository.findByProject(project).stream()
                    .filter(pm -> pm.getRole() == ProjectMemberRole.관리자 && pm.getStatus() == ProjectMemberStatus.ACCEPTED)
                    .count();
            if (adminCount <= 1) {
                throw new BusinessLogicException(ErrorCode.CANNOT_REMOVE_LAST_ADMIN);
            }
        }

        projectMemberRepository.delete(targetMembership);
        log.info("프로젝트 {}에서 멤버 {} 제외/초대취소 (수행자: {})", projectId, memberUserId, currentUser.getNickname());
    }

    @Override
    public void acceptProjectInvitation(Long projectId, User currentUser) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.PROJECT_NOT_FOUND));

        ProjectMember membership = projectMemberRepository.findByUserAndProject(currentUser, project)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.NO_PENDING_INVITATION, "해당 프로젝트에 대한 초대 정보를 찾을 수 없습니다."));

        if (membership.getStatus() != ProjectMemberStatus.PENDING) {
            throw new BusinessLogicException(ErrorCode.INVITATION_ALREADY_PROCESSED);
        }

        membership.acceptInvitation();
        log.info("사용자 {}가 프로젝트 '{}'({}}) 초대를 수락했습니다.", currentUser.getNickname(), project.getTitle(), projectId);
    }

    @Override
    public void rejectProjectInvitation(Long projectId, User currentUser) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.PROJECT_NOT_FOUND));

        ProjectMember membership = projectMemberRepository.findByUserAndProject(currentUser, project)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.NO_PENDING_INVITATION, "해당 프로젝트에 대한 초대 정보를 찾을 수 없습니다."));

        if (membership.getStatus() != ProjectMemberStatus.PENDING) {
            throw new BusinessLogicException(ErrorCode.INVITATION_ALREADY_PROCESSED);
        }
        projectMemberRepository.delete(membership);
        log.info("사용자 {}가 프로젝트 '{}'({}) 초대를 거절했습니다. (초대 기록 삭제)", currentUser.getNickname(), project.getTitle(), projectId);
    }
}