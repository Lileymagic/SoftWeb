package com.example.softengineerwebpr.domain.group.entity;

import com.example.softengineerwebpr.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// GroupMemberId 클래스가 같은 패키지 내에 별도 파일로 존재하므로 별도의 import는 필요 없습니다.

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "group_member",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_idx", "group_idx"})
        }
)
@IdClass(GroupMemberId.class) // 외부의 GroupMemberId.java 클래스를 참조
public class GroupMember {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx", nullable = false, foreignKey = @ForeignKey(name = "fk_group_member_user"))
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_idx", nullable = false, foreignKey = @ForeignKey(name = "fk_group_member_group"))
    private Group group;

    @Builder
    public GroupMember(User user, Group group) {
        this.user = user;
        this.group = group;
    }
}