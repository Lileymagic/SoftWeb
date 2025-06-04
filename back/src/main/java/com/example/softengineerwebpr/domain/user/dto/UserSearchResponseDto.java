package com.example.softengineerwebpr.domain.user.dto; //

import com.example.softengineerwebpr.domain.user.entity.User; //
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserSearchResponseDto {
    private Long userIdx;
    private String nickname;
    private String identificationCode;
    private String profileImage;
    private String email; // 명세서 및 기존 DTO 참고
    private String friendStatus; // 친구 상태: "NONE", "PENDING_SENT", "PENDING_RECEIVED", "ACCEPTED", "SELF"

    @Builder
    public UserSearchResponseDto(Long userIdx, String nickname, String identificationCode, String profileImage, String email, String friendStatus) {
        this.userIdx = userIdx;
        this.nickname = nickname;
        this.identificationCode = identificationCode;
        this.profileImage = profileImage;
        this.email = email;
        this.friendStatus = friendStatus;
    }

    public static UserSearchResponseDto fromUser(User user, String friendStatus) {
        if (user == null) return null;
        return UserSearchResponseDto.builder()
                .userIdx(user.getIdx())
                .nickname(user.getNickname()) //
                .identificationCode(user.getIdentificationCode()) //
                .profileImage(user.getProfileImage()) //
                .email(user.getEmail()) //
                .friendStatus(friendStatus)
                .build();
    }
}