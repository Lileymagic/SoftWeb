package com.example.softengineerwebpr.domain.group.entity;

import java.io.Serializable;
import java.util.Objects;

public class GroupMemberId implements Serializable {
    private Long user;  // User의 Idx 타입과 일치
    private Long group; // Group의 Idx 타입과 일치

    // JPA IdClass는 기본 생성자가 필요합니다.
    public GroupMemberId() {}

    public GroupMemberId(Long user, Long group) {
        this.user = user;
        this.group = group;
    }

    // Getter와 Setter가 필요할 수 있으나, JPA는 필드 직접 접근도 가능합니다.
    // 명확성을 위해 추가하거나, Lombok의 @Data, @Getter, @Setter 등을 사용할 수도 있습니다.
    // (다만, IdClass는 불변(immutable)으로 만드는 경우가 많아 Setter는 제외하기도 합니다.)
    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public Long getGroup() {
        return group;
    }

    public void setGroup(Long group) {
        this.group = group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupMemberId that = (GroupMemberId) o;
        return Objects.equals(user, that.user) && Objects.equals(group, that.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, group);
    }
}