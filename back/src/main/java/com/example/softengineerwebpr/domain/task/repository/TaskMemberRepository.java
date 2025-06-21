package com.example.softengineerwebpr.domain.task.repository;

import com.example.softengineerwebpr.domain.project.entity.Project; // Project 임포트 추가
import com.example.softengineerwebpr.domain.task.entity.Task;
import com.example.softengineerwebpr.domain.task.entity.TaskMember;
import com.example.softengineerwebpr.domain.task.entity.TaskMemberId;
import com.example.softengineerwebpr.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying; // Modifying 임포트 추가
import org.springframework.data.jpa.repository.Query;   // Query 임포트 추가
import org.springframework.data.repository.query.Param; // Param 임포트 추가

import java.util.List;
import java.util.Optional;

public interface TaskMemberRepository extends JpaRepository<TaskMember, TaskMemberId> {
    List<TaskMember> findByTask(Task task);
    List<TaskMember> findByUser(User user);
    Optional<TaskMember> findByUserAndTask(User user, Task task);
    boolean existsByUserAndTask(User user, Task task);

    /**
     * 특정 프로젝트 내에서 특정 사용자의 모든 업무 할당 기록을 삭제합니다. (새로 추가)
     * @param user 제외될 사용자
     * @param project 관련 프로젝트
     */
    @Modifying // SELECT 쿼리가 아님을 명시
    @Query("DELETE FROM TaskMember tm WHERE tm.user = :user AND tm.task.project = :project")
    void deleteAllByUserAndProject(@Param("user") User user, @Param("project") Project project);
}