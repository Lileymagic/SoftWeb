package com.example.softengineerwebpr.domain.post.entity;

import com.example.softengineerwebpr.domain.task.entity.Task;
import com.example.softengineerwebpr.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class) // 생성일자, 수정일자 자동 관리를 위함
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private Long idx; // 게시글의 고유 식별자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_idx", nullable = false, foreignKey = @ForeignKey(name = "fk_post_task"))
    private Task task; // 이 게시글이 속한 업무

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx", nullable = false, foreignKey = @ForeignKey(name = "fk_post_user"))
    private User user; // 게시글을 작성한 사용자

    @Column(nullable = false, length = 255)
    private String title; // 게시글 제목

    @Lob // TEXT 타입 매핑
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 게시글 내용

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; // 작성 시간

    @LastModifiedDate
    @Column(name = "updated_at") // DB 스키마상으로는 ON UPDATE CURRENT_TIMESTAMP
    private LocalDateTime updatedAt; // 수정 시간

    @Builder
    public Post(Task task, User user, String title, String content) {
        this.task = task;
        this.user = user;
        this.title = title;
        this.content = content;
    }

    // 게시글 내용 수정 메소드 (제목, 내용 등)
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}