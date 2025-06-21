package com.example.softengineerwebpr.domain.notification.dto;

import com.example.softengineerwebpr.common.entity.NotificationType;
import com.example.softengineerwebpr.domain.notification.entity.Notification;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NotificationResponseDto {
    private Long idx;
    private String content;
    private NotificationType type;
    private boolean isRead;
    private LocalDateTime createdAt;
    private Long referenceIdx;
    private String referenceTypeString;

    @Builder
    public NotificationResponseDto(Long idx, String content, NotificationType type, boolean isRead, LocalDateTime createdAt, Long referenceIdx, String referenceTypeString) {
        this.idx = idx;
        this.content = content;
        this.type = type;
        this.isRead = isRead;
        this.createdAt = createdAt;
        this.referenceIdx = referenceIdx;
        this.referenceTypeString = referenceTypeString;
    }

    public static NotificationResponseDto fromEntity(Notification notification) {
        if (notification == null) return null;
        return NotificationResponseDto.builder()
                .idx(notification.getIdx())
                .content(notification.getContent())
                .type(notification.getType())
                .isRead(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .referenceIdx(notification.getReferenceIdx())
                .referenceTypeString(notification.getReferenceTypeString())
                .build();
    }
}