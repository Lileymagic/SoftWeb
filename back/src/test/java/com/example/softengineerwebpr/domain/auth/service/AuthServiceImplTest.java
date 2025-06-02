package com.example.softengineerwebpr.domain.auth.service;

import com.example.softengineerwebpr.common.exception.BusinessLogicException;
import com.example.softengineerwebpr.common.exception.ErrorCode;
import com.example.softengineerwebpr.common.service.EmailService;
import com.example.softengineerwebpr.common.util.UserCodeGenerator;
import com.example.softengineerwebpr.domain.auth.dto.SignUpRequestDto;
import com.example.softengineerwebpr.domain.auth.entity.UserCredential;
import com.example.softengineerwebpr.domain.auth.repository.UserCredentialRepository;
import com.example.softengineerwebpr.domain.user.entity.User;
import com.example.softengineerwebpr.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService 테스트")
class AuthServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserCredentialRepository userCredentialRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @Mock
    private InMemoryVerificationCodeStore codeStore;

    @Mock
    private UserCodeGenerator userCodeGenerator;

    @InjectMocks
    private  AuthServiceImpl authService;

    private SignUpRequestDto signUpRequestDto;
    private User savedUser;

    @BeforeEach
    void setUp() {
        signUpRequestDto = new SignUpRequestDto();
        signUpRequestDto.setLoginId("testuser1");
        signUpRequestDto.setPassword("test123!");
        signUpRequestDto.setNickname("테스트유저");
        signUpRequestDto.setEmail("test@example.com");
        signUpRequestDto.setVerificationCode("1234");

        savedUser = User.builder()
                .nickname("테스트유저")
                .email("test@example.com")
                .identificationCode("1234")
                .isOnline(true)
                .build();

        try {
            var idxField = User.class.getDeclaredField("idx");
            idxField.setAccessible(true);
            idxField.set(savedUser, 1L);
        } catch (Exception e) {
        }
    }

    @Test
    @DisplayName("회원가입 성공")
    void signUp_Success() {
        when(codeStore.getCode(anyString())).thenReturn(new com.example.softengineerwebpr.domain.auth.dto.VerificationCodeEntry("1234", 3));
        when(userCredentialRepository.existsByLoginId(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userCodeGenerator.generateRandomFourDigitCode()).thenReturn("1234");
        when(userRepository.saveAndFlush(any(User.class))).thenReturn(savedUser);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        assertDoesNotThrow(() -> authService.signUp(signUpRequestDto));

        verify(userRepository).saveAndFlush(any(User.class));
        verify(userCredentialRepository).save(any(UserCredential.class));
        verify(codeStore).removeCode(signUpRequestDto.getEmail());

    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 인증 실패")
    void signUp_Fail_EmailVerificationFailed() {
        when(codeStore.getCode(anyString())).thenReturn(null);

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> authService.signUp(signUpRequestDto));
        assertEquals(ErrorCode.EMAIL_VERIFICATION_FAILED, exception.getErrorCode());
    }

    @Test
    @DisplayName("회원가입 실패 - 아이디 중복")
    void signUp_Fail_LoginIdDuplication() {
        when(codeStore.getCode(anyString())).thenReturn(new com.example.softengineerwebpr.domain.auth.dto.VerificationCodeEntry("1234", 3));
        when(userCredentialRepository.existsByLoginId(anyString())).thenReturn(true);

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> authService.signUp(signUpRequestDto));
        assertEquals(ErrorCode.LOGIN_ID_DUPLICATION, exception.getErrorCode());
    }

    @Test
    @DisplayName("아이디 중복 확인 - 사용 가능")
    void isLoginIdAvailable_Success() {
        when(userCredentialRepository.existsByLoginId("newuser")).thenReturn(false);

        boolean result = authService.isLoginIdAvailable("newuser");

        assertTrue(result);
    }

    @Test
    @DisplayName("아이디 중복 확인 - 사용 불가능")
    void isLoginIdAvailable_Fail() {
        when(userCredentialRepository.existsByLoginId("existinguser")).thenReturn(true);

        boolean result = authService.isLoginIdAvailable("existinguser");

        assertFalse(result);
    }

    @Test
    @DisplayName("이메일 인증번호 발송 성공")
    void sendVerificationEmail_Success() {
        String email = "test@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(userCodeGenerator.generateNumericVerificationCode(4)).thenReturn("5678");
        doNothing().when(emailService).sendSimpleMessage(anyString(), anyString(), anyString());

        assertDoesNotThrow(() -> authService.sendVerificationEmail(email));

        verify(codeStore).storeCode(eq(email), eq("5678"));
        verify(emailService).sendSimpleMessage(eq(email), anyString(), anyString());
    }

    @Test
    @DisplayName("이메일 인증번호 발송 실패 - 이미 가입된 이메일")
    void sendVerificationEmail_Fail_EmailDuplication() {
        String email = "existing@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> authService.sendVerificationEmail(email));
        assertEquals(ErrorCode.EMAIL_DUPLICATION, exception.getErrorCode());
    }

    @Test
    @DisplayName("아이디 찾기 성공")
    void findLoginIdAndSendEmail_Success() {
        String email = "test@example.com";
        User user = User.builder().email(email).build();
        UserCredential userCredential = UserCredential.builder()
                .user(user)
                .loginId("testuser1")
                .password("encodedPassword")
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userCredentialRepository.findByUser(user)).thenReturn(Optional.of(userCredential));
        doNothing().when(emailService).sendSimpleMessage(anyString(), anyString(), anyString());

        assertDoesNotThrow(() -> authService.findLoginIdAndSendEmail(email));

        verify(emailService).sendSimpleMessage(eq(email), anyString(), contains("testuser1"));
    }

    @Test
    @DisplayName("아이디 찾기 실패 - 사용자 없음")
    void findLoginIdAndSendEmail_Fail_UserNotFound() {
        String email = "notfound@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> authService.findLoginIdAndSendEmail(email));
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("비밀번호 재설정 성공")
    void resetPasswordAndSendEmail_Success() {
        String loginId = "testuser1";
        String email = "test@example.com";
        User user = User.builder().email(email).build();
        UserCredential userCredential = UserCredential.builder()
                .user(user)
                .loginId(loginId)
                .password("oldPassword")
                .build();

        when(userCredentialRepository.findByLoginId(loginId)).thenReturn(Optional.of(userCredential));
        when(userCodeGenerator.generateRandomTemporaryPassword(10)).thenReturn("tempPass123");
        when(passwordEncoder.encode("tempPass123")).thenReturn("encodedTempPass");
        doNothing().when(emailService).sendSimpleMessage(anyString(), anyString(), anyString());

        assertDoesNotThrow(() -> authService.resetPasswordAndSendEmail(loginId, email));

        verify(userCredentialRepository).save(userCredential);
        verify(emailService).sendSimpleMessage(eq(email), anyString(), contains("tempPass123"));
    }

    @Test
    @DisplayName("비밀번호 재설정 실패 - 사용자 정보 불일치")
    void resetPasswordAndSendEmail_Fail_UserInfoMismatch() {
        String loginId = "testuser1";
        String email = "wrong@example.com";
        User user = User.builder().email("correct@example.com").build();
        UserCredential userCredential = UserCredential.builder()
                .user(user)
                .loginId(loginId)
                .password("oldPassword")
                .build();

        when(userCredentialRepository.findByLoginId(loginId)).thenReturn(Optional.of(userCredential));

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> authService.resetPasswordAndSendEmail(loginId, email));
        assertEquals(ErrorCode.USER_INFO_MISMATCH, exception.getErrorCode());
    }
}
