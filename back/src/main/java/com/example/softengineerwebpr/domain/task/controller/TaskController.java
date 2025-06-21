package com.example.softengineerwebpr.domain.task.controller; //

import com.example.softengineerwebpr.common.dto.ApiResponse; //
import com.example.softengineerwebpr.domain.task.dto.*;
import com.example.softengineerwebpr.domain.task.entity.Task;
import com.example.softengineerwebpr.domain.task.service.TaskService; //
import com.example.softengineerwebpr.domain.user.entity.User; //
import com.example.softengineerwebpr.common.util.AuthenticationUtil; //
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final AuthenticationUtil authenticationUtil;

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
            @Valid @RequestBody TaskStatusUpdateRequestDto statusUpdateRequestDto,
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

    // ====================================================================
    // ===== 새로 추가된 업무 담당자 동기화(Sync) API 엔드포인트 시작 =====
    // ====================================================================
    /**
     * 특정 업무의 담당자를 요청된 사용자 및 그룹 멤버 목록과 동기화합니다.
     * (기존 담당자 목록을 요청된 목록으로 덮어쓰기)
     * * @param taskId 동기화할 업무의 ID
     * @param requestDto 동기화할 사용자 ID 목록과 그룹 ID 목록을 담은 DTO
     * @param authentication 현재 인증 정보
     * @return 성공 응답
     */
    @PutMapping("/tasks/{taskId}/members")
    public ResponseEntity<ApiResponse<Void>> syncTaskMembers(
            @PathVariable Long taskId,
            @Valid @RequestBody TaskMemberSyncRequestDto requestDto,
            Authentication authentication) {
        User currentUser = authenticationUtil.getCurrentUser(authentication);
        taskService.syncTaskMembers(taskId, requestDto, currentUser);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "업무 담당자가 성공적으로 동기화되었습니다."));
    }
    // ==================================================================
    // ===== 새로 추가된 업무 담당자 동기화(Sync) API 엔드포인트 끝 =====
    // ==================================================================


    /**
     * 특정 업무에 담당자 할당 (기존: 단일 할당용)
     */
    @PostMapping("/tasks/{taskId}/members")
    public ResponseEntity<ApiResponse<TaskResponseDto>> assignMemberToTask(
            @PathVariable Long taskId,
            @RequestBody MemberAssignmentRequestDto requestDto,
            Authentication authentication) {
        User currentUser = authenticationUtil.getCurrentUser(authentication);
        TaskResponseDto updatedTask = taskService.assignMemberToTask(taskId, requestDto.getUserId(), currentUser);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "업무에 담당자가 성공적으로 할당되었습니다.", updatedTask));
    }

    /**
     * 특정 업무에서 담당자 제외 (기존: 단일 제외용)
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