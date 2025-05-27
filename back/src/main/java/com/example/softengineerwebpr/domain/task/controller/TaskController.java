package com.example.softengineerwebpr.domain.task.controller;

import com.example.softengineerwebpr.common.dto.ApiResponse;
import com.example.softengineerwebpr.domain.task.dto.*;
import com.example.softengineerwebpr.domain.task.entity.Task; // Task.TaskStatus 접근
import com.example.softengineerwebpr.domain.task.service.TaskService;
import com.example.softengineerwebpr.domain.user.entity.User; // UserService에서 User를 가져오는 로직이 필요
import com.example.softengineerwebpr.domain.auth.service.AuthService; // 필요 시 사용자 정보 조회용 (혹은 UserService에서 처리)

// 현재 사용자 정보를 가져오기 위한 유틸리티 또는 서비스가 필요합니다.
// 여기서는 ProjectController에서 사용된 것과 유사한 로직을 사용한다고 가정하거나,
// AuthenticationPrincipal 어노테이션을 사용하여 User 객체를 직접 주입받는 방식을 고려할 수 있습니다.
// 편의상 여기서는 UserService 또는 별도 유틸리티를 통해 User 객체를 가져온다고 가정합니다.
// 예시: import org.springframework.security.core.annotation.AuthenticationPrincipal;
// (실제 User 객체를 Principal로 사용하려면 CustomUserDetails 또는 유사 객체 설정 필요)

import com.example.softengineerwebpr.common.util.AuthenticationUtil; // 가상의 인증 유틸리티 클래스
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api") // API 기본 경로
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final AuthenticationUtil authenticationUtil; // 현재 사용자 정보 가져오는 유틸리티 (가정)

    /**
     * 특정 프로젝트에 새 업무 생성
     */
    @PostMapping("/projects/{projectId}/tasks")
    public ResponseEntity<ApiResponse<TaskResponseDto>> createTask(
            @PathVariable Long projectId,
            @Valid @RequestBody TaskCreateRequestDto requestDto,
            Authentication authentication) {
        User currentUser = authenticationUtil.getCurrentUser(authentication);
        TaskResponseDto createdTask = taskService.createTask(projectId, requestDto, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "업무가 성공적으로 생성되었습니다.", createdTask));
    }

    /**
     * 특정 프로젝트의 모든 업무 목록 조회
     */
    @GetMapping("/projects/{projectId}/tasks")
    public ResponseEntity<ApiResponse<List<TaskResponseDto>>> getTasksByProject(
            @PathVariable Long projectId,
            Authentication authentication) {
        User currentUser = authenticationUtil.getCurrentUser(authentication);
        List<TaskResponseDto> tasks = taskService.getTasksByProject(projectId, currentUser);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "프로젝트 업무 목록 조회 성공", tasks));
    }

    /**
     * 특정 업무 상세 정보 조회
     */
    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<ApiResponse<TaskResponseDto>> getTaskById(
            @PathVariable Long taskId,
            Authentication authentication) {
        User currentUser = authenticationUtil.getCurrentUser(authentication);
        TaskResponseDto task = taskService.getTaskById(taskId, currentUser);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "업무 상세 정보 조회 성공", task));
    }

    /**
     * 특정 업무 정보 수정 (제목, 설명, 마감일 등)
     */
    @PutMapping("/tasks/{taskId}")
    public ResponseEntity<ApiResponse<TaskResponseDto>> updateTask(
            @PathVariable Long taskId,
            @Valid @RequestBody TaskUpdateRequestDto requestDto,
            Authentication authentication) {
        User currentUser = authenticationUtil.getCurrentUser(authentication);
        TaskResponseDto updatedTask = taskService.updateTask(taskId, requestDto, currentUser);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "업무가 성공적으로 수정되었습니다.", updatedTask));
    }

    /**
     * 특정 업무 상태 변경
     */
    @PatchMapping("/tasks/{taskId}/status")
    public ResponseEntity<ApiResponse<TaskResponseDto>> updateTaskStatus(
            @PathVariable Long taskId,
            @Valid @RequestBody TaskStatusUpdateRequestDto statusUpdateRequestDto, // 상태 변경용 DTO
            Authentication authentication) {
        User currentUser = authenticationUtil.getCurrentUser(authentication);
        TaskResponseDto updatedTask = taskService.updateTaskStatus(taskId, statusUpdateRequestDto.getStatus(), currentUser);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "업무 상태가 성공적으로 변경되었습니다.", updatedTask));
    }

    /**
     * 특정 업무 삭제
     */
    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(
            @PathVariable Long taskId,
            Authentication authentication) {
        User currentUser = authenticationUtil.getCurrentUser(authentication);
        taskService.deleteTask(taskId, currentUser);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "업무가 성공적으로 삭제되었습니다."));
    }

    /**
     * 특정 업무에 담당자 할당
     * 요청 본문에 할당할 사용자의 ID를 포함 (예: {"userId": 123})
     */
    @PostMapping("/tasks/{taskId}/members")
    public ResponseEntity<ApiResponse<TaskResponseDto>> assignMemberToTask(
            @PathVariable Long taskId,
            @RequestBody MemberAssignmentRequestDto requestDto, // 담당자 할당용 DTO
            Authentication authentication) {
        User currentUser = authenticationUtil.getCurrentUser(authentication);
        TaskResponseDto updatedTask = taskService.assignMemberToTask(taskId, requestDto.getUserId(), currentUser);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "업무에 담당자가 성공적으로 할당되었습니다.", updatedTask));
    }

    /**
     * 특정 업무에서 담당자 제외
     */
    @DeleteMapping("/tasks/{taskId}/members/{userIdToRemove}")
    public ResponseEntity<ApiResponse<Void>> removeMemberFromTask(
            @PathVariable Long taskId,
            @PathVariable Long userIdToRemove,
            Authentication authentication) {
        User currentUser = authenticationUtil.getCurrentUser(authentication);
        taskService.removeMemberFromTask(taskId, userIdToRemove, currentUser);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "업무에서 담당자가 성공적으로 제외되었습니다."));
    }
}

// --- TaskController에서 사용할 추가 DTO 정의 (별도 파일로 분리 권장) ---

// package com.example.softengineerwebpr.domain.task.dto;
// import com.example.softengineerwebpr.domain.task.entity.Task.TaskStatus;
// import jakarta.validation.constraints.NotNull;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import lombok.Setter;
//
// @Getter
// @Setter
// @NoArgsConstructor
// class TaskStatusUpdateRequestDto { // TaskController 내부 또는 task.dto 패키지에 생성
//    @NotNull(message = "새로운 업무 상태는 필수입니다.")
//    private Task.TaskStatus status;
// }
//
// @Getter
// @Setter
// @NoArgsConstructor
// class MemberAssignmentRequestDto { // TaskController 내부 또는 task.dto 패키지에 생성
//    @NotNull(message = "할당할 사용자의 ID는 필수입니다.")
//    private Long userId;
// }