package com.example.softengineerwebpr.domain.user.controller;

import com.example.softengineerwebpr.common.dto.ApiResponse;
import com.example.softengineerwebpr.domain.user.dto.UserResponseDto;
import com.example.softengineerwebpr.domain.user.dto
        .UserProfileUpdateRequestDto; // 프로필 수정을 위한 DTO (가정)
import com.example.softengineerwebpr.domain.user.entity.User;
import com.example.softengineerwebpr.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

// 현재 UserService 및 UserProfileUpdateRequestDto는 아직 생성되지 않았다고 가정하고 작성합니다.
// UserService에서 UserResponseDto를 올바르게 생성하여 반환해야 합니다.

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService; // UserService 주입

    /**
     * 현재 로그인된 사용자의 프로필 정보를 조회합니다.
     * (닉네임, 아이디/이메일, 식별코드, 자기소개, 프로필 이미지 등)
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponseDto>> getMyUserProfile(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            // 이 부분은 Spring Security의 접근 제어로도 처리될 수 있지만, 명시적으로 한 번 더 확인합니다.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.success(HttpStatus.UNAUTHORIZED.value(),"인증이 필요합니다.", null));
        }

        // UserService를 통해 현재 사용자의 UserResponseDto를 가져옵니다.
        // 이 DTO에는 loginId가 올바르게 채워져 있어야 합니다 (일반 사용자는 loginId, 소셜 사용자는 null).
        UserResponseDto userProfile = userService.getCurrentUserProfile(authentication);

        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "사용자 프로필 정보 조회 성공", userProfile));
    }

    /**
     * 현재 로그인된 사용자의 프로필 정보(닉네임, 자기소개, 프로필 이미지)를 수정합니다.
     * profile.html 페이지와 연관됩니다.
     * @param requestDto 프로필 수정 정보
     * @param authentication 현재 인증 정보
     * @return 수정된 사용자 프로필 정보
     */
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateUserProfile(
            @RequestBody UserProfileUpdateRequestDto requestDto, // 예시 DTO, 필요에 따라 정의해야 함
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.success(HttpStatus.UNAUTHORIZED.value(),"인증이 필요합니다.", null));
        }

        UserResponseDto updatedUserProfile = userService.updateUserProfile(authentication, requestDto);

        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "프로필 정보가 성공적으로 수정되었습니다.", updatedUserProfile));
    }

    // 여기에 추가적인 사용자 관련 엔드포인트들을 구현할 수 있습니다.
    // 예를 들어, 다른 사용자 프로필 조회 (공개 프로필의 경우), 사용자 검색 등
}