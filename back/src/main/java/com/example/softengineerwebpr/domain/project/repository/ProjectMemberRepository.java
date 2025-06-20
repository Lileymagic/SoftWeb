package com.example.softengineerwebpr.domain.project.repository;

import com.example.softengineerwebpr.domain.project.entity.Project;
import com.example.softengineerwebpr.domain.project.entity.ProjectMember;
import com.example.softengineerwebpr.domain.project.entity.ProjectMemberId;
import com.example.softengineerwebpr.domain.project.entity.ProjectMemberStatus;
import com.example.softengineerwebpr.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberId> {
    // 특정 사용자가 참여하고 있는 프로젝트 멤버 정보 조회
    List<ProjectMember> findByUser(User user);

    // 특정 프로젝트에 참여하고 있는 멤버 정보 조회
    List<ProjectMember> findByProject(Project project);

    // 특정 사용자와 특정 프로젝트로 멤버 정보 조회
    Optional<ProjectMember> findByUserAndProject(User user, Project project);

    // JPQL을 사용하여 사용자가 참여 중인 프로젝트 목록 직접 조회 (ProjectMember를 통해 Project 엔티티를 가져옴)
    @Query("SELECT pm.project FROM ProjectMember pm WHERE pm.user = :user")
    List<Project> findProjectsByUser(@Param("user") User user);

    /**
     * 특정 사용자와 특정 상태에 해당하는 모든 프로젝트 멤버십 정보를 조회합니다. (새로 추가)
     * @param user 사용자 엔티티
     * @param status 조회할 상태 (예: PENDING)
     * @return 해당 조건의 ProjectMember 리스트
     */
    List<ProjectMember> findByUserAndStatus(User user, ProjectMemberStatus status);
}