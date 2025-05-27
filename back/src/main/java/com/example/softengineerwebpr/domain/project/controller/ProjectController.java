package com.example.softengineerwebpr.domain.project.controller;

import com.example.softengineerwebpr.common.dto.ApiResponse;
import com.example.softengineerwebpr.common.dto.ErrorResponse;
import com.example.softengineerwebpr.domain.auth.entity.UserCredential;
import com.example.softengineerwebpr.domain.auth.repository.UserCredentialRepository;
import com.example.softengineerwebpr.domain.project.dto.*;
import com.example.softengineerwebpr.domain.project.entity.ProjectMemberRole;
import com.example.softengineerwebpr.domain.project.service.ProjectService;
import com.example.softengineerwebpr.domain.user.entity.User;
import com.example.softengineerwebpr.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final UserRepository userRepository;
    private final UserCredentialRepository userCredentialRepository;

    private User getCurrentDomainUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        User domainUser = null;

        if (principal instanceof OAuth2User oauth2User) {
            Object userIdObj = oauth2User.getAttribute("id");
            if (userIdObj instanceof Number) {
                Long userId = ((Number) userIdObj).longValue();
                domainUser = userRepository.findById(userId).orElse(null);
                if (domainUser == null) {
                    log.error("OAuth2 인증된 사용자 ID '{}'에 해당하는 사용자를 DB에서 찾을 수 없습니다.", userId);
                }
            } else {
                log.error("OAuth2User principal에서 'id' 속성을 찾을 수 없거나 숫자 타입이 아닙니다: {}", userIdObj);
            }
        } else if (principal instanceof UserDetails springUserDetails) {
            String loginId = springUserDetails.getUsername();
            Optional<UserCredential> userCredentialOpt = userCredentialRepository.findByLoginId(loginId);
            if (userCredentialOpt.isPresent()) {
                domainUser = userCredentialOpt.get().getUser();
            } else {
                log.error("Form 인증된 사용자 loginId '{}'에 해당하는 UserCredential을 찾을 수 없습니다.", loginId);
            }
        } else if (principal instanceof String && "anonymousUser".equals(principal)) {
            return null;
        } else if (principal instanceof User directUser) {
            domainUser = directUser;
        }
        else {
            log.warn("알 수 없는 Principal 타입입니다: {}", principal != null ? principal.getClass().getName() : "null");
        }
        return domainUser;
    }

    @PostMapping
    public ResponseEntity<?> createProject(
            @Valid @RequestBody ProjectCreateRequestDto requestDto,
            Authentication authentication,
            HttpServletRequest request) {

        User currentUser = getCurrentDomainUser(authentication);
        if (currentUser == null) {
            ErrorResponse errorResponse = new ErrorResponse(
                    HttpStatus.UNAUTHORIZED,
                    "AUTH_REQUIRED",
                    "프로젝트를 생성하려면 로그인이 필요합니다.",
                    request.getRequestURI()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
        log.info("API 호출: 새 프로젝트 생성 - title: {}, byUser: {}", requestDto.getTitle(), currentUser.getNickname());
        ProjectResponseDto createdProject = projectService.createProject(requestDto, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "프로젝트가 성공적으로 생성되었습니다.", createdProject));
    }

    @GetMapping
    public ResponseEntity<?> getMyProjects(
            Authentication authentication,
            HttpServletRequest request) {

        User currentUser = getCurrentDomainUser(authentication);
        if (currentUser == null) {
            ErrorResponse errorResponse = new ErrorResponse(
                    HttpStatus.UNAUTHORIZED,
                    "AUTH_REQUIRED",
                    "프로젝트 목록을 조회하려면 로그인이 필요합니다.",
                    request.getRequestURI()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
        log.info("API 호출: 내 프로젝트 목록 조회 by user: {}", currentUser.getNickname());
        List<ProjectResponseDto> myProjects = projectService.getMyProjects(currentUser);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "내 프로젝트 목록 조회가 성공했습니다.", myProjects));
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProjectById( // 반환 타입 ResponseEntity<?>로 변경
                                             @PathVariable Long projectId,
                                             Authentication authentication,
                                             HttpServletRequest request) { // HttpServletRequest 파라미터 추가
        User currentUser = getCurrentDomainUser(authentication);

        if (currentUser == null) {
            ErrorResponse errorResponse = new ErrorResponse(
                    HttpStatus.UNAUTHORIZED,
                    "AUTH_REQUIRED",
                    "프로젝트 상세 정보를 조회하려면 로그인이 필요합니다.",
                    request.getRequestURI() // 요청 경로 추가
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }

        ProjectResponseDto projectDetails = projectService.getProjectDetails(projectId, currentUser);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "프로젝트 상세 정보 조회 성공", projectDetails));
    }

    @PostMapping("/{projectId}/members")
    public ResponseEntity<?> inviteUserToProject(
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectInviteRequestDto inviteDto,
            Authentication authentication,
            HttpServletRequest request) {
        User currentUser = getCurrentDomainUser(authentication);
        if (currentUser == null) {
            ErrorResponse errorResponse = new ErrorResponse(
                    HttpStatus.UNAUTHORIZED,
                    "AUTH_REQUIRED",
                    "멤버를 초대하려면 로그인이 필요합니다.",
                    request.getRequestURI()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
        log.info("API 호출: 프로젝트 {}에 사용자 {} 초대 by user {}", projectId, inviteDto.getInviteeUserId(), currentUser.getNickname());
        projectService.inviteUserToProject(projectId, inviteDto, currentUser);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "프로젝트 초대 요청을 성공적으로 보냈습니다.", null));
    }

    @GetMapping("/{projectId}/members")
    public ResponseEntity<?> getProjectMembers(
            @PathVariable Long projectId,
            Authentication authentication,
            HttpServletRequest request) {
        User currentUser = getCurrentDomainUser(authentication);
        if (currentUser == null) {
            ErrorResponse errorResponse = new ErrorResponse(
                    HttpStatus.UNAUTHORIZED,
                    "AUTH_REQUIRED",
                    "프로젝트 멤버를 조회하려면 로그인이 필요합니다.",
                    request.getRequestURI()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
        log.info("API 호출: 프로젝트 {} 멤버 목록 조회 by user {}", projectId, currentUser.getNickname());
        List<ProjectMemberResponseDto> members = projectService.getProjectMembers(projectId, currentUser);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "프로젝트 멤버 목록 조회가 성공했습니다.", members));
    }

    @PatchMapping("/{projectId}/members/{memberUserId}/role")
    public ResponseEntity<?> updateProjectMemberRole(
            @PathVariable Long projectId,
            @PathVariable Long memberUserId,
            @Valid @RequestBody ProjectMemberRoleUpdateRequestDto roleUpdateRequestDto,
            Authentication authentication,
            HttpServletRequest request) {
        User currentUser = getCurrentDomainUser(authentication);
        if (currentUser == null) {
            ErrorResponse errorResponse = new ErrorResponse(
                    HttpStatus.UNAUTHORIZED,
                    "AUTH_REQUIRED",
                    "멤버 역할을 변경하려면 로그인이 필요합니다.",
                    request.getRequestURI()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
        log.info("API 호출: 프로젝트 {}의 멤버 {} 역할 변경 시도 -> {} by user {}", projectId, memberUserId, roleUpdateRequestDto.getRole(), currentUser.getNickname());
        projectService.updateProjectMemberRole(projectId, memberUserId, roleUpdateRequestDto.getRole(), currentUser);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "프로젝트 멤버 역할이 성공적으로 변경되었습니다.", null));
    }

    @DeleteMapping("/{projectId}/members/{memberUserId}")
    public ResponseEntity<?> removeProjectMember(
            @PathVariable Long projectId,
            @PathVariable Long memberUserId,
            Authentication authentication,
            HttpServletRequest request) {
        User currentUser = getCurrentDomainUser(authentication);
        if (currentUser == null) {
            ErrorResponse errorResponse = new ErrorResponse(
                    HttpStatus.UNAUTHORIZED,
                    "AUTH_REQUIRED",
                    "멤버를 제외하려면 로그인이 필요합니다.",
                    request.getRequestURI()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
        log.info("API 호출: 프로젝트 {}에서 멤버 {} 제외 시도 by user {}", projectId, memberUserId, currentUser.getNickname());
        projectService.removeProjectMember(projectId, memberUserId, currentUser);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "프로젝트에서 멤버가 성공적으로 제외되었습니다.", null));
    }

    @PostMapping("/{projectId}/invitations/accept")
    public ResponseEntity<?> acceptProjectInvitation(
            @PathVariable Long projectId,
            Authentication authentication,
            HttpServletRequest request) {
        User currentUser = getCurrentDomainUser(authentication);
        if (currentUser == null) {
            ErrorResponse errorResponse = new ErrorResponse(
                    HttpStatus.UNAUTHORIZED,
                    "AUTH_REQUIRED",
                    "초대를 수락하려면 로그인이 필요합니다.",
                    request.getRequestURI()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
        log.info("API 호출: 사용자 {}가 프로젝트 {} 초대 수락", currentUser.getNickname(), projectId);
        projectService.acceptProjectInvitation(projectId, currentUser);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "프로젝트 초대를 수락했습니다.", null));
    }

    @PostMapping("/{projectId}/invitations/reject")
    public ResponseEntity<?> rejectProjectInvitation(
            @PathVariable Long projectId,
            Authentication authentication,
            HttpServletRequest request) {
        User currentUser = getCurrentDomainUser(authentication);
        if (currentUser == null) {
            ErrorResponse errorResponse = new ErrorResponse(
                    HttpStatus.UNAUTHORIZED,
                    "AUTH_REQUIRED",
                    "초대를 거절하려면 로그인이 필요합니다.",
                    request.getRequestURI()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
        log.info("API 호출: 사용자 {}가 프로젝트 {} 초대 거절", currentUser.getNickname(), projectId);
        projectService.rejectProjectInvitation(projectId, currentUser);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "프로젝트 초대를 거절했습니다.", null));
    }

    /**
     * 특정 프로젝트의 상세 정보(제목, 설명 등)를 수정합니다.
     * @param projectId 수정할 프로젝트의 ID
     * @param requestDto 수정할 내용 (title, description)
     * @param authentication 현재 인증 정보
     * @param request HttpServletRequest (ErrorResponse에 경로를 제공하기 위함)
     * @return 수정된 프로젝트 정보 또는 오류 응답
     */
    @PutMapping("/{projectId}")
    public ResponseEntity<?> updateProjectDetails(
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectUpdateRequestDto requestDto,
            Authentication authentication,
            HttpServletRequest request) {

        User currentUser = getCurrentDomainUser(authentication);
        if (currentUser == null) {
            ErrorResponse errorResponse = new ErrorResponse(
                    HttpStatus.UNAUTHORIZED,
                    "AUTH_REQUIRED",
                    "프로젝트 정보를 수정하려면 로그인이 필요합니다.",
                    request.getRequestURI()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }

        ProjectResponseDto updatedProject = projectService.updateProjectDetails(projectId, requestDto, currentUser);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "프로젝트 정보가 성공적으로 수정되었습니다.", updatedProject));
    }
}