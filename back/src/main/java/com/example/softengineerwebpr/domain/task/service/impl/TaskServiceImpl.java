package com.example.softengineerwebpr.domain.task.service.impl;

import com.example.softengineerwebpr.common.exception.BusinessLogicException;
import com.example.softengineerwebpr.common.exception.ErrorCode; // ErrorCode 전체 임포트
import com.example.softengineerwebpr.domain.project.entity.Project;
import com.example.softengineerwebpr.domain.project.entity.ProjectMemberRole;
import com.example.softengineerwebpr.domain.project.entity.ProjectMemberStatus;
import com.example.softengineerwebpr.domain.project.repository.ProjectRepository;
import com.example.softengineerwebpr.domain.project.repository.ProjectMemberRepository;
import com.example.softengineerwebpr.domain.task.dto.TaskCreateRequestDto;
import com.example.softengineerwebpr.domain.task.dto.TaskResponseDto;
import com.example.softengineerwebpr.domain.task.dto.TaskUpdateRequestDto;
import com.example.softengineerwebpr.domain.task.entity.Task;
import com.example.softengineerwebpr.domain.task.entity.TaskMember;
import com.example.softengineerwebpr.domain.task.repository.TaskMemberRepository;
import com.example.softengineerwebpr.domain.task.repository.TaskRepository;
import com.example.softengineerwebpr.domain.task.service.TaskService;
import com.example.softengineerwebpr.domain.user.entity.User;
import com.example.softengineerwebpr.domain.user.repository.UserRepository;
// import com.example.softengineerwebpr.domain.notification.service.NotificationService;
// import com.example.softengineerwebpr.domain.history.service.HistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMemberRepository taskMemberRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMemberRepository projectMemberRepository;

    // private final NotificationService notificationService;
    // private final HistoryService historyService;

    // --- Helper Methods ---
    private Project findProjectOrThrow(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.PROJECT_NOT_FOUND));
    }

    private Task findTaskOrThrow(Long taskId) {
        // ErrorCode.RESOURCE_NOT_FOUND -> ErrorCode.TASK_NOT_FOUND로 변경
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.TASK_NOT_FOUND));
    }

    private User findUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.USER_NOT_FOUND));
    }

    private void checkProjectMembership(Project project, User user) {
        if (!projectMemberRepository.findByUserAndProject(user, project)
                .filter(pm -> pm.getStatus() == ProjectMemberStatus.ACCEPTED)
                .isPresent()) {
            throw new BusinessLogicException(ErrorCode.NOT_PROJECT_MEMBER);
        }
    }

    private boolean isUserProjectAdmin(User user, Project project) {
        return projectMemberRepository.findByUserAndProject(user, project)
                .filter(pm -> pm.getStatus() == ProjectMemberStatus.ACCEPTED)
                .map(pm -> pm.getRole() == ProjectMemberRole.관리자)
                .orElse(false);
    }

    private boolean isUserTaskAssignee(User user, Task task) {
        return taskMemberRepository.existsByUserAndTask(user, task);
    }

    private List<User> getAssignedUsersForTask(Task task) {
        return taskMemberRepository.findByTask(task).stream()
                .map(TaskMember::getUser)
                .collect(Collectors.toList());
    }

    // --- Service Methods ---

    @Override
    public TaskResponseDto createTask(Long projectId, TaskCreateRequestDto requestDto, User currentUser) {
        Project project = findProjectOrThrow(projectId);
        checkProjectMembership(project, currentUser);

        Task task = Task.builder()
                .project(project)
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .deadline(requestDto.getDeadline())
                .status(Task.TaskStatus.ToDo)
                .createdBy(currentUser)
                .build();
        Task savedTask = taskRepository.save(task);
        log.info("새 업무 생성: '{}', 프로젝트: '{}', 생성자: {}", savedTask.getTitle(), project.getTitle(), currentUser.getNickname());
        return TaskResponseDto.fromEntity(savedTask, getAssignedUsersForTask(savedTask));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponseDto> getTasksByProject(Long projectId, User currentUser) {
        Project project = findProjectOrThrow(projectId);
        checkProjectMembership(project, currentUser);

        List<Task> tasks = taskRepository.findByProjectOrderByCreatedAtDesc(project);
        return tasks.stream()
                .map(task -> TaskResponseDto.fromEntity(task, getAssignedUsersForTask(task)))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponseDto getTaskById(Long taskId, User currentUser) {
        Task task = findTaskOrThrow(taskId);
        checkProjectMembership(task.getProject(), currentUser);
        return TaskResponseDto.fromEntity(task, getAssignedUsersForTask(task));
    }

    @Override
    public TaskResponseDto updateTask(Long taskId, TaskUpdateRequestDto requestDto, User currentUser) {
        Task task = findTaskOrThrow(taskId);
        Project project = task.getProject();
        checkProjectMembership(project, currentUser);

        boolean canUpdate = task.getCreatedBy().equals(currentUser) ||
                isUserTaskAssignee(currentUser, task) ||
                isUserProjectAdmin(currentUser, project);

        if (!canUpdate) {
            // ErrorCode.ACCESS_DENIED -> ErrorCode.NO_AUTHORITY_TO_MANAGE_TASK로 변경
            throw new BusinessLogicException(ErrorCode.NO_AUTHORITY_TO_MANAGE_TASK);
        }

        task.updateDetails(
                StringUtils.hasText(requestDto.getTitle()) ? requestDto.getTitle() : task.getTitle(),
                requestDto.getDescription() != null ? requestDto.getDescription() : task.getDescription(),
                requestDto.getStatus() != null ? requestDto.getStatus() : task.getStatus(),
                requestDto.getDeadline() != null ? requestDto.getDeadline() : task.getDeadline()
        );

        Task updatedTask = taskRepository.save(task);
        log.info("업무 수정: ID {}, 제목 '{}', 수행자: {}", taskId, updatedTask.getTitle(), currentUser.getNickname());
        return TaskResponseDto.fromEntity(updatedTask, getAssignedUsersForTask(updatedTask));
    }

    @Override
    public TaskResponseDto updateTaskStatus(Long taskId, Task.TaskStatus newStatus, User currentUser) {
        Task task = findTaskOrThrow(taskId);
        checkProjectMembership(task.getProject(), currentUser);

        boolean canUpdateStatus = isUserTaskAssignee(currentUser, task) ||
                isUserProjectAdmin(currentUser, task.getProject());

        if (!canUpdateStatus) {
            // ErrorCode.ACCESS_DENIED -> ErrorCode.NO_AUTHORITY_TO_MANAGE_TASK로 변경
            throw new BusinessLogicException(ErrorCode.NO_AUTHORITY_TO_MANAGE_TASK);
        }

        Task.TaskStatus oldStatus = task.getStatus();
        if (oldStatus == newStatus) {
            return TaskResponseDto.fromEntity(task, getAssignedUsersForTask(task));
        }

        task.updateStatus(newStatus);
        Task updatedTask = taskRepository.save(task);
        log.info("업무 상태 변경: ID {}, '{}' -> '{}', 수행자: {}", taskId, oldStatus, newStatus, currentUser.getNickname());
        return TaskResponseDto.fromEntity(updatedTask, getAssignedUsersForTask(updatedTask));
    }

    @Override
    public void deleteTask(Long taskId, User currentUser) {
        Task task = findTaskOrThrow(taskId);
        Project project = task.getProject();
        checkProjectMembership(project, currentUser);

        boolean canDelete = task.getCreatedBy().equals(currentUser) ||
                isUserProjectAdmin(currentUser, project);

        if (!canDelete) {
            // ErrorCode.ACCESS_DENIED -> ErrorCode.NO_AUTHORITY_TO_MANAGE_TASK로 변경
            throw new BusinessLogicException(ErrorCode.NO_AUTHORITY_TO_MANAGE_TASK);
        }

        taskMemberRepository.deleteAll(taskMemberRepository.findByTask(task));
        taskRepository.delete(task);
        log.info("업무 삭제: ID {}, 제목 '{}', 수행자: {}", taskId, task.getTitle(), currentUser.getNickname());
    }

    @Override
    public TaskResponseDto assignMemberToTask(Long taskId, Long userIdToAssign, User currentUser) {
        Task task = findTaskOrThrow(taskId);
        Project project = task.getProject();
        checkProjectMembership(project, currentUser);

        User userToAssign = findUserOrThrow(userIdToAssign);
        // 업무에 할당하려는 사용자가 해당 프로젝트의 멤버인지 확인하는 로직은 checkProjectMembership(project, userToAssign)으로 이미 존재합니다.
        // 만약 TASK_ASSIGNEE_NOT_PROJECT_MEMBER 에러 코드를 사용하고 싶다면, checkProjectMembership 호출 후,
        // 실패 시 해당 에러를 발생시키도록 할 수 있으나, 현재는 NOT_PROJECT_MEMBER로 충분해 보입니다.
        checkProjectMembership(project, userToAssign);


        if (!isUserProjectAdmin(currentUser, project)) {
            // ErrorCode.ACCESS_DENIED -> ErrorCode.NO_AUTHORITY_TO_MANAGE_TASK로 변경
            throw new BusinessLogicException(ErrorCode.NO_AUTHORITY_TO_MANAGE_TASK);
        }

        if (taskMemberRepository.existsByUserAndTask(userToAssign, task)) {
            // ErrorCode.INTERNAL_SERVER_ERROR -> ErrorCode.USER_ALREADY_ASSIGNED_TO_TASK로 변경
            throw new BusinessLogicException(ErrorCode.USER_ALREADY_ASSIGNED_TO_TASK);
        }

        TaskMember taskMember = TaskMember.builder()
                .task(task)
                .user(userToAssign)
                .build();
        taskMemberRepository.save(taskMember);
        log.info("업무 담당자 할당: 업무 ID {}, 사용자 '{}', 수행자: {}", taskId, userToAssign.getNickname(), currentUser.getNickname());
        return TaskResponseDto.fromEntity(task, getAssignedUsersForTask(task));
    }

    @Override
    public void removeMemberFromTask(Long taskId, Long userIdToRemove, User currentUser) {
        Task task = findTaskOrThrow(taskId);
        Project project = task.getProject();
        checkProjectMembership(project, currentUser);

        User userToRemove = findUserOrThrow(userIdToRemove);

        if (!isUserProjectAdmin(currentUser, project)) {
            // ErrorCode.ACCESS_DENIED -> ErrorCode.NO_AUTHORITY_TO_MANAGE_TASK로 변경
            throw new BusinessLogicException(ErrorCode.NO_AUTHORITY_TO_MANAGE_TASK);
        }

        TaskMember taskMember = taskMemberRepository.findByUserAndTask(userToRemove, task)
                // ErrorCode.RESOURCE_NOT_FOUND -> ErrorCode.USER_NOT_ASSIGNED_TO_TASK로 변경
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.USER_NOT_ASSIGNED_TO_TASK));

        taskMemberRepository.delete(taskMember);
        log.info("업무 담당자 제외: 업무 ID {}, 사용자 '{}', 수행자: {}", taskId, userToRemove.getNickname(), currentUser.getNickname());
    }
}