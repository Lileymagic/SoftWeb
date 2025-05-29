package com.example.softengineerwebpr.domain.post.service.impl;

import com.example.softengineerwebpr.common.exception.BusinessLogicException;
import com.example.softengineerwebpr.common.exception.ErrorCode;
import com.example.softengineerwebpr.domain.post.dto.PostBasicResponseDto;
import com.example.softengineerwebpr.domain.post.entity.Post;
import com.example.softengineerwebpr.domain.post.repository.PostRepository;
import com.example.softengineerwebpr.domain.post.service.PostService;
import com.example.softengineerwebpr.domain.project.entity.Project;
import com.example.softengineerwebpr.domain.project.entity.ProjectMemberStatus;
import com.example.softengineerwebpr.domain.project.repository.ProjectMemberRepository;
import com.example.softengineerwebpr.domain.task.entity.Task;
import com.example.softengineerwebpr.domain.task.repository.TaskRepository;
import com.example.softengineerwebpr.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final TaskRepository taskRepository; // Task 엔티티 조회를 위해 필요
    private final ProjectMemberRepository projectMemberRepository; // 프로젝트 멤버십 확인을 위해 필요

    private Task findTaskOrThrow(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.TASK_NOT_FOUND));
    }

    private void checkProjectMembership(Project project, User user) {
        if (!projectMemberRepository.findByUserAndProject(user, project)
                .filter(pm -> pm.getStatus() == ProjectMemberStatus.ACCEPTED)
                .isPresent()) {
            throw new BusinessLogicException(ErrorCode.NOT_PROJECT_MEMBER, "해당 프로젝트의 멤버가 아니므로 게시글을 조회할 수 없습니다.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostBasicResponseDto> getPostsByTaskId(Long taskId, User currentUser) {
        Task task = findTaskOrThrow(taskId);
        Project project = task.getProject(); // Task 엔티티에서 Project 정보를 가져옴

        // 현재 사용자가 해당 업무가 속한 프로젝트의 멤버인지 확인
        checkProjectMembership(project, currentUser);

        List<Post> posts = postRepository.findByTaskOrderByCreatedAtDesc(task);

        return posts.stream()
                .map(PostBasicResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    // TODO: createPost, getPostById, updatePost, deletePost 메소드 구현
}