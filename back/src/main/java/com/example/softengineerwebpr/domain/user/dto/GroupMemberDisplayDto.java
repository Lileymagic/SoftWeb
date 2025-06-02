package com.example.softengineerwebpr.domain.user.dto; // 또는 com.example.softengineerwebpr.domain.group.dto 등 적절한 위치

import com.example.softengineerwebpr.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GroupMemberDisplayDto {
    private Long userIdx;
    private String nickname;
    private String identificationCode; // #태그 앞부분
    private String profileImage;

    @Builder
    public GroupMemberDisplayDto(Long userIdx, String nickname, String identificationCode, String profileImage) {
        this.userIdx = userIdx;
        this.nickname = nickname;
        this.identificationCode = identificationCode;
        this.profileImage = profileImage;
    }

    public static GroupMemberDisplayDto fromEntity(User user) {
        if (user == null) return null;
        return GroupMemberDisplayDto.builder()
                .userIdx(user.getIdx())
                .nickname(user.getNickname())
                .identificationCode(user.getIdentificationCode())
                .profileImage(user.getProfileImage())
                .build();
    }
}