package com.example.softengineerwebpr.domain.task.entity;

import com.example.softengineerwebpr.domain.project.entity.Project;
import com.example.softengineerwebpr.domain.user.entity.User; // 업무 생성자 등을 위해 필요
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener; // 생성일자 자동 관리

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class) // @CreatedDate 사용 위함
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_idx", nullable = false, foreignKey = @ForeignKey(name = "fk_task_project"))
    private Project project; // 이 업무가 속한 프로젝트

    @Column(nullable = false, length = 255)
    private String title;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status; // ENUM: 'To Do', 'In Progress', 'Done'

    private LocalDateTime deadline;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false, foreignKey = @ForeignKey(name = "fk_task_user_created_by"))
    private User createdBy;


    // TaskStatus Enum 정의 (별도 파일 또는 내부 클래스로 가능)
    public enum TaskStatus {
        ToDo, InProgress, Done // DB ENUM 값과 일치: 'To Do', 'In Progress', 'Done'
    }

    @Builder
    public Task(Project project, String title, String description, TaskStatus status, LocalDateTime deadline, User createdBy) {
        this.project = project;
        this.title = title;
        this.description = description;
        this.status = status;
        this.deadline = deadline;
        this.createdBy = createdBy;
    }

    // 필요에 따른 setter 또는 수정 메소드 추가
    public void updateDetails(String title, String description, TaskStatus status, LocalDateTime deadline) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.deadline = deadline;
    }

    public void updateStatus(TaskStatus status) {
        this.status = status;
    }
}