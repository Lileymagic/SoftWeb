package com.example.softengineerwebpr.domain.task.service;

import com.example.softengineerwebpr.domain.task.dto.TaskCreateRequestDto;
import com.example.softengineerwebpr.domain.task.dto.TaskMemberSyncRequestDto;
import com.example.softengineerwebpr.domain.task.dto.TaskResponseDto;
import com.example.softengineerwebpr.domain.task.dto.TaskUpdateRequestDto;
import com.example.softengineerwebpr.domain.task.entity.Task; // Task.TaskStatus 접근을 위해
import com.example.softengineerwebpr.domain.user.entity.User;

import java.util.List;

public interface TaskService {

    /**
     * 특정 프로젝트에 새로운 업무를 생성합니다.
     *
     * @param projectId        업무가 속할 프로젝트의 ID
     * @param requestDto       업무 생성 요청 데이터 (제목, 설명, 마감일 등)
     * @param currentUser      현재 작업을 수행하는 사용자 (권한 검사 및 작성자 정보로 사용)
     * @return 생성된 업무 정보를 담은 TaskResponseDto
     */
    TaskResponseDto createTask(Long projectId, TaskCreateRequestDto requestDto, User currentUser);

    /**
     * 특정 프로젝트에 속한 모든 업무 목록을 조회합니다.
     *
     * @param projectId        업무 목록을 조회할 프로젝트의 ID
     * @param currentUser      현재 작업을 수행하는 사용자 (프로젝트 접근 권한 검사)
     * @return 해당 프로젝트의 업무 목록 (TaskResponseDto 리스트)
     */
    List<TaskResponseDto> getTasksByProject(Long projectId, User currentUser);

    /**
     * 특정 업무의 상세 정보를 조회합니다.
     *
     * @param taskId           조회할 업무의 ID
     * @param currentUser      현재 작업을 수행하는 사용자 (업무 접근 권한 검사)
     * @return 업무 상세 정보를 담은 TaskResponseDto
     */
    TaskResponseDto getTaskById(Long taskId, User currentUser);

    /**
     * 특정 업무의 정보를 수정합니다.
     * (제목, 설명, 마감일, 상태 등)
     *
     * @param taskId           수정할 업무의 ID
     * @param requestDto       업무 수정 요청 데이터
     * @param currentUser      현재 작업을 수행하는 사용자 (업무 수정 권한 검사)
     * @return 수정된 업무 정보를 담은 TaskResponseDto
     */
    TaskResponseDto updateTask(Long taskId, TaskUpdateRequestDto requestDto, User currentUser);

    /**
     * 특정 업무의 상태를 변경합니다.
     *
     * @param taskId        상태를 변경할 업무의 ID
     * @param newStatus     새로운 업무 상태
     * @param currentUser   현재 작업을 수행하는 사용자
     * @return 상태가 변경된 업무 정보를 담은 TaskResponseDto
     */
    TaskResponseDto updateTaskStatus(Long taskId, Task.TaskStatus newStatus, User currentUser);


    /**
     * 특정 업무를 삭제합니다.
     *
     * @param taskId           삭제할 업무의 ID
     * @param currentUser      현재 작업을 수행하는 사용자 (업무 삭제 권한 검사)
     */
    void deleteTask(Long taskId, User currentUser);

    /**
     * 특정 업무에 사용자를 담당자로 할당합니다.
     *
     * @param taskId           담당자를 할당할 업무의 ID
     * @param userIdToAssign   담당자로 지정될 사용자의 ID
     * @param currentUser      현재 작업을 수행하는 사용자 (권한 검사)
     * @return 담당자가 할당된 업무 정보를 담은 TaskResponseDto 또는 void
     */
    TaskResponseDto assignMemberToTask(Long taskId, Long userIdToAssign, User currentUser);

    /**
     * 특정 업무에서 담당자를 제외합니다.
     *
     * @param taskId           담당자를 제외할 업무의 ID
     * @param userIdToRemove   제외될 사용자의 ID
     * @param currentUser      현재 작업을 수행하는 사용자 (권한 검사)
     */
    void removeMemberFromTask(Long taskId, Long userIdToRemove, User currentUser);

    /**
     * 특정 업무의 담당자를 요청된 사용자 및 그룹 멤버 목록과 동기화합니다.
     * (기존 담당자 중 목록에 없으면 제외, 목록에 있으나 기존 담당자가 아니면 추가)
     * @param taskId 동기화할 업무의 ID
     * @param requestDto 동기화할 사용자 및 그룹 ID 목록을 담은 DTO
     * @param currentUser 현재 작업을 수행하는 사용자 (권한 검사)
     */
    void syncTaskMembers(Long taskId, TaskMemberSyncRequestDto requestDto, User currentUser);

}