package com.example.softengineerwebpr.domain.notification.service;

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
}