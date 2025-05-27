package com.example.softengineerwebpr.common.util;

import com.example.softengineerwebpr.common.exception.BusinessLogicException;
import com.example.softengineerwebpr.common.exception.ErrorCode;
import com.example.softengineerwebpr.domain.auth.entity.UserCredential;
import com.example.softengineerwebpr.domain.auth.repository.UserCredentialRepository;
import com.example.softengineerwebpr.domain.user.entity.User;
import com.example.softengineerwebpr.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationUtil {

    private final UserRepository userRepository;
    private final UserCredentialRepository userCredentialRepository;

    /**
     * Spring Security의 Authentication 객체로부터 현재 시스템의 User 엔티티를 반환합니다.
     *
     * @param authentication 현재 사용자의 인증 정보
     * @return 인증된 User 엔티티
     * @throws BusinessLogicException 인증 정보가 없거나, 사용자를 찾을 수 없는 경우
     */
    public User getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            // API 경로 권한 설정에서 이미 처리되겠지만, 서비스/컨트롤러 레벨에서도 방어적으로 확인
            log.warn("Attempted to get current user but authentication is null, not authenticated, or anonymous.");
            throw new BusinessLogicException(ErrorCode.USER_NOT_FOUND, "인증된 사용자 정보를 찾을 수 없습니다 (인증 객체 없음).");
        }

        Object principal = authentication.getPrincipal();
        User user;

        if (principal instanceof OAuth2User oauth2User) {
            // CustomOAuth2UserService에서 "id"라는 이름으로 User의 idx를 attribute에 저장했음을 가정
            Object userIdObj = oauth2User.getAttribute("id");
            if (userIdObj instanceof Number) {
                Long userId = ((Number) userIdObj).longValue();
                user = userRepository.findById(userId)
                        .orElseThrow(() -> {
                            log.warn("OAuth2 authenticated user not found in DB with idx: {}", userId);
                            return new BusinessLogicException(ErrorCode.USER_NOT_FOUND, "OAuth2 사용자를 DB에서 찾을 수 없습니다. ID: " + userId);
                        });
            } else {
                log.warn("OAuth2User principal에서 사용자 idx를 나타내는 'id' 속성을 찾을 수 없거나 숫자 타입이 아닙니다: {}", userIdObj);
                throw new BusinessLogicException(ErrorCode.USER_NOT_FOUND, "OAuth2 사용자 정보 형식이 올바르지 않습니다.");
            }
        } else if (principal instanceof UserDetails springUserDetails) {
            String loginId = springUserDetails.getUsername(); // CustomUserDetailsService에서 loginId를 username으로 사용
            UserCredential userCredential = userCredentialRepository.findByLoginId(loginId)
                    .orElseThrow(() -> {
                        log.warn("UserCredential not found for loginId: {}", loginId);
                        return new BusinessLogicException(ErrorCode.USER_NOT_FOUND, "사용자 자격 증명을 찾을 수 없습니다. Login ID: " + loginId);
                    });
            user = userCredential.getUser();
            if (user == null) { // 데이터 정합성 오류 가능성
                log.error("UserCredential with loginId {} has a null User reference.", loginId);
                throw new BusinessLogicException(ErrorCode.USER_NOT_FOUND, "자격 증명에 연결된 사용자 정보를 찾을 수 없습니다.");
            }
        } else if (principal instanceof String && !"anonymousUser".equals(principal)) {
            // Principal이 문자열인 경우, 예를 들어 JWT 토큰에서 직접 사용자 ID (Long 타입 문자열)를 Principal로 설정한 경우.
            // 현재 프로젝트의 SecurityConfig는 기본 폼 로그인과 OAuth2를 사용하므로 이 케이스는 일반적이지 않음.
            // 만약 이 케이스를 지원해야 한다면, 아래 로직 활성화 및 테스트 필요.
            try {
                Long userId = Long.parseLong((String) principal);
                user = userRepository.findById(userId)
                        .orElseThrow(() -> {
                            log.warn("User not found in DB with string principal (parsed as Long userId): {}", userId);
                            return new BusinessLogicException(ErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다. ID: " + userId);
                        });
            } catch (NumberFormatException e) {
                log.warn("String Principal을 Long 타입 사용자 ID로 변환 실패: {}. Principal: {}", e.getMessage(), principal);
                throw new BusinessLogicException(ErrorCode.USER_NOT_FOUND, "잘못된 형식의 사용자 인증 정보입니다.");
            }
        }
        else {
            log.warn("지원되지 않는 Principal 타입입니다: {}", principal.getClass().getName());
            throw new BusinessLogicException(ErrorCode.USER_NOT_FOUND, "인증 정보의 주체(Principal) 형식이 올바르지 않습니다.");
        }
        return user;
    }

    /**
     * Spring Security의 Authentication 객체로부터 현재 시스템의 User 엔티티와
     * 일반 로그인 사용자의 경우 loginId를 함께 반환하는 내부용 클래스 또는 레코드를 사용할 수 있습니다.
     * 이 정보는 UserResponseDto를 채울 때 ServiceImpl에서 사용됩니다.
     * 여기서는 getCurrentUser가 User 엔티티만 반환하도록 하고,
     * loginId가 필요한 경우 ServiceImpl에서 UserCredentialRepository를 통해 조회하도록 합니다.
     * (예시: UserServiceImpl의 getAuthenticatedUser 헬퍼 클래스 참고)
     */
}