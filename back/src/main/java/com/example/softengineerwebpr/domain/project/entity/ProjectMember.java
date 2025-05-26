package com.example.softengineerwebpr.domain.project.entity;

import com.example.softengineerwebpr.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
// import org.springframework.data.jpa.domain.support.AuditingEntityListener; // 제거 (Audited 필드 없음)

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "project_member",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_idx", "project_idx"})
        }
)
@IdClass(ProjectMemberId.class)
// @EntityListeners(AuditingEntityListener.class) // Audited 필드가 없으므로 이 리스너도 제거합니다.
public class ProjectMember {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx", nullable = false, foreignKey = @ForeignKey(name = "fk_project_member_user"))
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_idx", nullable = false, foreignKey = @ForeignKey(name = "fk_project_member_project"))
    private Project project;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectMemberRole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false) // DB에 추가된 'status' 컬럼과 매핑
    private ProjectMemberStatus status;

    // invited_at, responded_at 필드 및 관련 @CreatedDate 어노테이션 제거됨

    @Builder
    public ProjectMember(User user, Project project, ProjectMemberRole role, ProjectMemberStatus status) {
        this.user = user;
        this.project = project;
        this.role = role;
        this.status = status;
    }

    public void updateRole(ProjectMemberRole role) {
        this.role = role;
    }

    public void acceptInvitation() {
        if (this.status == ProjectMemberStatus.PENDING) {
            this.status = ProjectMemberStatus.ACCEPTED;
            // respondedAt 설정 로직 제거됨
        } else {
            // 이미 처리된 초대에 대한 로깅 또는 예외 처리 (BusinessLogicException(ErrorCode.INVITATION_ALREADY_PROCESSED) 등)
            // 예: log.warn("초대 수락 시도: 이미 처리된 초대입니다. userId={}, projectId={}, currentStatus={}", user.getIdx(), project.getIdx(), this.status);
        }
    }

    public void rejectInvitation() {
        if (this.status == ProjectMemberStatus.PENDING) {
            this.status = ProjectMemberStatus.REJECTED; // 또는 서비스 계층에서 이 레코드를 삭제
            // respondedAt 설정 로직 제거됨
        } else {
            // 이미 처리된 초대에 대한 로깅 또는 예외 처리
            // 예: log.warn("초대 거절 시도: 이미 처리된 초대입니다. userId={}, projectId={}, currentStatus={}", user.getIdx(), project.getIdx(), this.status);
        }
    }
}