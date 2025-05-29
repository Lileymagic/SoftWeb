package com.example.softengineerwebpr.domain.post.repository;

import com.example.softengineerwebpr.domain.post.entity.Post;
import com.example.softengineerwebpr.domain.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * 특정 업무(Task)에 속한 모든 게시글을 작성일자(createdAt) 기준 내림차순으로 조회합니다.
     * @param task 게시글을 조회할 업무 엔티티
     * @return 해당 업무의 게시글 리스트
     */
    List<Post> findByTaskOrderByCreatedAtDesc(Task task);

    /**
     * 특정 업무 ID(task_idx)에 속한 모든 게시글을 작성일자(createdAt) 기준 내림차순으로 조회합니다.
     * JPQL을 사용하여 명시적으로 정의할 수도 있습니다. (위의 메소드와 기능적으로 유사)
     * @param taskIdx 게시글을 조회할 업무의 ID
     * @return 해당 업무의 게시글 리스트
     */
    @Query("SELECT p FROM Post p WHERE p.task.idx = :taskIdx ORDER BY p.createdAt DESC")
    List<Post> findByTaskIdxOrderByCreatedAtDesc(@Param("taskIdx") Long taskIdx);

    // 필요에 따라 사용자 ID로 게시글을 찾는 메소드 등 추가 가능
    // List<Post> findByUserOrderByCreatedAtDesc(User user);
}