package com.example.softengineerwebpr.domain.task.repository;

import com.example.softengineerwebpr.domain.task.entity.Task;
import com.example.softengineerwebpr.domain.task.entity.TaskMember;
import com.example.softengineerwebpr.domain.task.entity.TaskMemberId;
import com.example.softengineerwebpr.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TaskMemberRepository extends JpaRepository<TaskMember, TaskMemberId> {
    List<TaskMember> findByTask(Task task);
    List<TaskMember> findByUser(User user);
    Optional<TaskMember> findByUserAndTask(User user, Task task);
    // Task에 특정 사용자가 할당되어 있는지 확인
    boolean existsByUserAndTask(User user, Task task);
}