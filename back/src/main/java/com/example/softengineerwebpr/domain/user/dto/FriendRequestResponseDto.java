package com.example.softengineerwebpr.domain.user.dto; //

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class FriendRequestResponseDto {
    private UserSearchResponseDto user; // 요청자 또는 수신자 정보
    private LocalDateTime requestedAt;
    private String status; // 예: "요청"

    @Builder
    public FriendRequestResponseDto(UserSearchResponseDto user, LocalDateTime requestedAt, String status) {
        this.user = user;
        this.requestedAt = requestedAt;
        this.status = status;
    }
}