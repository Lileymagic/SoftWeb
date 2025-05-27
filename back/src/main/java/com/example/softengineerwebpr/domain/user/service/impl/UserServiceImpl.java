package com.example.softengineerwebpr.domain.user.service.impl;

import com.example.softengineerwebpr.common.exception.BusinessLogicException;
import com.example.softengineerwebpr.common.exception.ErrorCode;
import com.example.softengineerwebpr.domain.auth.entity.UserCredential;
import com.example.softengineerwebpr.domain.auth.repository.UserCredentialRepository;
import com.example.softengineerwebpr.domain.user.dto.UserProfileUpdateRequestDto;
import com.example.softengineerwebpr.domain.user.dto.UserResponseDto;
import com.example.softengineerwebpr.domain.user.entity.User;
import com.example.softengineerwebpr.domain.user.repository.UserRepository;
import com.example.softengineerwebpr.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils; // StringUtils 추가

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserCredentialRepository userCredentialRepository;

    /**
     * Authentication 객체로부터 User 엔티티와 loginId (해당되는 경우)를 추출합니다.
     * 내부적으로 사용되는 헬퍼 DTO입니다.
     */
    private static class AuthenticatedUserInfo {
        User user;
        String loginId; // 일반 로그인 사용자의 경우 loginId, 소셜 로그인의 경우 null

        AuthenticatedUserInfo(User user, String loginId) {
            this.user = user;
            this.loginId = loginId;
        }
    }

    private AuthenticatedUserInfo getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new BusinessLogicException(ErrorCode.USER_NOT_FOUND, "인증된 사용자 정보를 찾을 수 없습니다.");
        }

        Object principal = authentication.getPrincipal();
        User user = null;
        String loginId = null;

        if (principal instanceof OAuth2User oauth2User) {
            // CustomOAuth2UserService에서 "id"라는 이름으로 User의 idx를 attribute에 저장했음
            Object userIdObj = oauth2User.getAttribute("id");
            if (userIdObj instanceof Number) {
                Long userId = ((Number) userIdObj).longValue();
                user = userRepository.findById(userId)
                        .orElseThrow(() -> new BusinessLogicException(ErrorCode.USER_NOT_FOUND, "OAuth2 사용자를 찾을 수 없습니다. ID: " + userId));
            } else {
                log.warn("OAuth2User principal에서 사용자 idx를 나타내는 'id' 속성을 찾을 수 없거나 숫자 타입이 아닙니다: {}", userIdObj);
                throw new BusinessLogicException(ErrorCode.USER_NOT_FOUND, "OAuth2 사용자 정보를 처리할 수 없습니다.");
            }
            // 소셜 로그인이므로 loginId는 null
        } else if (principal instanceof UserDetails springUserDetails) {
            String username = springUserDetails.getUsername(); // CustomUserDetailsService에서 loginId를 username으로 사용
            UserCredential userCredential = userCredentialRepository.findByLoginId(username)
                    .orElseThrow(() -> new BusinessLogicException(ErrorCode.USER_NOT_FOUND, "사용자 자격 증명을 찾을 수 없습니다. Login ID: " + username));
            user = userCredential.getUser();
            loginId = userCredential.getLoginId();
            if (user == null) { // 데이터 정합성 오류 가능성
                throw new BusinessLogicException(ErrorCode.USER_NOT_FOUND, "자격 증명에 연결된 사용자를 찾을 수 없습니다.");
            }
        } else if (principal instanceof String) { // 예: JWT 토큰에서 직접 사용자 ID (Long)를 Principal로 설정한 경우 (현재 구조는 아님)
            try {
                Long userId = Long.parseLong((String) principal);
                user = userRepository.findById(userId)
                        .orElseThrow(() -> new BusinessLogicException(ErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다. ID: " + userId));
                // 이 경우 loginId는 UserCredential에서 찾아야 함 (필요시)
                Optional<UserCredential> credOpt = userCredentialRepository.findByUser(user);
                if (credOpt.isPresent()) {
                    loginId = credOpt.get().getLoginId();
                }

            } catch (NumberFormatException e) {
                log.warn("문자열 Principal을 Long 타입 사용자 ID로 변환 실패: {}", principal);
                throw new BusinessLogicException(ErrorCode.USER_NOT_FOUND, "잘못된 사용자 인증 정보입니다.");
            }
        }
        else {
            log.warn("지원되지 않는 Principal 타입입니다: {}", principal.getClass().getName());
            throw new BusinessLogicException(ErrorCode.USER_NOT_FOUND, "인증 정보 형식이 올바르지 않습니다.");
        }
        return new AuthenticatedUserInfo(user, loginId);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getCurrentUserProfile(Authentication authentication) {
        AuthenticatedUserInfo authInfo = getAuthenticatedUser(authentication);
        return UserResponseDto.fromEntity(authInfo.user, authInfo.loginId);
    }

    @Override
    public UserResponseDto updateUserProfile(Authentication authentication, UserProfileUpdateRequestDto requestDto) {
        AuthenticatedUserInfo authInfo = getAuthenticatedUser(authentication);
        User currentUser = authInfo.user;

        // 닉네임 변경 처리: requestDto에 닉네임이 있고, 현재 닉네임과 다를 경우
        if (StringUtils.hasText(requestDto.getNickname()) && !currentUser.getNickname().equals(requestDto.getNickname())) {
            // 여기서 닉네임+식별코드 중복 검사는 User 엔티티의 uk_user_nickname_identification_code 제약조건에 의해 DB 레벨에서 처리될 수 있음
            // AuthServiceImpl의 signUp 로직처럼 애플리케이션 레벨에서 미리 검사하려면 userRepository.existsByNicknameAndIdentificationCode(...) 등이 필요.
            // 단, 현재 User 엔티티에는 identificationCode를 변경하는 로직이 없으므로, 닉네임만 변경 시 기존 identificationCode와 조합하여 유니크해야 함.
            // User 엔티티의 uk_user_nickname_identification_code 제약 조건이 있으므로,
            // 동일한 identification_code를 가진 다른 사용자가 새 닉네임을 이미 사용 중인지 확인하는 로직이 필요할 수 있습니다.
            // 여기서는 User 엔티티의 updateProfile 또는 updateNickname 메소드가 단순 필드 변경만 한다고 가정하고 진행합니다.
            // 필요시: userRepository.findByNicknameAndIdentificationCode(requestDto.getNickname(), currentUser.getIdentificationCode())
            //          .ifPresent(existingUser -> { if (!existingUser.getIdx().equals(currentUser.getIdx())) throw new BusinessLogicException(ErrorCode.NICKNAME_DUPLICATION); });
            log.info("사용자 {}의 닉네임 변경 시도: {} -> {}", currentUser.getNickname(), requestDto.getNickname());
        }

        // User 엔티티의 업데이트 메소드 활용
        // User 엔티티에 프로필 이미지와 자기소개만 업데이트하는 메소드가 있다면 그것을 사용하거나,
        // 개별 setter를 사용하거나, 모든 것을 업데이트하는 메소드를 사용할 수 있습니다.
        // User.java에는 updateProfile(nickname, profileImage, bio)가 있습니다.
        currentUser.updateProfile(
                StringUtils.hasText(requestDto.getNickname()) ? requestDto.getNickname() : currentUser.getNickname(),
                StringUtils.hasText(requestDto.getProfileImage()) ? requestDto.getProfileImage() : currentUser.getProfileImage(),
                StringUtils.hasText(requestDto.getBio()) ? requestDto.getBio() : currentUser.getBio()
        );
        // 만약 닉네임 필드만 따로 있고, 나머지 bio, profileImage만 업데이트하는 메서드가 User 엔티티에 있다면 그게 더 적절할 수 있음.
        // (예: currentUser.updateBioAndImage(requestDto.getBio(), requestDto.getProfileImage());)

        User updatedUser = userRepository.save(currentUser);
        log.info("사용자 프로필 업데이트 완료: userId={}", updatedUser.getIdx());

        return UserResponseDto.fromEntity(updatedUser, authInfo.loginId);
    }
}