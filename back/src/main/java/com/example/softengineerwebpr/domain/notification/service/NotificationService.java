package com.example.softengineerwebpr.domain.notification.service;

import com.example.softengineerwebpr.common.entity.NotificationType;
import com.example.softengineerwebpr.domain.notification.entity.Notification;
import com.example.softengineerwebpr.domain.project.entity.Project;
import com.example.softengineerwebpr.domain.user.entity.User;
import java.util.List;

public interface NotificationService {
    Notification createProjectInvitationNotification(User inviter, User invitee, Project project);
    // 다른 알림 생성 메서드들 (추후 추가) ...

    List<Notification> getNotificationsForUser(User user);
    long getUnreadNotificationCount(User user);
    void markNotificationAsRead(Long notificationId, User currentUser);
    void markAllNotificationsAsRead(User currentUser);
    void deleteNotification(Long notificationId, User currentUser);

    /**
     * 범용 알림 생성 및 저장 메소드 (새로 추가)
     * @param targetUser 알림을 받을 사용자
     * @param type 알림 유형
     * @param content 알림 내용
     * @param referenceIdx 관련 대상의 ID (예: 요청자 ID, 프로젝트 ID, 업무 ID 등)
     * @param referenceTypeString 관련 대상의 타입 문자열 (예: "USER", "PROJECT", "TASK")
     * @return 생성된 Notification 객체
     */
    Notification createAndSendNotification(User targetUser, NotificationType type, String content, Long referenceIdx, String referenceTypeString);
}