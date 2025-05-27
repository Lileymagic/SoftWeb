package com.example.softengineerwebpr.domain.group.repository;

import com.example.softengineerwebpr.domain.group.entity.Group;
import com.example.softengineerwebpr.domain.group.entity.GroupMember;
import com.example.softengineerwebpr.domain.group.entity.GroupMemberId;
import com.example.softengineerwebpr.domain.project.entity.Project; // Project 임포트
import com.example.softengineerwebpr.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // Query 임포트
import org.springframework.data.repository.query.Param; // Param 임포트
import java.util.List;

public interface GroupMemberRepository extends JpaRepository<GroupMember, GroupMemberId> {

    // 수정된 메서드: JPQL을 사용하여 명시적으로 쿼리 작성
    @Query("SELECT gm FROM GroupMember gm WHERE gm.user = :user AND gm.group.project = :project")
    List<GroupMember> findByUserAndGroup_Project(@Param("user") User user, @Param("project") Project project);
    // 메서드 이름을 findByUserAndGroup_Project로 변경하여 Spring Data JPA의 파싱 규칙과 유사하게 하거나,
    // 혹은 원래 이름 findByUserAndProject를 유지하고 @Query만 사용해도 됩니다. 여기서는 의미를 명확히 하기 위해 변경.

    List<GroupMember> findByUser(User user);
    List<GroupMember> findByGroup(Group group);
}