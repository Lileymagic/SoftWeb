package com.example.softengineerwebpr.domain.project.controller;

import com.example.softengineerwebpr.common.dto.ApiResponse;
import com.example.softengineerwebpr.domain.project.dto.*; // ProjectMemberRoleUpdateRequestDto 포함
import com.example.softengineerwebpr.domain.project.entity.ProjectMemberRole;
import com.example.softengineerwebpr.domain.project.service.ProjectService;
import com.example.softengineerwebpr.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    // (createProject, getMyProjects, inviteUserToProject, getProjectMembers, updateProjectMemberRole, removeProjectMember 엔드포인트는 이전과 동일하므로 생략)
    // ... (이전 답변의 해당 엔드포인트들 전체 코드) ...
    @PostMapping
    public ResponseEntity<ApiResponse<ProjectResponseDto>> createProject(
            @Valid @RequestBody ProjectCreateRequestDto requestDto,
            @AuthenticationPrincipal User currentUser) {
        log.info("API 호출: 새 프로젝트 생성 - title: {}", requestDto.getTitle());
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.success(HttpStatus.UNAUTHORIZED.value(), "인증되지 않은 사용자입니다.", null));
        }
        ProjectResponseDto createdProject = projectService.createProject(requestDto, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "프로젝트가 성공적으로 생성되었습니다.", createdProject));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProjectResponseDto>>> getMyProjects(
            @AuthenticationPrincipal User currentUser) {
        log.info("API 호출: 내 프로젝트 목록 조회");
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.success(HttpStatus.UNAUTHORIZED.value(), "인증되지 않은 사용자입니다.", null));
        }
        List<ProjectResponseDto> myProjects = projectService.getMyProjects(currentUser);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "내 프로젝트 목록 조회가 성공했습니다.", myProjects));
    }

    @PostMapping("/{projectId}/members")
    public ResponseEntity<ApiResponse<Void>> inviteUserToProject(
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectInviteRequestDto inviteDto,
            @AuthenticationPrincipal User currentUser) {
        log.info("API 호출: 프로젝트 {}에 사용자 {} 초대", projectId, inviteDto.getInviteeUserId());
        projectService.inviteUserToProject(projectId, inviteDto, currentUser);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "프로젝트 초대 요청을 성공적으로 보냈습니다.", null));
    }

    @GetMapping("/{projectId}/members")
    public ResponseEntity<ApiResponse<List<ProjectMemberResponseDto>>> getProjectMembers(
            @PathVariable Long projectId,
            @AuthenticationPrincipal User currentUser) {
        log.info("API 호출: 프로젝트 {} 멤버 목록 조회", projectId);
        List<ProjectMemberResponseDto> members = projectService.getProjectMembers(projectId, currentUser);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "프로젝트 멤버 목록 조회가 성공했습니다.", members));
    }

    @PatchMapping("/{projectId}/members/{memberUserId}/role")
    public ResponseEntity<ApiResponse<Void>> updateProjectMemberRole(
            @PathVariable Long projectId,
            @PathVariable Long memberUserId,
            @Valid @RequestBody ProjectMemberRoleUpdateRequestDto roleUpdateRequestDto,
            @AuthenticationPrincipal User currentUser) {
        log.info("API 호출: 프로젝트 {}의 멤버 {} 역할 변경 시도 -> {}", projectId, memberUserId, roleUpdateRequestDto.getRole());
        projectService.updateProjectMemberRole(projectId, memberUserId, roleUpdateRequestDto.getRole(), currentUser);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "프로젝트 멤버 역할이 성공적으로 변경되었습니다.", null));
    }

    @DeleteMapping("/{projectId}/members/{memberUserId}")
    public ResponseEntity<ApiResponse<Void>> removeProjectMember(
            @PathVariable Long projectId,
            @PathVariable Long memberUserId,
            @AuthenticationPrincipal User currentUser) {
        log.info("API 호출: 프로젝트 {}에서 멤버 {} 제외 시도", projectId, memberUserId);
        projectService.removeProjectMember(projectId, memberUserId, currentUser);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "프로젝트에서 멤버가 성공적으로 제외되었습니다.", null));
    }

    // --- 초대 수락/거절 엔드포인트 추가 ---
    @PostMapping("/{projectId}/invitations/accept")
    public ResponseEntity<ApiResponse<Void>> acceptProjectInvitation(
            @PathVariable Long projectId,
            @AuthenticationPrincipal User currentUser) {
        log.info("API 호출: 사용자 {}가 프로젝트 {} 초대 수락", currentUser.getNickname(), projectId);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.success(HttpStatus.UNAUTHORIZED.value(), "인증되지 않은 사용자입니다.", null));
        }
        projectService.acceptProjectInvitation(projectId, currentUser);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "프로젝트 초대를 수락했습니다.", null));
    }

    @PostMapping("/{projectId}/invitations/reject")
    public ResponseEntity<ApiResponse<Void>> rejectProjectInvitation(
            @PathVariable Long projectId,
            @AuthenticationPrincipal User currentUser) {
        log.info("API 호출: 사용자 {}가 프로젝트 {} 초대 거절", currentUser.getNickname(), projectId);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.success(HttpStatus.UNAUTHORIZED.value(), "인증되지 않은 사용자입니다.", null));
        }
        projectService.rejectProjectInvitation(projectId, currentUser);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "프로젝트 초대를 거절했습니다.", null));
    }
}