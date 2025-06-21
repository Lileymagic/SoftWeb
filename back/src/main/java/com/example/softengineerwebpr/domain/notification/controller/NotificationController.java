package com.example.softengineerwebpr.domain.notification.controller;

import com.example.softengineerwebpr.common.dto.ApiResponse;
import com.example.softengineerwebpr.common.util.AuthenticationUtil;
import com.example.softengineerwebpr.domain.notification.dto.NotificationResponseDto;
import com.example.softengineerwebpr.domain.notification.entity.Notification;
import com.example.softengineerwebpr.domain.notification.service.NotificationService;
import com.example.softengineerwebpr.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications") // 알림 관련 API는 /api/notifications 경로 사용
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final AuthenticationUtil authenticationUtil;

    /**
     * 현재 로그인한 사용자의 모든 알림 목록을 조회합니다.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationResponseDto>>> getUserNotifications(Authentication authentication) {
        User currentUser = authenticationUtil.getCurrentUser(authentication);
        List<Notification> notifications = notificationService.getNotificationsForUser(currentUser);
        List<NotificationResponseDto> responseDtos = notifications.stream()
                .map(NotificationResponseDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "알림 목록 조회 성공", responseDtos));
    }

    /**
     * 현재 로그인한 사용자의 읽지 않은 알림 개수를 조회합니다.
     */
    @GetMapping("/unread-count")
    public ResponseEntity<ApiResponse<Long>> getUnreadNotificationCount(Authentication authentication) {
        User currentUser = authenticationUtil.getCurrentUser(authentication);
        long count = notificationService.getUnreadNotificationCount(currentUser);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "읽지 않은 알림 개수 조회 성공", count));
    }

    /**
     * 특정 알림을 읽음 상태로 변경합니다.
     */
    @PostMapping("/{notificationId}/read") // 상태 변경이므로 POST 또는 PATCH 사용
    public ResponseEntity<ApiResponse<Void>> markNotificationAsRead(@PathVariable Long notificationId, Authentication authentication) {
        User currentUser = authenticationUtil.getCurrentUser(authentication);
        notificationService.markNotificationAsRead(notificationId, currentUser);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "알림을 읽음 처리했습니다."));
    }

    /**
     * 현재 로그인한 사용자의 모든 알림을 읽음 상태로 변경합니다.
     */
    @PostMapping("/read-all")
    public ResponseEntity<ApiResponse<Void>> markAllNotificationsAsRead(Authentication authentication) {
        User currentUser = authenticationUtil.getCurrentUser(authentication);
        notificationService.markAllNotificationsAsRead(currentUser);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "모든 알림을 읽음 처리했습니다."));
    }

    /**
     * 특정 알림을 삭제합니다.
     */
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(@PathVariable Long notificationId, Authentication authentication) {
        User currentUser = authenticationUtil.getCurrentUser(authentication);
        notificationService.deleteNotification(notificationId, currentUser);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "알림을 삭제했습니다."));
    }
}