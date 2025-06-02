package com.example.softengineerwebpr.domain.group.repository;

import com.example.softengineerwebpr.domain.group.entity.Group;
import com.example.softengineerwebpr.domain.group.entity.GroupMember;
import com.example.softengineerwebpr.domain.group.entity.GroupMemberId;
import com.example.softengineerwebpr.domain.project.entity.Project;
import com.example.softengineerwebpr.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional; // Optional 임포트 추가

public interface GroupMemberRepository extends JpaRepository<GroupMember, GroupMemberId> {

    @Query("SELECT gm FROM GroupMember gm WHERE gm.user = :user AND gm.group.project = :project")
    List<GroupMember> findByUserAndGroup_Project(@Param("user") User user, @Param("project") Project project);

    List<GroupMember> findByUser(User user);
    List<GroupMember> findByGroup(Group group);

    /**
     * 특정 사용자와 특정 그룹으로 그룹 멤버 정보를 조회합니다.
     * @param user 사용자 엔티티
     * @param group 그룹 엔티티
     * @return Optional<GroupMember> 객체
     */
    Optional<GroupMember> findByUserAndGroup(User user, Group group); // <<< 이 메소드 추가
}