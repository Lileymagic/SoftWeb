package com.example.softengineerwebpr.domain.user.service; //

import com.example.softengineerwebpr.domain.user.dto.FriendRequestResponseDto;
import com.example.softengineerwebpr.domain.user.dto.UserSearchResponseDto;
import com.example.softengineerwebpr.domain.user.entity.User;

import java.util.List;

public interface FriendService {

    /**
     * 사용자를 닉네임으로 검색합니다.
     * @param nicknameQuery 검색할 닉네임 문자열
     * @param currentUser 현재 요청을 보낸 사용자
     * @return 검색된 사용자 목록 (UserSearchResponseDto 리스트)
     */
    List<UserSearchResponseDto> searchUsersByNickname(String nicknameQuery, User currentUser); // 메소드명 및 파라미터 변경

    void sendFriendRequest(Long recipientUserId, User requester);

    void acceptFriendRequest(Long requesterUserId, User accepter);

    void rejectFriendRequest(Long requesterUserId, User rejecter);

    void removeFriend(Long friendUserId, User currentUser);

    List<UserSearchResponseDto> getFriendList(User currentUser);

    List<FriendRequestResponseDto> getReceivedFriendRequests(User currentUser);

    List<FriendRequestResponseDto> getSentFriendRequests(User currentUser);
}