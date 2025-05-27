package com.example.softengineerwebpr.domain.user.service;

import com.example.softengineerwebpr.domain.user.dto.UserProfileUpdateRequestDto;
import com.example.softengineerwebpr.domain.user.dto.UserResponseDto;
import org.springframework.security.core.Authentication;

public interface UserService {

    /**
     * 현재 인증된 사용자의 프로필 정보를 조회합니다.
     * 이 메소드는 UserResponseDto를 반환하며, 일반 로그인 사용자의 경우 loginId를 포함하고,
     * 소셜 로그인 사용자의 경우 loginId는 null일 수 있습니다.
     *
     * @param authentication 현재 사용자의 인증 정보
     * @return 사용자의 프로필 정보를 담은 UserResponseDto
     */
    UserResponseDto getCurrentUserProfile(Authentication authentication);

    /**
     * 현재 인증된 사용자의 프로필 정보(닉네임, 자기소개, 프로필 이미지 등)를 수정합니다.
     *
     * @param authentication 현재 사용자의 인증 정보
     * @param requestDto     수정할 프로필 정보를 담은 DTO
     * @return 수정된 사용자 프로필 정보를 담은 UserResponseDto
     */
    UserResponseDto updateUserProfile(Authentication authentication, UserProfileUpdateRequestDto requestDto);

    // 여기에 필요한 다른 사용자 관련 서비스 메소드 시그니처를 추가할 수 있습니다.
    // 예를 들어, ID로 특정 사용자 정보 조회 (관리자용), 사용자 검색 등
}
