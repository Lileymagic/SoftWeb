package com.example.softengineerwebpr.domain.group.service.impl;

import com.example.softengineerwebpr.common.exception.BusinessLogicException;
import com.example.softengineerwebpr.common.exception.ErrorCode;
import com.example.softengineerwebpr.domain.group.dto.*;
import com.example.softengineerwebpr.domain.group.entity.Group;
import com.example.softengineerwebpr.domain.group.entity.GroupMember;
import com.example.softengineerwebpr.domain.group.repository.GroupMemberRepository;
import com.example.softengineerwebpr.domain.group.repository.GroupRepository;
import com.example.softengineerwebpr.domain.group.service.GroupService;
import com.example.softengineerwebpr.domain.project.entity.Project;
import com.example.softengineerwebpr.domain.project.entity.ProjectMemberRole;
import com.example.softengineerwebpr.domain.project.entity.ProjectMemberStatus;
import com.example.softengineerwebpr.domain.project.repository.ProjectMemberRepository;
import com.example.softengineerwebpr.domain.project.repository.ProjectRepository;
import com.example.softengineerwebpr.domain.user.dto.GroupMemberDisplayDto; // 수정된 DTO 임포트
import com.example.softengineerwebpr.domain.user.entity.User;
import com.example.softengineerwebpr.domain.user.repository.UserRepository;
// import com.example.softengineerwebpr.domain.notification.service.NotificationService; // 필요시 주석 해제
// import com.example.softengineerwebpr.domain.auth.repository.UserCredentialRepository; // getGroupMembers에서 더 이상 사용하지 않으면 제거 가능, 다른 곳에서 사용하면 유지 및 주입 확인

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor // final 필드에 대한 생성자 자동 생성
@Transactional
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMemberRepository projectMemberRepository;
    // private final NotificationService notificationService; // 필요시 주석 해제

    // --- Helper Methods ---
    private Project findProjectOrThrow(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.PROJECT_NOT_FOUND));
    }

    private Group findGroupOrThrow(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.RESOURCE_NOT_FOUND, "그룹을 찾을 수 없습니다. ID: " + groupId));
    }

    private User findUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.USER_NOT_FOUND));
    }

    private void checkProjectMembership(Project project, User user) {
        if (!projectMemberRepository.findByUserAndProject(user, project)
                .filter(pm -> pm.getStatus() == ProjectMemberStatus.ACCEPTED)
                .isPresent()) {
            throw new BusinessLogicException(ErrorCode.NOT_PROJECT_MEMBER, "해당 프로젝트의 멤버가 아닙니다.");
        }
    }

    private boolean isUserProjectAdmin(User user, Project project) {
        return projectMemberRepository.findByUserAndProject(user, project)
                .filter(pm -> pm.getStatus() == ProjectMemberStatus.ACCEPTED)
                .map(pm -> pm.getRole() == ProjectMemberRole.관리자)
                .orElse(false);
    }

    private void checkGroupManagementPermission(Project project, User user) {
        if (!isUserProjectAdmin(user, project)) {
            throw new BusinessLogicException(ErrorCode.ACCESS_DENIED, "프로젝트 그룹을 관리할 권한이 없습니다.");
        }
    }

    // --- Service Methods ---

    @Override
    public GroupResponseDto createGroup(Long projectId, GroupCreateRequestDto requestDto, User currentUser) {
        Project project = findProjectOrThrow(projectId);
        checkGroupManagementPermission(project, currentUser);

        Group group = Group.builder()
                .project(project)
                .name(requestDto.getName())
                .color(requestDto.getColor())
                .permission(requestDto.getPermissionBitmask() != null ? requestDto.getPermissionBitmask() : 0)
                .build();

        Group savedGroup = groupRepository.save(group);
        log.info("새 그룹 생성: '{}', 프로젝트 ID: {}, 생성자: {}", savedGroup.getName(), projectId, currentUser.getNickname());
        return GroupResponseDto.fromEntity(savedGroup);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupResponseDto> getGroupsByProject(Long projectId, User currentUser) {
        Project project = findProjectOrThrow(projectId);
        checkProjectMembership(project, currentUser);

        // GroupRepository에 findByProject(Project project) 메소드가 있다고 가정합니다.
        // 없다면 groupRepository.findAll().stream().filter(g -> g.getProject().equals(project))... 로 처리합니다.
        // 명확성을 위해 GroupRepository에 List<Group> findByProject(Project project); 추가하는 것을 권장합니다.
        List<Group> groups = groupRepository.findAll().stream()
                .filter(group -> group.getProject().getIdx().equals(projectId)) // projectId로 필터링
                .collect(Collectors.toList());

        return groups.stream()
                .map(GroupResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public GroupResponseDto getGroupDetails(Long groupId, User currentUser) {
        Group group = findGroupOrThrow(groupId);
        checkProjectMembership(group.getProject(), currentUser);
        return GroupResponseDto.fromEntity(group);
    }

    @Override
    public GroupResponseDto updateGroupDetails(Long groupId, GroupUpdateRequestDto requestDto, User currentUser) {
        Group group = findGroupOrThrow(groupId);
        checkGroupManagementPermission(group.getProject(), currentUser);

        // Group 엔티티에 이름과 색상을 업데이트하는 메소드가 있다고 가정 (예: group.updateDetails(name, color))
        // 또는 개별 setter 사용 후 save
        group.updateDetails( // Group 엔티티의 updateDetails 메소드 사용
                requestDto.getName() != null ? requestDto.getName() : group.getName(),
                requestDto.getColor() // null이 올 수 있음 (색상 제거 등). Group 엔티티에서 null 처리 필요.
        );
        Group updatedGroup = groupRepository.save(group);
        log.info("그룹 정보 수정: ID {}, 새 이름: '{}', 수행자: {}", groupId, updatedGroup.getName(), currentUser.getNickname());
        return GroupResponseDto.fromEntity(updatedGroup);
    }

    @Override
    public void deleteGroup(Long groupId, User currentUser) {
        Group group = findGroupOrThrow(groupId);
        checkGroupManagementPermission(group.getProject(), currentUser);
        groupRepository.delete(group);
        log.info("그룹 삭제: ID {}, 이름: '{}', 수행자: {}", groupId, group.getName(), currentUser.getNickname());
    }

    /**
     * GroupServiceImpl.java의 getGroupMembers 메소드 수정
     * UserCredentialRepository에 대한 의존성 없이 GroupMemberDisplayDto를 반환합니다.
     */
    @Override
    @Transactional(readOnly = true)
    public List<GroupMemberDisplayDto> getGroupMembers(Long groupId, User currentUser) {
        Group group = findGroupOrThrow(groupId);
        checkProjectMembership(group.getProject(), currentUser);

        List<GroupMember> groupMembers = groupMemberRepository.findByGroup(group);

        return groupMembers.stream()
                .map(GroupMember::getUser) // GroupMember에서 User 엔티티를 가져옴
                .filter(java.util.Objects::nonNull) // User가 null이 아닌 경우만 처리
                .map(GroupMemberDisplayDto::fromEntity) // User 엔티티로 GroupMemberDisplayDto 생성
                .collect(Collectors.toList());
    }

    @Override
    public void addMembersToGroup(Long groupId, GroupMemberManagementRequestDto requestDto, User currentUser) {
        Group group = findGroupOrThrow(groupId);
        Project project = group.getProject();
        checkGroupManagementPermission(project, currentUser);

        List<User> usersToAdd = userRepository.findAllById(requestDto.getUserIds());
        if (usersToAdd.size() != requestDto.getUserIds().size()) {
            log.warn("그룹 멤버 추가: 요청된 사용자 ID 중 일부를 찾을 수 없습니다. GroupId: {}", groupId);
            // 모든 사용자를 찾을 수 없는 경우 예외를 던지거나, 찾은 사용자만 추가하는 정책을 선택할 수 있습니다.
            // 여기서는 찾은 사용자만 처리합니다.
        }

        for (User user : usersToAdd) {
            checkProjectMembership(project, user); // 추가하려는 사용자가 프로젝트 멤버인지 확인
            if (!groupMemberRepository.findByUserAndGroup(user, group).isPresent()) {
                GroupMember groupMember = GroupMember.builder().user(user).group(group).build();
                groupMemberRepository.save(groupMember);
                log.info("그룹 '{}'에 사용자 '{}' 추가 완료", group.getName(), user.getNickname());
            } else {
                log.warn("사용자 '{}'는 이미 그룹 '{}'의 멤버입니다.", user.getNickname(), group.getName());
            }
        }
    }

    @Override
    public void removeMembersFromGroup(Long groupId, GroupMemberManagementRequestDto requestDto, User currentUser) {
        Group group = findGroupOrThrow(groupId);
        checkGroupManagementPermission(group.getProject(), currentUser);

        List<User> usersToRemove = userRepository.findAllById(requestDto.getUserIds());
        // 찾을 수 없는 사용자가 있어도, 있는 사용자만 처리

        for (User user : usersToRemove) {
            groupMemberRepository.findByUserAndGroup(user, group).ifPresent(groupMember -> {
                groupMemberRepository.delete(groupMember);
                log.info("그룹 '{}'에서 사용자 '{}' 제외 완료", group.getName(), user.getNickname());
            });
        }
    }


    @Override
    public GroupResponseDto updateGroupPermissions(Long groupId, GroupPermissionUpdateRequestDto requestDto, User currentUser) {
        Group group = findGroupOrThrow(groupId);
        checkGroupManagementPermission(group.getProject(), currentUser);

        group.setPermission(requestDto.getPermissionBitmask()); // Group 엔티티에 setPermission 메소드 필요
        Group updatedGroup = groupRepository.save(group);
        log.info("그룹 '{}' 권한 변경: 새 권한 값 {}, 수행자: {}", group.getName(), requestDto.getPermissionBitmask(), currentUser.getNickname());
        return GroupResponseDto.fromEntity(updatedGroup);
    }

    @Override
    public void syncGroupMembers(Long groupId, GroupMemberSyncRequestDto requestDto, User currentUser) {
        Group group = findGroupOrThrow(groupId);
        Project project = group.getProject();
        checkGroupManagementPermission(project, currentUser);

        List<User> newMemberList = userRepository.findAllById(requestDto.getUserIds());
        if (newMemberList.size() != requestDto.getUserIds().size()) {
            log.warn("그룹 멤버 동기화: 요청된 사용자 ID 중 일부를 찾을 수 없습니다. GroupId: {}", groupId);
        }

        for (User userToAssign : newMemberList) {
            checkProjectMembership(project, userToAssign);
        }

        Set<User> currentMembers = groupMemberRepository.findByGroup(group).stream()
                .map(GroupMember::getUser)
                .collect(Collectors.toSet());
        Set<User> requestedMembers = new HashSet<>(newMemberList);

        // 제거할 멤버 (현재 O, 요청 X)
        currentMembers.stream()
                .filter(user -> !requestedMembers.contains(user))
                .forEach(user -> groupMemberRepository.findByUserAndGroup(user, group)
                        .ifPresent(groupMemberRepository::delete));

        // 추가할 멤버 (현재 X, 요청 O)
        requestedMembers.stream()
                .filter(user -> !currentMembers.contains(user))
                .forEach(user -> {
                    GroupMember newGroupMember = GroupMember.builder()
                            .group(group)
                            .user(user)
                            .build();
                    groupMemberRepository.save(newGroupMember);
                });
        log.info("그룹 '{}' 멤버 동기화 완료. 최종 멤버 수: {}", group.getName(), requestedMembers.size());
    }
}