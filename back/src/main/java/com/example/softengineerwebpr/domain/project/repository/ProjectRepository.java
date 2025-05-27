package com.example.softengineerwebpr.domain.project.repository;

import com.example.softengineerwebpr.domain.project.entity.Project;
import com.example.softengineerwebpr.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    // 사용자가 생성한 프로젝트 목록 조회
    List<Project> findByCreatedBy(User user);

    // 프로젝트 제목으로 검색 (필요시)
    // List<Project> findByTitleContaining(String title);
}