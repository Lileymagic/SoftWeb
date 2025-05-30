package com.example.softengineerwebpr.domain.comment.entity;

import com.example.softengineerwebpr.domain.post.entity.Post;
import com.example.softengineerwebpr.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class) // @CreatedDate, @LastModifiedDate 사용 위함
@Table(name = "comment") // DB 테이블 이름 명시
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_idx", nullable = false, foreignKey = @ForeignKey(name = "fk_comment_post"))
    private Post post; // 이 댓글이 속한 게시글

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx", nullable = false, foreignKey = @ForeignKey(name = "fk_comment_user"))
    private User user; // 댓글 작성자

    @Lob // TEXT 타입 매핑 (내용이 길 수 있으므로)
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at") // 수정일은 @LastModifiedDate로 자동 관리 가능
    private LocalDateTime updatedAt; // DB 스키마에는 있지만, BaseTimeEntity를 사용하지 않으므로 수동 또는 @LastModifiedDate 추가

    // 대댓글을 위한 자기 참조 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_idx", foreignKey = @ForeignKey(name = "fk_comment_parent"))
    private Comment parentComment; // 부모 댓글

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> childComments = new ArrayList<>(); // 자식 댓글 목록

    @Builder
    public Comment(Post post, User user, String content, Comment parentComment) {
        this.post = post;
        this.user = user;
        this.content = content;
        this.parentComment = parentComment;
        // this.createdAt은 @CreatedDate에 의해 자동 설정
    }

    // 내용 수정을 위한 메소드
    public void updateContent(String content) {
        this.content = content;
        this.updatedAt = LocalDateTime.now(); // 수정 시각 직접 업데이트
    }

    // 연관관계 편의 메소드 (양방향 관계 설정 시)
    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
        if (parentComment != null) {
            parentComment.getChildComments().add(this);
        }
    }
}