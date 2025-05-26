package com.example.softengineerwebpr.domain.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProjectInviteRequestDto {

    @NotNull(message = "초대할 사용자 IDX는 필수입니다.") // 또는 닉네임/식별코드를 받을 DTO로 변경
    private Long inviteeUserId; // 초대받는 사람의 User IDX

    // 만약 닉네임#코드로 검색하여 초대한다면 아래 필드 사용
    // private String inviteeNickname;
    // private String inviteeIdentificationCode;
}