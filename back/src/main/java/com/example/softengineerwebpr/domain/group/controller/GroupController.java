package com.example.softengineerwebpr.domain.group.controller;

import com.example.softengineerwebpr.common.dto.ApiResponse;
import com.example.softengineerwebpr.common.util.AuthenticationUtil;
import com.example.softengineerwebpr.domain.group.dto.*;
import com.example.softengineerwebpr.domain.group.service.GroupService;
import com.example.softengineerwebpr.domain.user.dto.GroupMemberDisplayDto; // 새로운 DTO 임포트
import com.example.softengineerwebpr.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api") // API 공통 기본 경로
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;
    private final AuthenticationUtil authenticationUtil;

    /**
     * 특정 프로젝트에 새 그룹 생성
     */
    @PostMapping("/projects/{projectId}/groups")
    public ResponseEntity<ApiResponse<GroupResponseDto>> createGroup(
            @PathVariable Long projectId,
            @Valid @RequestBody GroupCreateRequestDto requestDto,
            Authentication authentication) {
        User currentUser = authenticationUtil.getCurrentUser(authentication);
        GroupResponseDto createdGroup = groupService.createGroup(projectId, requestDto, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "그룹이 성공적으로 생성되었습니다.", createdGroup));
    }

    /**
     * 특정 프로젝트의 모든 그룹 목록 조회
     */
    @GetMapping("/projects/{projectId}/groups")
    public ResponseEntity<ApiResponse<List<GroupResponseDto>>> getGroupsByProject(
            @PathVariable Long projectId,
            Authentication authentication) {
        User currentUser = authenticationUtil.getCurrentUser(authentication);
        List<GroupResponseDto> groups = groupService.getGroupsByProject(projectId, currentUser);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "프로젝트 그룹 목록 조회 성공", groups));
    }

    /**
     * 특정 그룹 상세 정보 조회
     */
    @GetMapping("/groups/{groupId}")
    public ResponseEntity<ApiResponse<GroupResponseDto>> getGroupDetails(
            @PathVariable Long groupId,
            Authentication authentication) {
        User currentUser = authenticationUtil.getCurrentUser(authentication);
        GroupResponseDto groupDetails = groupService.getGroupDetails(groupId, currentUser);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "그룹 상세 정보 조회 성공", groupDetails));
    }

    /**
     * 특정 그룹 정보 수정 (이름, 색상)
     */
    @PutMapping("/groups/{groupId}")
    public ResponseEntity<ApiResponse<GroupResponseDto>> updateGroupDetails(
            @PathVariable Long groupId,
            @Valid @RequestBody GroupUpdateRequestDto requestDto,
            Authentication authentication) {
        User currentUser = authenticationUtil.getCurrentUser(authentication);
        GroupResponseDto updatedGroup = groupService.updateGroupDetails(groupId, requestDto, currentUser);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "그룹 정보가 성공적으로 수정되었습니다.", updatedGroup));
    }

    /**
     * 특정 그룹 삭제
     */
    @DeleteMapping("/groups/{groupId}")
    public ResponseEntity<ApiResponse<Void>> deleteGroup(
            @PathVariable Long groupId,
            Authentication authentication) {
        User currentUser = authenticationUtil.getCurrentUser(authentication);
        groupService.deleteGroup(groupId, currentUser);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "그룹이 성공적으로 삭제되었습니다."));
    }

    /**
     * 특정 그룹의 멤버 목록 조회
     * 반환 DTO를 GroupMemberDisplayDto로 변경
     */
    @GetMapping("/groups/{groupId}/members")
    public ResponseEntity<ApiResponse<List<GroupMemberDisplayDto>>> getGroupMembers( // 반환 타입 변경
                                                                                     @PathVariable Long groupId,
                                                                                     Authentication authentication) {
        User currentUser = authenticationUtil.getCurrentUser(authentication);
        List<GroupMemberDisplayDto> members = groupService.getGroupMembers(groupId, currentUser); // 서비스 메소드 반환 타입과 일치
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "그룹 멤버 목록 조회 성공", members));
    }

    /**
     * 특정 그룹의 멤버 목록을 동기화합니다. (전체 멤버 목록을 한 번에 설정)
     */
    @PutMapping("/groups/{groupId}/members")
    public ResponseEntity<ApiResponse<Void>> syncGroupMembers(
            @PathVariable Long groupId,
            @Valid @RequestBody GroupMemberSyncRequestDto requestDto,
            Authentication authentication) {
        User currentUser = authenticationUtil.getCurrentUser(authentication);
        groupService.syncGroupMembers(groupId, requestDto, currentUser);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "그룹 멤버가 성공적으로 동기화되었습니다."));
    }


    /**
     * 특정 그룹의 권한 수정
     */
    @PutMapping("/groups/{groupId}/permissions")
    public ResponseEntity<ApiResponse<GroupResponseDto>> updateGroupPermissions(
            @PathVariable Long groupId,
            @Valid @RequestBody GroupPermissionUpdateRequestDto requestDto,
            Authentication authentication) {
        User currentUser = authenticationUtil.getCurrentUser(authentication);
        GroupResponseDto updatedGroup = groupService.updateGroupPermissions(groupId, requestDto, currentUser);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "그룹 권한이 성공적으로 수정되었습니다.", updatedGroup));
    }
}