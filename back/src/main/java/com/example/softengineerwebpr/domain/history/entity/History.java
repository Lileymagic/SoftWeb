package com.example.softengineerwebpr.domain.history.entity;

import com.example.softengineerwebpr.domain.project.entity.Project;
import com.example.softengineerwebpr.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "history")
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_idx", nullable = false, foreignKey = @ForeignKey(name = "fk_history_project"))
    private Project project;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false)
    private HistoryActionType actionType;

    @Column(name = "target_id")
    private Long targetId; // 행동 대상의 ID (예: 업무 ID, 사용자 ID)

    @Column(columnDefinition = "TEXT")
    private String description; // 상세 설명

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false, foreignKey = @ForeignKey(name = "fk_history_user_created_by"))
    private User createdBy; // 행동을 한 사용자

    @Builder
    public History(Project project, HistoryActionType actionType, Long targetId, String description, User createdBy) {
        this.project = project;
        this.actionType = actionType;
        this.targetId = targetId;
        this.description = description;
        this.createdBy = createdBy;
    }
}