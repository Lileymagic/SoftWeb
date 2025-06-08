package com.example.softengineerwebpr.domain.dashboard.repository;


import com.example.softengineerwebpr.domain.project.entity.Project;
import com.example.softengineerwebpr.domain.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DashboardRepository extends JpaRepository<Task, Long> {
    //프로젝트별 업무 상태 통계 조회
    @Query("SELECT t.status as status, COUNT(t) as count " +
            "FROM Task t " +
            "WHERE t.project = :project " +
            "GROUP BY t.status")
    List<Object[]> findTaskStatusCountsByProject(@Param("project") Project project);

    //마감일 준수 통계 조회, 마감일이 있어야함
    @Query("SELECT " +
            "CASE WHEN t.updatedAt <= t.deadline THEN 'ON_TIME' " +
            "     ELSE 'OVERDUE' END as compliance, " +
            "COUNT(t) as count " +
            "FROM Task t " +
            "WHERE t.project = :project " +
            "AND t.status = 'Done' " +
            "and t.deadline IS NOT NULL " +
            "and t.updatedAt IS NOT NULL " +
            "GROUP BY CASE WHEN t.updatedAt <= t.deadline THEN 'ON_TIME' ELSE 'OVERDUE' END")
    List<Object[]> findDeadlineComplianceByProject(@Param("project") Project project);

    //전체 완료된 업무 수
    @Query("SELECT COUNT(t) FROM Task t WHERE t.project = :project AND t.status = 'Done'")
    long countCompletedTasksByProject(@Param("project") Project project);
}
