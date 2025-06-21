package com.example.softengineerwebpr.domain.notification.service; //

import com.example.softengineerwebpr.common.entity.NotificationType; //
import com.example.softengineerwebpr.common.exception.BusinessLogicException; //
import com.example.softengineerwebpr.common.exception.ErrorCode; //
import com.example.softengineerwebpr.domain.notification.entity.Notification; //
import com.example.softengineerwebpr.domain.notification.repository.NotificationRepository; //
import com.example.softengineerwebpr.domain.project.entity.Project; //
import com.example.softengineerwebpr.domain.user.entity.User; //
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    /**
     * 프로젝트 초대 알림을 생성합니다. (범용 메소드 호출 방식으로 리팩토링)
     */
    @Override
    public Notification createProjectInvitationNotification(User inviter, User invitee, Project project) {
        String content = String.format("'%s'님이 '%s' 프로젝트에 당신을 초대했습니다.",
                inviter.getNickname(), project.getTitle());

        // 새로 추가된 범용 알림 생성 메소드를 호출하여 코드 중복을 줄입니다.
        return createAndSendNotification(invitee, NotificationType.PROJECT_INVITED, content, project.getIdx(), "PROJECT");
    }

    /**
     * 범용 알림 생성 및 저장 메소드
     */
    @Override
    public Notification createAndSendNotification(User targetUser, NotificationType type, String content, Long referenceIdx, String referenceTypeString) {
        if (targetUser == null || type == null || content == null || content.isEmpty()) {
            log.warn("알림 생성 실패: 필수 파라미터 누락 (targetUser, type, content)");
            throw new BusinessLogicException(ErrorCode.INVALID_INPUT_VALUE, "알림 생성을 위한 필수 정보가 누락되었습니다.");
        }

        Notification notification = Notification.builder()
                .user(targetUser)                       // 알림을 받을 사용자
                .type(type)                             // 알림 유형
                .content(content)                       // 알림 내용
                .referenceIdx(referenceIdx)             // 관련 대상 ID
                .referenceTypeString(referenceTypeString) // 관련 대상 타입
                .build(); //

        Notification savedNotification = notificationRepository.save(notification);
        log.info("새 알림 생성: type={}, toUser={}, content='{}'",
                type.name(), targetUser.getNickname(), content);

        // TODO: 실시간 알림 전송 로직 (예: WebSocket, SSE)이 필요하다면 여기서 호출합니다.

        return savedNotification;
    }

    /**
     * 특정 사용자의 모든 알림 목록을 최신순으로 조회합니다.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Notification> getNotificationsForUser(User user) {
        return notificationRepository.findByUserOrderByCreatedAtDesc(user); //
    }

    /**
     * 특정 사용자의 읽지 않은 알림 개수를 조회합니다.
     */
    @Override
    @Transactional(readOnly = true)
    public long getUnreadNotificationCount(User user) {
        return notificationRepository.countByUserAndIsReadFalse(user); //
    }

    /**
     * 특정 알림을 읽음 상태로 변경합니다.
     */
    @Override
    public void markNotificationAsRead(Long notificationId, User currentUser) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.RESOURCE_NOT_FOUND, "알림을 찾을 수 없습니다."));

        // 알림의 소유권 확인
        if (!notification.getUser().getIdx().equals(currentUser.getIdx())) {
            throw new BusinessLogicException(ErrorCode.ACCESS_DENIED, "해당 알림에 접근할 권한이 없습니다.");
        }

        notification.markAsRead(); // Notification 엔티티의 메소드 호출
        log.info("알림 읽음 처리: notificationId={}, userId={}", notificationId, currentUser.getNickname());
        // @Transactional에 의해 변경 감지(Dirty Checking)가 되어 별도의 save 호출이 필요 없습니다.
    }

    /**
     * 특정 사용자의 모든 알림을 읽음 상태로 변경합니다.
     */
    @Override
    public void markAllNotificationsAsRead(User currentUser) {
        List<Notification> unreadNotifications = notificationRepository.findByUserOrderByCreatedAtDesc(currentUser).stream()
                .filter(n -> !n.isRead())
                .collect(Collectors.toList());

        unreadNotifications.forEach(Notification::markAsRead); //
        log.info("{} 사용자의 모든 알림 읽음 처리", currentUser.getNickname());
    }

    /**
     * 특정 알림을 삭제합니다.
     */
    @Override
    public void deleteNotification(Long notificationId, User currentUser) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.RESOURCE_NOT_FOUND, "알림을 찾을 수 없습니다."));

        // 알림 삭제 권한 확인
        if (!notification.getUser().getIdx().equals(currentUser.getIdx())) {
            throw new BusinessLogicException(ErrorCode.ACCESS_DENIED, "해당 알림을 삭제할 권한이 없습니다.");
        }

        notificationRepository.delete(notification);
        log.info("알림 삭제: notificationId={}, userId={}", notificationId, currentUser.getNickname());
    }
}