package com.example.softengineerwebpr.domain.user.service.impl; //

import com.example.softengineerwebpr.common.entity.NotificationType; //
import com.example.softengineerwebpr.common.exception.BusinessLogicException;
import com.example.softengineerwebpr.common.exception.ErrorCode; //
import com.example.softengineerwebpr.domain.notification.service.NotificationService; //
import com.example.softengineerwebpr.domain.user.dto.FriendRequestResponseDto;
import com.example.softengineerwebpr.domain.user.dto.UserSearchResponseDto;
import com.example.softengineerwebpr.domain.user.entity.Friend;
import com.example.softengineerwebpr.domain.user.entity.FriendStatus;
import com.example.softengineerwebpr.domain.user.entity.User; //
import com.example.softengineerwebpr.domain.user.repository.FriendRepository;
import com.example.softengineerwebpr.domain.user.repository.UserRepository; //
import com.example.softengineerwebpr.domain.user.service.FriendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FriendServiceImpl implements FriendService {

    private final UserRepository userRepository;
    private final FriendRepository friendRepository;
    private final NotificationService notificationService; // 의존성은 유지

    @Override
    @Transactional(readOnly = true)
    public List<UserSearchResponseDto> searchUsersByNickname(String nicknameQuery, User currentUser) {
        List<User> foundUsers = userRepository.findAllByNicknameContainingIgnoreCase(nicknameQuery); //

        return foundUsers.stream()
                .filter(user -> !user.getIdx().equals(currentUser.getIdx()))
                .map(user -> {
                    Optional<Friend> relationOpt = friendRepository.findExistingRelation(currentUser, user);
                    String statusString = "NONE";
                    if (relationOpt.isPresent()) {
                        Friend relation = relationOpt.get();
                        if (relation.getStatus() == FriendStatus.수락) {
                            statusString = "ACCEPTED";
                        } else if (relation.getStatus() == FriendStatus.요청) {
                            if (relation.getRequester().equals(currentUser)) {
                                statusString = "PENDING_SENT";
                            } else {
                                statusString = "PENDING_RECEIVED";
                            }
                        } else if (relation.getStatus() == FriendStatus.거절) {
                            statusString = "NONE";
                        }
                    }
                    return UserSearchResponseDto.fromUser(user, statusString);
                })
                .collect(Collectors.toList());
    }

    @Override
    public void sendFriendRequest(Long recipientUserId, User requester) {
        User recipient = userRepository.findById(recipientUserId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.USER_NOT_FOUND, "친구 요청을 받을 사용자를 찾을 수 없습니다."));

        if (requester.getIdx().equals(recipientUserId)) {
            throw new BusinessLogicException(ErrorCode.INVALID_INPUT_VALUE, "자기 자신에게 친구 요청을 보낼 수 없습니다.");
        }

        Optional<Friend> existingRelation = friendRepository.findExistingRelation(requester, recipient);
        if (existingRelation.isPresent()) {
            Friend friend = existingRelation.get();
            if (friend.getStatus() == FriendStatus.수락) {
                throw new BusinessLogicException(ErrorCode.ALREADY_FRIEND);
            } else if (friend.getStatus() == FriendStatus.요청) {
                throw new BusinessLogicException(ErrorCode.FRIEND_REQUEST_ALREADY_SENT);
            }
            friendRepository.delete(friend);
            log.info("기존 친구 관계(거절 등) 삭제 후 새로 요청: 요청자 {}, 수신자 {}", requester.getNickname(), recipient.getNickname());
        }

        Friend friendRequest = Friend.builder()
                .requester(requester)
                .recipient(recipient)
                .status(FriendStatus.요청)
                .build();
        friendRepository.save(friendRequest);
        log.info("친구 요청 발송: 요청자 {}, 수신자 {}", requester.getNickname(), recipient.getNickname());

        // 알림 생성 (수신자에게) - 주석 처리
        /*
        String notificationContent = String.format("'%s#%s'님이 친구 요청을 보냈습니다.",
                requester.getNickname(), requester.getIdentificationCode()); //
        notificationService.createAndSendNotification(recipient, NotificationType.FRIEND_REQUEST_RECEIVED, notificationContent, requester.getIdx(), "USER"); //
        */
    }

    @Override
    public void acceptFriendRequest(Long requesterUserId, User accepter) {
        User requester = userRepository.findById(requesterUserId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.USER_NOT_FOUND, "요청한 사용자를 찾을 수 없습니다."));

        Friend friendRequest = friendRepository.findByRequesterAndRecipient(requester, accepter)
                .filter(f -> f.getStatus() == FriendStatus.요청)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.FRIEND_REQUEST_NOT_FOUND, "처리할 친구 요청이 없거나 이미 처리된 요청입니다."));

        friendRequest.updateStatus(FriendStatus.수락);
        friendRepository.save(friendRequest);
        log.info("친구 요청 수락: 수락자 {}, 요청자 {}", accepter.getNickname(), requester.getNickname());

        // 알림 생성 (요청자에게) - 주석 처리
        /*
        String notificationContent = String.format("'%s#%s'님이 친구 요청을 수락했습니다.",
                accepter.getNickname(), accepter.getIdentificationCode()); //
        notificationService.createAndSendNotification(requester, NotificationType.FRIEND_REQUEST_ACCEPTED, notificationContent, accepter.getIdx(), "USER"); //
        */
    }

    @Override
    public void rejectFriendRequest(Long requesterUserId, User rejecter) {
        User requester = userRepository.findById(requesterUserId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.USER_NOT_FOUND, "요청한 사용자를 찾을 수 없습니다."));

        Friend friendRequest = friendRepository.findByRequesterAndRecipient(requester, rejecter)
                .filter(f -> f.getStatus() == FriendStatus.요청)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.FRIEND_REQUEST_NOT_FOUND, "처리할 친구 요청이 없거나 이미 처리된 요청입니다."));

        friendRequest.updateStatus(FriendStatus.거절);
        friendRepository.save(friendRequest);
        log.info("친구 요청 거절: 거절자 {}, 요청자 {}", rejecter.getNickname(), requester.getNickname());

        // 알림 생성 (요청자에게) - 주석 처리
        /*
        String notificationContent = String.format("'%s#%s'님이 친구 요청을 거절했습니다.",
                rejecter.getNickname(), rejecter.getIdentificationCode()); //
        notificationService.createAndSendNotification(requester, NotificationType.FRIEND_REQUEST_REJECTED, notificationContent, rejecter.getIdx(), "USER"); //
        */
    }

    @Override
    public void removeFriend(Long friendUserId, User currentUser) {
        User friendUser = userRepository.findById(friendUserId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.USER_NOT_FOUND, "삭제할 친구를 찾을 수 없습니다."));

        Friend friendRelation = friendRepository.findExistingRelation(currentUser, friendUser)
                .filter(f -> f.getStatus() == FriendStatus.수락)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.NOT_FRIEND));

        friendRepository.delete(friendRelation);
        log.info("친구 삭제: 사용자 {}, 삭제된 친구 {}", currentUser.getNickname(), friendUser.getNickname());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserSearchResponseDto> getFriendList(User currentUser) {
        List<Friend> friendRelations = friendRepository.findAllFriendsOfUserByStatus(currentUser, FriendStatus.수락);
        return friendRelations.stream()
                .map(friend -> {
                    User friendUser = friend.getRequester().equals(currentUser) ? friend.getRecipient() : friend.getRequester();
                    return UserSearchResponseDto.fromUser(friendUser, "ACCEPTED");
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FriendRequestResponseDto> getReceivedFriendRequests(User currentUser) {
        List<Friend> receivedRequests = friendRepository.findAllByRecipientAndStatusOrderByCreatedAtDesc(currentUser, FriendStatus.요청);
        return receivedRequests.stream()
                .map(request -> FriendRequestResponseDto.builder()
                        .user(UserSearchResponseDto.fromUser(request.getRequester(), "PENDING_RECEIVED"))
                        .requestedAt(request.getCreatedAt())
                        .status(request.getStatus().name())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FriendRequestResponseDto> getSentFriendRequests(User currentUser) {
        List<Friend> sentRequests = friendRepository.findAllByRequesterAndStatusOrderByCreatedAtDesc(currentUser, FriendStatus.요청);
        return sentRequests.stream()
                .map(request -> FriendRequestResponseDto.builder()
                        .user(UserSearchResponseDto.fromUser(request.getRecipient(), "PENDING_SENT"))
                        .requestedAt(request.getCreatedAt())
                        .status(request.getStatus().name())
                        .build())
                .collect(Collectors.toList());
    }
}