package com.example.softengineerwebpr.domain.notification.repository;

import com.example.softengineerwebpr.domain.notification.entity.Notification;
import com.example.softengineerwebpr.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserOrderByCreatedAtDesc(User user);
    long countByUserAndIsReadFalse(User user);
}