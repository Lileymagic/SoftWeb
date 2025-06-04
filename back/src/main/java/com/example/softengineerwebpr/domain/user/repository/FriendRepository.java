package com.example.softengineerwebpr.domain.user.repository;

import com.example.softengineerwebpr.domain.user.entity.Friend;
import com.example.softengineerwebpr.domain.user.entity.FriendId;
import com.example.softengineerwebpr.domain.user.entity.FriendStatus;
import com.example.softengineerwebpr.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, FriendId> {

    // 특정 요청자와 특정 수신자 사이의 관계 조회
    Optional<Friend> findByRequesterAndRecipient(User requester, User recipient);

    // 두 사용자 간의 관계 조회 (요청자-수신자 순서 무관하게)
    @Query("SELECT f FROM Friend f WHERE (f.requester = :userA AND f.recipient = :userB) OR (f.requester = :userB AND f.recipient = :userA)")
    Optional<Friend> findExistingRelation(@Param("userA") User userA, @Param("userB") User userB);

    // 특정 사용자의 모든 친구 목록 조회 (상태: 수락)
    @Query("SELECT f FROM Friend f WHERE (f.requester = :user OR f.recipient = :user) AND f.status = :status")
    List<Friend> findAllFriendsOfUserByStatus(@Param("user") User user, @Param("status") FriendStatus status);

    // 특정 사용자가 받은 친구 요청 목록 조회 (현재 사용자가 수신자이고 상태가 '요청')
    List<Friend> findAllByRecipientAndStatusOrderByCreatedAtDesc(User recipient, FriendStatus status);

    // 특정 사용자가 보낸 친구 요청 목록 조회 (현재 사용자가 요청자이고 상태가 '요청')
    List<Friend> findAllByRequesterAndStatusOrderByCreatedAtDesc(User requester, FriendStatus status);

    // 두 사용자 간에 특정 상태의 관계가 존재하는지 확인
    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END " +
            "FROM Friend f WHERE ((f.requester = :userA AND f.recipient = :userB) OR (f.requester = :userB AND f.recipient = :userA)) " +
            "AND f.status = :status")
    boolean existsRelationBetweenUsersWithStatus(@Param("userA") User userA, @Param("userB") User userB, @Param("status") FriendStatus status);

    // 두 사용자 간에 어떤 상태로든 관계가 이미 존재하는지 확인
    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END " +
            "FROM Friend f WHERE (f.requester = :userA AND f.recipient = :userB) OR (f.requester = :userB AND f.recipient = :userA)")
    boolean existsRelationBetweenUsers(@Param("userA") User userA, @Param("userB") User userB);

}