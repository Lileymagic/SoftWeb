package com.example.softengineerwebpr.domain.user.entity;

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
@Table(name = "friend") // DB 테이블명 "friend" [cite: 25]
@IdClass(FriendId.class) // 복합 키 클래스 지정
@EntityListeners(AuditingEntityListener.class) // @CreatedDate 사용 위함
public class Friend {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_idx", referencedColumnName = "idx", nullable = false) // DB 컬럼명 "requester_idx"
    private User requester; // 친구 요청을 보낸 사용자

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_idx", referencedColumnName = "idx", nullable = false) // DB 컬럼명 "recipient_idx"
    private User recipient; // 친구 요청을 받은 사용자

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FriendStatus status; // ENUM: 요청, 수락, 거절 [cite: 25]

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false) // DB 컬럼명 "created_at" [cite: 25]
    private LocalDateTime createdAt;

    @Builder
    public Friend(User requester, User recipient, FriendStatus status) {
        this.requester = requester;
        this.recipient = recipient;
        this.status = status;
        // createdAt은 @CreatedDate에 의해 자동 설정
    }

    public void updateStatus(FriendStatus newStatus) {
        this.status = newStatus;
        // 필요하다면 상태 변경 시각을 기록하는 별도 필드(예: updatedAt)를 추가하고 여기서 업데이트할 수 있습니다.
        // 현재 스키마에는 created_at만 명시되어 있으므로 추가 로직은 생략합니다. [cite: 25]
    }
}