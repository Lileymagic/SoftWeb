package com.example.softengineerwebpr.domain.history.repository; // domain.history.repository 패키지 생성

import com.example.softengineerwebpr.domain.history.entity.History;
import com.example.softengineerwebpr.domain.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {

    /**
     * 특정 프로젝트의 모든 히스토리를 최신순으로 조회합니다.
     */
    List<History> findByProjectOrderByCreatedAtDesc(Project project);
}