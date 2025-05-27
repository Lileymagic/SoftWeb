package com.example.softengineerwebpr.domain.user.dto; // 또는 domain.auth.dto

import com.example.softengineerwebpr.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserResponseDto {
    private Long idx;
    private String nickname;
    private String email;
    private String identificationCode;
    private String profileImage;
    private String bio;
    private LocalDateTime lastLogin;
    private boolean isOnline;
    private String loginId; // 아이디(일반 로그인 시 사용) 필드 추가

    @Builder
    public UserResponseDto(Long idx, String nickname, String email, String identificationCode,
                           String profileImage, String bio, LocalDateTime lastLogin, boolean isOnline,
                           String loginId) { // 생성자 파라미터에 loginId 추가
        this.idx = idx;
        this.nickname = nickname;
        this.email = email;
        this.identificationCode = identificationCode;
        this.profileImage = profileImage;
        this.bio = bio;
        this.lastLogin = lastLogin;
        this.isOnline = isOnline;
        this.loginId = loginId; // loginId 필드 초기화
    }

    /**
     * User 엔티티로부터 UserResponseDto를 생성합니다.
     * 이 메소드는 User 엔티티에 직접 loginId 정보가 없으므로 loginId를 null로 설정합니다.
     * loginId를 포함시키려면 아래의 fromEntity(User user, String loginId) 메소드나
     * 빌더를 직접 사용하는 서비스 로직이 필요합니다.
     * @param user User 엔티티
     * @return UserResponseDto 객체
     */
    public static UserResponseDto fromEntity(User user) {
        if (user == null) return null;
        return UserResponseDto.builder()
                .idx(user.getIdx())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .identificationCode(user.getIdentificationCode())
                .profileImage(user.getProfileImage())
                .bio(user.getBio())
                .lastLogin(user.getLastLogin())
                .isOnline(user.isOnline())
                .loginId(null) // User 엔티티에는 loginId 정보가 없으므로 기본값 null
                .build();
    }

    /**
     * User 엔티티와 loginId 정보로부터 UserResponseDto를 생성합니다.
     * 서비스 계층에서 User 정보와 UserCredential 정보를 조합하여 이 메소드를 사용할 수 있습니다.
     * @param user User 엔티티
     * @param loginId 사용자의 로그인 ID (일반 로그인 시), 소셜 로그인의 경우 null일 수 있음
     * @return UserResponseDto 객체
     */
    public static UserResponseDto fromEntity(User user, String loginId) {
        if (user == null) return null;
        return UserResponseDto.builder()
                .idx(user.getIdx())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .identificationCode(user.getIdentificationCode())
                .profileImage(user.getProfileImage())
                .bio(user.getBio())
                .lastLogin(user.getLastLogin())
                .isOnline(user.isOnline())
                .loginId(loginId) // 외부에서 전달받은 loginId 사용
                .build();
    }
}