package com.example.softengineerwebpr.domain.project.service; // 사용자님의 원래 패키지 경로

import com.example.softengineerwebpr.common.entity.NotificationType;
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
import com.example.softengineerwebpr.domain.project.dto.ProjectUpdateRequestDto; // 추가된 DTO 임포트
import com.example.softengineerwebpr.domain.project.entity.Project;
import com.example.softengineerwebpr.domain.project.entity.ProjectMember;
import com.example.softengineerwebpr.domain.project.entity.ProjectMemberRole;
import com.example.softengineerwebpr.domain.project.entity.ProjectMemberStatus;
import com.example.softengineerwebpr.domain.project.repository.ProjectMemberRepository;
import com.example.softengineerwebpr.domain.project.repository.ProjectRepository;
import com.example.softengineerwebpr.domain.task.repository.TaskMemberRepository;
import com.example.softengineerwebpr.domain.user.entity.User;
import com.example.softengineerwebpr.domain.user.repository.UserRepository;
import com.example.softengineerwebpr.domain.history.entity.HistoryActionType; // 히스토리 Enum 임포트
import com.example.softengineerwebpr.domain.history.service.HistoryService; // 히스토리 서비스 임포트
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
    private final GroupMemberRepository groupMemberRepository; // 이전 코드에서 사용되었으므로 유지
    private final NotificationService notificationService; // 이전 코드에서 사용되었으므로 유지
    private final HistoryService historyService;
    private final TaskMemberRepository taskMemberRepository;

    // 헬퍼 메소드: 사용자가 프로젝트 관리자인지 확인 (ProjectMemberStatus.ACCEPTED 조건 포함)
    private boolean isUserProjectAdmin(User user, Project project) {
        return projectMemberRepository.findByUserAndProject(user, project)
                .filter(pm -> pm.getStatus() == ProjectMemberStatus.ACCEPTED)
                .map(pm -> pm.getRole() == ProjectMemberRole.관리자)
                .orElse(false);
    }

    // ProjectServiceImpl에 이미 있던 userHasProjectPermission 및 checkUserProjectPermission 메소드
    // (이전 답변들에서 ProjectServiceImpl의 일부로 제공되었던 내용이므로, 여기에 포함하거나 사용자님의 기존 코드를 확인해야 합니다.)
    // 만약 없다면, isUserProjectAdmin과 유사하게 또는 더 일반적인 권한 확인 로직이 필요합니다.
    // 여기서는 isUserProjectAdmin을 활용하거나, 프로젝트 생성자 여부로 권한을 판단하는 것으로 단순화합니다.

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
    @Transactional(readOnly = true)
    public ProjectResponseDto getProjectDetails(Long projectId, User currentUser) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.PROJECT_NOT_FOUND));

        projectMemberRepository.findByUserAndProject(currentUser, project)
                .filter(pm -> pm.getStatus() == ProjectMemberStatus.ACCEPTED)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.NOT_PROJECT_MEMBER, "해당 프로젝트의 상세 정보를 조회할 권한이 없습니다."));

        return ProjectResponseDto.fromEntity(project);
    }

    @Override
    public ProjectResponseDto updateProjectDetails(Long projectId, ProjectUpdateRequestDto requestDto, User currentUser) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.PROJECT_NOT_FOUND));

        boolean canUpdate = currentUser.equals(project.getCreatedBy()) || isUserProjectAdmin(currentUser, project);
        if (!canUpdate) {
            throw new BusinessLogicException(ErrorCode.ACCESS_DENIED, "프로젝트 정보를 수정할 권한이 없습니다.");
        }

        String newTitle = requestDto.getTitle(); // title은 @NotBlank이므로 항상 존재
        String newDescription = requestDto.getDescription() != null ? requestDto.getDescription() : project.getDescription();

        project.update(newTitle, newDescription);
        Project updatedProject = projectRepository.save(project);

        log.info("프로젝트 정보 수정 완료: ID {}, 새 제목: {}, 수행자: {}",
                updatedProject.getIdx(), updatedProject.getTitle(), currentUser.getNickname());

        return ProjectResponseDto.fromEntity(updatedProject);
    }

    @Override
    public void inviteUserToProject(Long projectId, ProjectInviteRequestDto inviteDto, User inviter) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.PROJECT_NOT_FOUND));

        if (!isUserProjectAdmin(inviter, project)) {
            throw new BusinessLogicException(ErrorCode.NO_AUTHORITY_TO_INVITE);
        }

        User invitee = userRepository.findById(inviteDto.getInviteeUserId())
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.USER_NOT_FOUND, "초대할 사용자를 찾을 수 없습니다."));

        Optional<ProjectMember> existingMembershipOpt = projectMemberRepository.findByUserAndProject(invitee, project);

        if (existingMembershipOpt.isPresent()) {
            ProjectMember existingMembership = existingMembershipOpt.get();
            if (existingMembership.getStatus() == ProjectMemberStatus.ACCEPTED) {
                throw new BusinessLogicException(ErrorCode.ALREADY_PROJECT_MEMBER);
            } else if (existingMembership.getStatus() == ProjectMemberStatus.PENDING) {
                throw new BusinessLogicException(ErrorCode.PROJECT_INVITATION_PENDING);
            } else { // REJECTED 등 다른 상태일 경우, 기존 기록을 지우고 새로 초대
                projectMemberRepository.delete(existingMembership);
                log.info("기존 거절된 초대 기록 삭제 후 재초대: projectId={}, inviteeId={}", projectId, invitee.getIdx());
            }
        }

        // ===== 수정된 부분 시작: PENDING 상태로 ProjectMember 레코드 생성 =====
        ProjectMember newInvitation = ProjectMember.builder()
                .project(project)
                .user(invitee)
                .role(ProjectMemberRole.일반사용자) // 초대 시 기본 역할
                .status(ProjectMemberStatus.PENDING)  // 상태를 PENDING으로 설정
                .build();
        projectMemberRepository.save(newInvitation);
        // ===== 수정된 부분 끝 =====

        log.info("사용자 {}가 프로젝트 {}에 사용자 {}를 초대 (상태: PENDING)", inviter.getNickname(), projectId, invitee.getNickname());

        // 알림 생성 호출 (기존과 동일)
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
                .filter(pm -> pm.getStatus() == ProjectMemberStatus.ACCEPTED) // 수락된 멤버만 조회
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
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.USER_NOT_FOUND, "역할을 변경할 멤버를 찾을 수 없습니다."));

        // 역할 변경 권한 확인 (예: 프로젝트 관리자만 가능)
        if (!isUserProjectAdmin(currentUser, project)) {
            throw new BusinessLogicException(ErrorCode.NO_AUTHORITY_TO_MANAGE_MEMBERS);
        }

        // 자기 자신의 역할 변경 또는 마지막 관리자 관련 로직 (ProjectServiceImpl의 기존 로직 참조)
        if (currentUser.getIdx().equals(memberUserId) && newRole != ProjectMemberRole.관리자) {
            // 관리자가 자신의 역할을 일반 사용자로 강등하려는 경우, 마지막 관리자인지 확인
            long adminCount = projectMemberRepository.findByProject(project).stream()
                    .filter(pm -> pm.getRole() == ProjectMemberRole.관리자 && pm.getStatus() == ProjectMemberStatus.ACCEPTED)
                    .count();
            if (adminCount <= 1) {
                throw new BusinessLogicException(ErrorCode.CANNOT_REMOVE_LAST_ADMIN, "프로젝트의 마지막 관리자는 자신의 역할을 변경할 수 없습니다.");
            }
        }
        if (currentUser.getIdx().equals(memberUserId) && project.getCreatedBy().equals(currentUser) && newRole != ProjectMemberRole.관리자) {
            // 프로젝트 생성자가 자신의 역할을 관리자 외의 것으로 바꾸려 하고, 다른 관리자가 없다면 방지
            // (위의 로직과 유사하나, 생성자 특화 조건이 있다면 추가)
        }


        ProjectMember targetMembership = projectMemberRepository.findByUserAndProject(memberToUpdateUserEntity, project)
                .filter(pm -> pm.getStatus() == ProjectMemberStatus.ACCEPTED)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.TARGET_USER_NOT_PROJECT_MEMBER));

        targetMembership.updateRole(newRole);
        projectMemberRepository.save(targetMembership); // 명시적 저장 (Transactional에 의해 자동 변경 감지되지만 명시)
        log.info("프로젝트 {}의 멤버 {} 역할을 {}로 변경 (수행자: {})", projectId, memberUserId, newRole, currentUser.getNickname());
    }

    @Override
    public void removeProjectMember(Long projectId, Long memberUserId, User currentUser) {
        // 1. 필요한 엔티티 조회
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.PROJECT_NOT_FOUND));

        User memberToRemoveUserEntity = userRepository.findById(memberUserId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.USER_NOT_FOUND, "제외할 멤버를 찾을 수 없습니다."));

        // 2. 권한 확인 (프로젝트 관리자인지)
        if (!isUserProjectAdmin(currentUser, project)) { // isUserProjectAdmin 헬퍼 메소드 사용
            throw new BusinessLogicException(ErrorCode.NO_AUTHORITY_TO_MANAGE_MEMBERS, "멤버를 제외할 권한이 없습니다.");
        }

        // 3. 비즈니스 규칙 확인 (자신을 제명하거나 마지막 관리자를 제명하는 것을 방지)
        if (currentUser.getIdx().equals(memberUserId)) {
            throw new BusinessLogicException(ErrorCode.CANNOT_MODIFY_OWN_ROLE_OR_REMOVE_ONESELF_AS_ADMIN, "관리자는 자신을 프로젝트에서 제외할 수 없습니다.");
        }

        ProjectMember targetMembership = projectMemberRepository.findByUserAndProject(memberToRemoveUserEntity, project)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.TARGET_USER_NOT_PROJECT_MEMBER));

        // 마지막 관리자인지 확인
        if (targetMembership.getRole() == ProjectMemberRole.관리자 && targetMembership.getStatus() == ProjectMemberStatus.ACCEPTED) {
            long adminCount = projectMemberRepository.findByProject(project).stream()
                    .filter(pm -> pm.getRole() == ProjectMemberRole.관리자 && pm.getStatus() == ProjectMemberStatus.ACCEPTED)
                    .count();
            if (adminCount <= 1) {
                throw new BusinessLogicException(ErrorCode.CANNOT_REMOVE_LAST_ADMIN);
            }
        }

        // 4. (핵심 수정) 해당 프로젝트 내에서 사용자의 모든 업무 할당(TaskMember) 기록 삭제
        taskMemberRepository.deleteAllByUserAndProject(memberToRemoveUserEntity, project);
        log.info("프로젝트 ID {}에서 사용자 '{}'의 모든 업무 할당을 해제했습니다.", projectId, memberToRemoveUserEntity.getNickname());

        // 5. 프로젝트 멤버십(ProjectMember) 기록 삭제
        projectMemberRepository.delete(targetMembership);
        log.info("프로젝트 ID {}에서 멤버 ID {}를 제외했습니다. (수행자: {})", projectId, memberUserId, currentUser.getNickname());

        // 6. 히스토리 기록
        String historyDescription = String.format("'%s'님이 '%s'님을 프로젝트에서 제외했습니다.",
                currentUser.getNickname(), memberToRemoveUserEntity.getNickname());
        historyService.recordHistory(project, currentUser, HistoryActionType.멤버제거, historyDescription, memberToRemoveUserEntity.getIdx());

        // 7. 알림 전송 (제외된 당사자에게)
        String notificationContent = String.format("'%s' 프로젝트에서 제외되었습니다.",
                project.getTitle());
        notificationService.createAndSendNotification(
                memberToRemoveUserEntity,
                NotificationType.PROJECT_MEMBER_LEFT,
                notificationContent,
                project.getIdx(),
                "PROJECT"
        );
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
        projectMemberRepository.save(membership); // 상태 변경 후 저장
        log.info("사용자 {}가 프로젝트 '{}'({}) 초대를 수락했습니다.", currentUser.getNickname(), project.getTitle(), projectId);

        // ===== 히스토리 기록 로직 추가 시작 =====
        String historyDescription = String.format("'%s'님이 프로젝트에 참여했습니다.", currentUser.getNickname());
        historyService.recordHistory(project, currentUser, HistoryActionType.멤버추가, historyDescription, currentUser.getIdx());
        // ===== 히스토리 기록 로직 추가 끝 =====

        // ===== 알림 기록 로직 추가 시작 =====
        // 프로젝트에 참여한 다른 모든 멤버들에게 새 멤버 참여 알림 전송 (선택적)
        // 여기서는 프로젝트 생성자에게만 알림을 보내는 것으로 단순화합니다.
        // (누가 초대를 보냈는지에 대한 정보가 없기 때문)
        User projectCreator = project.getCreatedBy();
        if (!projectCreator.equals(currentUser)) { // 본인이 생성한 프로젝트가 아닐 경우에만 알림
            String notificationContent = String.format("'%s'님이 '%s' 프로젝트 초대를 수락했습니다.",
                    currentUser.getNickname(), project.getTitle());
            notificationService.createAndSendNotification(
                    projectCreator,
                    NotificationType.PROJECT_INVITE_ACCEPTED, // 보낸 초대가 수락되었다는 알림
                    notificationContent,
                    currentUser.getIdx(), // 참조 ID: 수락한 사람
                    "USER"
            );
        }
        // ===== 알림 기록 로직 추가 끝 =====
    }

    @Override
    public void rejectProjectInvitation(Long projectId, User currentUser) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.PROJECT_NOT_FOUND));

        ProjectMember membership = projectMemberRepository.findByUserAndProject(currentUser, project)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.NO_PENDING_INVITATION, "해당 프로젝트에 대한 초대 정보를 찾을 수 없습니다."));

        // 이미 처리된 초대인지 확인합니다.
        if (membership.getStatus() != ProjectMemberStatus.PENDING) {
            throw new BusinessLogicException(ErrorCode.INVITATION_ALREADY_PROCESSED);
        }

        // 1. 엔티티의 상태를 PENDING에서 REJECTED로 변경합니다.
        membership.rejectInvitation();

        // 2. 변경된 상태를 데이터베이스에 저장(업데이트)합니다.
        projectMemberRepository.save(membership);
        // ---------------------------------

        log.info("사용자 {}가 프로젝트 '{}'({}) 초대를 거절했습니다. (상태: REJECTED)", currentUser.getNickname(), project.getTitle(), projectId);
    }

    /**
     * 현재 사용자가 받은 보류 중인 초대 목록을 조회하는 기능 구현 (새로 추가)
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponseDto> getPendingInvitations(User currentUser) {
        log.info("사용자 {}의 보류 중인 초대 목록 조회", currentUser.getNickname());

        // 새로 추가한 findByUserAndStatus 메소드 사용
        List<ProjectMember> pendingMemberships = projectMemberRepository.findByUserAndStatus(currentUser, ProjectMemberStatus.PENDING);

        // ProjectMember 목록에서 Project 목록을 추출하여 DTO로 변환
        return pendingMemberships.stream()
                .map(ProjectMember::getProject) // 각 멤버십에서 프로젝트 정보를 가져옴
                .map(ProjectResponseDto::fromEntity) // Project 엔티티를 ProjectResponseDto로 변환
                .collect(Collectors.toList());
    }

}