package com.example.softengineerwebpr.domain.project.entity; // ProjectMember 엔티티와 같은 패키지로 가정

import java.io.Serializable;
import java.util.Objects;

public class ProjectMemberId implements Serializable {

    private Long user;    // ProjectMember 엔티티의 'user' 필드(User 타입)의 ID 타입과 일치 (User의 idx가 Long)
    private Long project; // ProjectMember 엔티티의 'project' 필드(Project 타입)의 ID 타입과 일치 (Project의 idx가 Long)

    // JPA IdClass는 기본 생성자가 필요합니다.
    public ProjectMemberId() {
    }

    public ProjectMemberId(Long user, Long project) {
        this.user = user;
        this.project = project;
    }

    // Getter (JPA 명세상 IdClass의 필드에 접근하기 위해 필요할 수 있음)
    public Long getUser() {
        return user;
    }

    public Long getProject() {
        return project;
    }

    // Setter (선택적, IdClass는 불변으로 만들기도 함)
    public void setUser(Long user) {
        this.user = user;
    }

    public void setProject(Long project) {
        this.project = project;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectMemberId that = (ProjectMemberId) o;
        return Objects.equals(user, that.user) &&
                Objects.equals(project, that.project);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, project);
    }
}