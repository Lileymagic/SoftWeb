package com.example.softengineerwebpr.domain.notification.service;

import com.example.softengineerwebpr.common.entity.NotificationType;
import com.example.softengineerwebpr.common.exception.BusinessLogicException;
import com.example.softengineerwebpr.common.exception.ErrorCode;
import com.example.softengineerwebpr.domain.notification.entity.Notification;
import com.example.softengineerwebpr.domain.notification.repository.NotificationRepository;
import com.example.softengineerwebpr.domain.project.entity.Project;
import com.example.softengineerwebpr.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public Notification createProjectInvitationNotification(User inviter, User invitee, Project project) {
        String content = String.format("'%s'님이 '%s' 프로젝트에 당신을 초대했습니다.",
                inviter.getNickname(), project.getTitle());

        Notification notification = Notification.builder()
                .user(invitee) // 알림을 받는 사람
                .type(NotificationType.PROJECT_INVITED)
                .content(content)
                .referenceIdx(project.getIdx()) // 관련 프로젝트 ID
                .referenceTypeString("PROJECT") // 관련 타입
                .build();
        Notification savedNotification = notificationRepository.save(notification);
        log.info("프로젝트 초대 알림 생성: toUser={}, fromUser={}, projectTitle={}", invitee.getNickname(), inviter.getNickname(), project.getTitle());
        return savedNotification;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getNotificationsForUser(User user) {
        // TODO: 페이징 처리 고려
        return notificationRepository.findByUserOrderByCreatedAtDesc(user);
    }

    @Override
    @Transactional(readOnly = true)
    public long getUnreadNotificationCount(User user) {
        return notificationRepository.countByUserAndIsReadFalse(user);
    }

    @Override
    public void markNotificationAsRead(Long notificationId, User currentUser) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.RESOURCE_NOT_FOUND, "알림을 찾을 수 없습니다."));
        if (!notification.getUser().equals(currentUser)) {
            throw new BusinessLogicException(ErrorCode.ACCESS_DENIED, "해당 알림에 접근할 권한이 없습니다.");
        }
        notification.markAsRead();
        // notificationRepository.save(notification); // @Transactional에 의해 변경 감지
        log.info("알림 읽음 처리: notificationId={}, userId={}", notificationId, currentUser.getNickname());
    }

    @Override
    public void markAllNotificationsAsRead(User currentUser) {
        List<Notification> unreadNotifications = notificationRepository.findByUserOrderByCreatedAtDesc(currentUser).stream()
                .filter(n -> !n.isRead())
                .collect(java.util.stream.Collectors.toList());
        unreadNotifications.forEach(Notification::markAsRead);
        // notificationRepository.saveAll(unreadNotifications); // @Transactional에 의해 변경 감지
        log.info("{} 사용자의 모든 알림 읽음 처리", currentUser.getNickname());
    }

    @Override
    public void deleteNotification(Long notificationId, User currentUser) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.RESOURCE_NOT_FOUND, "알림을 찾을 수 없습니다."));
        if (!notification.getUser().equals(currentUser)) {
            throw new BusinessLogicException(ErrorCode.ACCESS_DENIED, "해당 알림을 삭제할 권한이 없습니다.");
        }
        notificationRepository.delete(notification);
        log.info("알림 삭제: notificationId={}, userId={}", notificationId, currentUser.getNickname());
    }
}