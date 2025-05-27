package com.example.softengineerwebpr.domain.notification.entity;

import com.example.softengineerwebpr.common.entity.NotificationType;
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
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx", nullable = false, foreignKey = @ForeignKey(name = "fk_notification_user"))
    private User user; // 알림을 받을 사용자

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 50)
    private NotificationType type; // 알림 종류 (Enum 사용)

    @Lob // TEXT 타입 매핑
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content; // 알림 내용

    @Column(name = "is_read", nullable = false)
    private boolean isRead = false; // 읽음 여부, 기본값 false

    @Column(name = "reference_idx", nullable = true)
    private Long referenceIdx; // 관련 대상의 ID (예: 프로젝트 ID, 업무 ID, 친구 요청 ID 등)

    @Column(name = "reference_type", nullable = true, length = 50) // 예: "PROJECT", "TASK", "USER" (친구 요청의 경우 요청자 User ID)
    private String referenceTypeString; // 관련 대상의 타입 문자열

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public Notification(User user, NotificationType type, String content, Long referenceIdx, String referenceTypeString) {
        this.user = user;
        this.type = type;
        this.content = content;
        this.referenceIdx = referenceIdx;
        this.referenceTypeString = referenceTypeString;
        this.isRead = false; // 생성 시 항상 false
    }

    public void markAsRead() {
        this.isRead = true;
    }
}