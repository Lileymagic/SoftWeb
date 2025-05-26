package com.example.softengineerwebpr.domain.group.entity;

import com.example.softengineerwebpr.domain.project.entity.Project;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "`group`") // group은 SQL 예약어일 수 있으므로 backtick 사용
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_idx", nullable = false, foreignKey = @ForeignKey(name = "fk_group_project"))
    private Project project;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 20)
    private String color;

    @Column(name = "permission", nullable = true) // DB 컬럼명 'permission'과 일치시키기 위해 필드명 수정 또는 @Column(name="permission") 사용
    private Integer permission; // 필드명을 permission (단수형)으로 수정

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupMember> members = new ArrayList<>();

    @Builder
    public Group(Project project, String name, String color, Integer permission) { // 생성자 파라미터명도 permission으로 수정
        this.project = project;
        this.name = name;
        this.color = color;
        this.permission = permission != null ? permission : 0; // 기본값 0 (권한 없음)
    }

    public void updateDetails(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void setPermission(Integer permission) { // Setter 메서드명도 permission으로 수정
        this.permission = permission;
    }

    // Getter는 Lombok @Getter가 자동으로 생성해주므로 별도 수정 필요 없음 (getPermission()이 됨)
}