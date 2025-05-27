package com.example.softengineerwebpr.domain.task.entity;

import java.io.Serializable;
import java.util.Objects;

public class TaskMemberId implements Serializable {
    private Long user; // User 엔티티의 ID 타입 (idx)
    private Long task; // Task 엔티티의 ID 타입 (idx)

    public TaskMemberId() {}

    public TaskMemberId(Long user, Long task) {
        this.user = user;
        this.task = task;
    }

    // equals and hashCode (반드시 구현)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskMemberId that = (TaskMemberId) o;
        return Objects.equals(user, that.user) && Objects.equals(task, that.task);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, task);
    }
}