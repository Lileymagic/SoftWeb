package com.example.softengineerwebpr.domain.group.service;

import com.example.softengineerwebpr.domain.group.dto.*; // 이전 단계에서 생성한 DTO들
import com.example.softengineerwebpr.domain.user.dto.GroupMemberDisplayDto;
import com.example.softengineerwebpr.domain.user.entity.User;

import java.util.List;

public interface GroupService {

    /**
     * 특정 프로젝트에 새로운 그룹을 생성합니다.
     * @param projectId 그룹을 생성할 프로젝트의 ID
     * @param requestDto 그룹 생성 요청 데이터 (이름, 색상, 초기 권한 등)
     * @param currentUser 현재 작업을 수행하는 사용자 (권한 검사)
     * @return 생성된 그룹 정보를 담은 GroupResponseDto
     */
    GroupResponseDto createGroup(Long projectId, GroupCreateRequestDto requestDto, User currentUser);

    /**
     * 특정 프로젝트에 속한 모든 그룹 목록을 조회합니다.
     * @param projectId 그룹 목록을 조회할 프로젝트의 ID
     * @param currentUser 현재 작업을 수행하는 사용자 (프로젝트 접근 권한 검사)
     * @return 해당 프로젝트의 그룹 목록 (GroupResponseDto 리스트)
     */
    List<GroupResponseDto> getGroupsByProject(Long projectId, User currentUser);

    /**
     * 특정 그룹의 상세 정보를 조회합니다. (멤버 목록 제외)
     * @param groupId 조회할 그룹의 ID
     * @param currentUser 현재 작업을 수행하는 사용자 (그룹 접근 권한 검사)
     * @return 그룹 상세 정보를 담은 GroupResponseDto
     */
    GroupResponseDto getGroupDetails(Long groupId, User currentUser);

    /**
     * 특정 그룹의 정보(이름, 색상)를 수정합니다.
     * @param groupId 수정할 그룹의 ID
     * @param requestDto 그룹 수정 요청 데이터
     * @param currentUser 현재 작업을 수행하는 사용자 (그룹 수정 권한 검사)
     * @return 수정된 그룹 정보를 담은 GroupResponseDto
     */
    GroupResponseDto updateGroupDetails(Long groupId, GroupUpdateRequestDto requestDto, User currentUser);

    /**
     * 특정 그룹을 삭제합니다.
     * @param groupId 삭제할 그룹의 ID
     * @param currentUser 현재 작업을 수행하는 사용자 (그룹 삭제 권한 검사)
     */
    void deleteGroup(Long groupId, User currentUser);

    /**
     * 특정 그룹에 여러 사용자를 멤버로 추가합니다.
     * @param groupId 멤버를 추가할 그룹의 ID
     * @param requestDto 추가할 사용자들의 ID 목록을 담은 DTO
     * @param currentUser 현재 작업을 수행하는 사용자 (그룹 멤버 관리 권한 검사)
     */
    void addMembersToGroup(Long groupId, GroupMemberManagementRequestDto requestDto, User currentUser);

    /**
     * 특정 그룹에서 여러 사용자를 멤버에서 제외합니다.
     * @param groupId 멤버를 제외할 그룹의 ID
     * @param requestDto 제외할 사용자들의 ID 목록을 담은 DTO
     * @param currentUser 현재 작업을 수행하는 사용자 (그룹 멤버 관리 권한 검사)
     */
    void removeMembersFromGroup(Long groupId, GroupMemberManagementRequestDto requestDto, User currentUser);

    /**
     * 특정 그룹에 속한 멤버 목록을 조회합니다.
     * @param groupId 멤버 목록을 조회할 그룹의 ID
     * @param currentUser 현재 작업을 수행하는 사용자 (권한 검사)
     * @return 해당 그룹의 멤버 간략 정보 목록 (GroupMemberDisplayDto 리스트) // 반환 타입 변경
     */
    List<GroupMemberDisplayDto> getGroupMembers(Long groupId, User currentUser); // 반환 타입 변경

    /**
     * 특정 그룹의 권한을 수정합니다.
     * @param groupId 권한을 수정할 그룹의 ID
     * @param requestDto 새로운 권한 비트마스크 정보를 담은 DTO
     * @param currentUser 현재 작업을 수행하는 사용자 (그룹 권한 수정 권한 검사)
     * @return 수정된 그룹 정보를 담은 GroupResponseDto
     */
    GroupResponseDto updateGroupPermissions(Long groupId, GroupPermissionUpdateRequestDto requestDto, User currentUser);

    /**
            * 특정 그룹의 멤버 목록을 요청된 사용자 ID 목록과 동기화합니다.
     * (기존 멤버 중 목록에 없으면 제거, 목록에 있으나 기존 멤버가 아니면 추가)
            * @param groupId 동기화할 그룹의 ID
     * @param requestDto 동기화할 전체 사용자 ID 목록을 담은 DTO
     * @param currentUser 현재 작업을 수행하는 사용자 (권한 검사)
     */
    void syncGroupMembers(Long groupId, GroupMemberSyncRequestDto requestDto, User currentUser);
}