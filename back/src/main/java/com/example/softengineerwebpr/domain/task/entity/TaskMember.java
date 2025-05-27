package com.example.softengineerwebpr.domain.task.entity;

import com.example.softengineerwebpr.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "task_member")
@IdClass(TaskMemberId.class)
public class TaskMember {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx", referencedColumnName = "idx", foreignKey = @ForeignKey(name = "fk_task_member_user"))
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_idx", referencedColumnName = "idx", foreignKey = @ForeignKey(name = "fk_task_member_task"))
    private Task task;

    @Builder
    public TaskMember(User user, Task task) {
        this.user = user;
        this.task = task;
    }
}