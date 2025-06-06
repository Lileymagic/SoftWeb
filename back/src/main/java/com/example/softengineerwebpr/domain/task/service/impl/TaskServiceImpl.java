package com.example.softengineerwebpr.domain.task.service.impl;

import com.example.softengineerwebpr.common.exception.BusinessLogicException;
import com.example.softengineerwebpr.common.exception.ErrorCode; // ErrorCode 전체 임포트
import com.example.softengineerwebpr.domain.post.entity.Post;
import com.example.softengineerwebpr.domain.post.repository.PostRepository;
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
import com.example.softengineerwebpr.domain.file.dto.FileResponseDto; //
import com.example.softengineerwebpr.domain.file.entity.FileReferenceType; //
import com.example.softengineerwebpr.domain.file.service.FileService; // FileService 주입
import com.example.softengineerwebpr.domain.task.dto.TaskResponseDto; //
import com.example.softengineerwebpr.domain.user.dto.UserBasicInfoDto; //
import java.util.Collections; // 필요시
import java.util.List; // 필요시
import java.util.stream.Collectors; // 필요시

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
    private final FileService fileService; // 새로 주입
    private final PostRepository postRepository;

    // private final NotificationService notificationService;
    // private final HistoryService historyService;

    // Helper 메소드 (findProjectOrThrow, findTaskOrThrow, findUserOrThrow, checkProjectMembership 등은 기존과 동일)
    private Project findProjectOrThrow(Long projectId) { //
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.PROJECT_NOT_FOUND));
    }
    private Task findTaskOrThrow(Long taskId) { //
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.TASK_NOT_FOUND));
    }
    private User findUserOrThrow(Long userId) { //
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.USER_NOT_FOUND));
    }
    private void checkProjectMembership(Project project, User user) { //
        if (!projectMemberRepository.findByUserAndProject(user, project)
                .filter(pm -> pm.getStatus() == ProjectMemberStatus.ACCEPTED)
                .isPresent()) {
            throw new BusinessLogicException(ErrorCode.NOT_PROJECT_MEMBER);
        }
    }
    private boolean isUserProjectAdmin(User user, Project project) { //
        return projectMemberRepository.findByUserAndProject(user, project)
                .filter(pm -> pm.getStatus() == ProjectMemberStatus.ACCEPTED)
                .map(pm -> pm.getRole() == ProjectMemberRole.관리자) //
                .orElse(false);
    }
    private boolean isUserTaskAssignee(User user, Task task) { //
        return taskMemberRepository.existsByUserAndTask(user, task); //
    }
    private List<User> getAssignedUsersForTask(Task task) { //
        return taskMemberRepository.findByTask(task).stream() //
                .map(TaskMember::getUser) //
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
                .status(Task.TaskStatus.ToDo) //
                .createdBy(currentUser)
                .build(); //
        Task savedTask = taskRepository.save(task);
        log.info("새 업무 생성: '{}', 프로젝트: '{}', 생성자: {}", savedTask.getTitle(), project.getTitle(), currentUser.getNickname());

        // 파일은 별도 API로 업로드. 생성 시점에는 파일 없음.
        return TaskResponseDto.fromEntity(savedTask, getAssignedUsersForTask(savedTask), Collections.emptyList()); //
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponseDto> getTasksByProject(Long projectId, User currentUser) {
        Project project = findProjectOrThrow(projectId);
        checkProjectMembership(project, currentUser);

        List<Task> tasks = taskRepository.findByProjectOrderByCreatedAtDesc(project); //
        return tasks.stream()
                .map(task -> {
                    List<FileResponseDto> fileDtos = fileService.getFilesForReference(FileReferenceType.TASK, task.getIdx());
                    return TaskResponseDto.fromEntity(task, getAssignedUsersForTask(task), fileDtos); //
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponseDto getTaskById(Long taskId, User currentUser) {
        Task task = findTaskOrThrow(taskId);
        checkProjectMembership(task.getProject(), currentUser);
        List<FileResponseDto> fileDtos = fileService.getFilesForReference(FileReferenceType.TASK, task.getIdx());
        return TaskResponseDto.fromEntity(task, getAssignedUsersForTask(task), fileDtos); //
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
            throw new BusinessLogicException(ErrorCode.NO_AUTHORITY_TO_MANAGE_TASK);
        }

        task.updateDetails( //
                StringUtils.hasText(requestDto.getTitle()) ? requestDto.getTitle() : task.getTitle(),
                requestDto.getDescription() != null ? requestDto.getDescription() : task.getDescription(),
                requestDto.getStatus() != null ? requestDto.getStatus() : task.getStatus(),
                requestDto.getDeadline() != null ? requestDto.getDeadline() : task.getDeadline()
        );

        Task updatedTask = taskRepository.save(task);
        log.info("업무 수정: ID {}, 제목 '{}', 수행자: {}", taskId, updatedTask.getTitle(), currentUser.getNickname());
        List<FileResponseDto> fileDtos = fileService.getFilesForReference(FileReferenceType.TASK, updatedTask.getIdx());
        return TaskResponseDto.fromEntity(updatedTask, getAssignedUsersForTask(updatedTask), fileDtos); //
    }


    @Override
    public void deleteTask(Long taskId, User currentUser) {
        Task task = findTaskOrThrow(taskId);
        Project project = task.getProject();
        checkProjectMembership(project, currentUser);

        boolean canDelete = task.getCreatedBy().equals(currentUser) || isUserProjectAdmin(currentUser, project);
        if (!canDelete) {
            throw new BusinessLogicException(ErrorCode.NO_AUTHORITY_TO_MANAGE_TASK);
        }

        // 1. 업무에 직접 첨부된 파일 삭제
        fileService.deleteFilesForReference(FileReferenceType.TASK, taskId, currentUser);

        // 2. 업무에 속한 게시글들 및 그 게시글에 첨부된 파일, 댓글, 댓글에 첨부된 파일 삭제
        //    PostService에 deleteTaskAssociatedPosts(Long taskId, User currentUser) 같은 메소드를 만들어서 위임하거나,
        //    여기서 PostRepository를 통해 게시글 목록을 가져와 각각 PostService.deletePost(postId, currentUser)를 호출.
        //    PostService.deletePost는 이미 내부적으로 댓글 및 파일 삭제 로직을 포함하고 있어야 함.
        //    (Post 테이블의 task_idx 외래키에 ON DELETE CASCADE가 있다면 DB에서 게시글/댓글이 자동 삭제될 수 있으나,
        //     파일은 물리적 삭제 및 File 엔티티 삭제가 필요하므로 애플리케이션 레벨 처리가 안전.)
        //    DB 스키마 확인 결과: post 테이블의 fk_post_task에 ON DELETE CASCADE가 있음
        //    그러나 Post에 연결된 파일, Comment에 연결된 파일은 명시적으로 지워야 함.

        //    임시 처리: PostRepository를 직접 사용하여 게시글 ID 목록을 가져오고,
        //    각 게시글에 대해 fileService.deleteFilesForReference(FileReferenceType.POST, ...) 호출.
        //    이후 postRepository.deleteAll() (이 경우 댓글은 cascade).
        //    더 나은 방법은 PostService에 관련 로직을 두고 호출.
        List<Post> postsInTask = postRepository.findByTaskOrderByCreatedAtDesc(task); //
        for (Post post : postsInTask) {
            // Post에 달린 댓글의 파일들 삭제 (CommentService에 위임하거나 직접 처리)
            // CommentService.deleteCommentsAndFilesForPost(post.getIdx(), currentUser);
            // Post 자체 파일 삭제
            fileService.deleteFilesForReference(FileReferenceType.POST, post.getIdx(), currentUser);
        }
        // Post는 Task 삭제 시 DB Cascade로 삭제될 것으로 예상되므로, 명시적 postRepository.deleteAll(postsInTask)는 생략 가능
        // (만약 Post의 task_idx 외래키에 ON DELETE CASCADE가 없다면 여기서 삭제해야 함)
        // -> Post의 task_idx FK에 ON DELETE CASCADE 있음.

        // 3. 업무 담당자 정보 삭제 (TaskMember)
        taskMemberRepository.deleteAll(taskMemberRepository.findByTask(task)); //

        // 4. 업무 자체 삭제
        taskRepository.delete(task);
        log.info("업무 삭제: ID {}, 제목 '{}', 수행자: {}", taskId, task.getTitle(), currentUser.getNickname());
    }

    // updateTaskStatus, assignMemberToTask, removeMemberFromTask 메소드는 파일 처리와 직접 관련 없으므로 기존 로직 유지 (이전 답변 내용 참고)
    //
    @Override
    public TaskResponseDto updateTaskStatus(Long taskId, Task.TaskStatus newStatus, User currentUser) {
        Task task = findTaskOrThrow(taskId);
        checkProjectMembership(task.getProject(), currentUser);

        boolean canUpdateStatus = isUserTaskAssignee(currentUser, task) ||
                isUserProjectAdmin(currentUser, task.getProject());

        if (!canUpdateStatus) {
            throw new BusinessLogicException(ErrorCode.NO_AUTHORITY_TO_MANAGE_TASK);
        }

        Task.TaskStatus oldStatus = task.getStatus();
        if (oldStatus == newStatus) {
            List<FileResponseDto> fileDtos = fileService.getFilesForReference(FileReferenceType.TASK, task.getIdx());
            return TaskResponseDto.fromEntity(task, getAssignedUsersForTask(task), fileDtos);
        }

        task.updateStatus(newStatus); //
        Task updatedTask = taskRepository.save(task);
        log.info("업무 상태 변경: ID {}, '{}' -> '{}', 수행자: {}", taskId, oldStatus, newStatus, currentUser.getNickname());
        List<FileResponseDto> fileDtos = fileService.getFilesForReference(FileReferenceType.TASK, updatedTask.getIdx());
        return TaskResponseDto.fromEntity(updatedTask, getAssignedUsersForTask(updatedTask), fileDtos);
    }

    @Override
    public TaskResponseDto assignMemberToTask(Long taskId, Long userIdToAssign, User currentUser) {
        Task task = findTaskOrThrow(taskId);
        Project project = task.getProject();
        checkProjectMembership(project, currentUser);

        User userToAssign = findUserOrThrow(userIdToAssign);
        checkProjectMembership(project, userToAssign); // 할당할 사용자도 프로젝트 멤버여야 함

        if (!isUserProjectAdmin(currentUser, project) && !task.getCreatedBy().equals(currentUser)) { // 프로젝트 관리자 또는 업무 생성자만 할당 가능 (정책에 따라 조정)
            throw new BusinessLogicException(ErrorCode.NO_AUTHORITY_TO_MANAGE_TASK, "업무에 담당자를 할당할 권한이 없습니다.");
        }

        if (taskMemberRepository.existsByUserAndTask(userToAssign, task)) { //
            throw new BusinessLogicException(ErrorCode.USER_ALREADY_ASSIGNED_TO_TASK);
        }

        TaskMember taskMember = TaskMember.builder()
                .task(task)
                .user(userToAssign)
                .build(); //
        taskMemberRepository.save(taskMember);
        log.info("업무 담당자 할당: 업무 ID {}, 사용자 '{}', 수행자: {}", taskId, userToAssign.getNickname(), currentUser.getNickname());
        List<FileResponseDto> fileDtos = fileService.getFilesForReference(FileReferenceType.TASK, task.getIdx());
        return TaskResponseDto.fromEntity(task, getAssignedUsersForTask(task), fileDtos);
    }

    @Override
    public void removeMemberFromTask(Long taskId, Long userIdToRemove, User currentUser) {
        Task task = findTaskOrThrow(taskId);
        Project project = task.getProject();
        checkProjectMembership(project, currentUser);

        User userToRemove = findUserOrThrow(userIdToRemove);

        if (!isUserProjectAdmin(currentUser, project) && !task.getCreatedBy().equals(currentUser)) { // 프로젝트 관리자 또는 업무 생성자만 제외 가능 (정책에 따라 조정)
            throw new BusinessLogicException(ErrorCode.NO_AUTHORITY_TO_MANAGE_TASK, "업무에서 담당자를 제외할 권한이 없습니다.");
        }

        TaskMember taskMember = taskMemberRepository.findByUserAndTask(userToRemove, task) //
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.USER_NOT_ASSIGNED_TO_TASK));

        taskMemberRepository.delete(taskMember);
        log.info("업무 담당자 제외: 업무 ID {}, 사용자 '{}', 수행자: {}", taskId, userToRemove.getNickname(), currentUser.getNickname());
    }
}