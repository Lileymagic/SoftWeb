package com.example.softengineerwebpr.domain.user.dto;

import com.example.softengineerwebpr.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserBasicInfoDto {
    private Long userIdx;
    private String nickname;
    private String profileImage; // 필요시

    @Builder
    public UserBasicInfoDto(Long userIdx, String nickname, String profileImage) {
        this.userIdx = userIdx;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public static UserBasicInfoDto fromEntity(User user) {
        if (user == null) return null;
        return UserBasicInfoDto.builder()
                .userIdx(user.getIdx())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .build();
    }
}