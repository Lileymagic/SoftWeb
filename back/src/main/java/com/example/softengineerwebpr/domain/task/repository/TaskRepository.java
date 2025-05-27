package com.example.softengineerwebpr.domain.task.repository;

import com.example.softengineerwebpr.domain.project.entity.Project;
import com.example.softengineerwebpr.domain.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProjectOrderByCreatedAtDesc(Project project);
    // 필요에 따라 추가적인 조회 메소드 정의 (예: 상태별 조회 등)
    // List<Task> findByProjectAndStatus(Project project, Task.TaskStatus status);
}