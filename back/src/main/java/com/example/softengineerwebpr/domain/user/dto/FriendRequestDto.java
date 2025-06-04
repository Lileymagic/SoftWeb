package com.example.softengineerwebpr.domain.user.dto; //

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FriendRequestDto {
    @NotNull(message = "친구 요청을 받을 사용자의 ID는 필수입니다.")
    private Long recipientUserId;
}