package com.example.softengineerwebpr.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter // 컨트롤러에서 @RequestBody로 받기 위해, 또는 @Builder 사용 시 불필요
@NoArgsConstructor
public class SignUpRequestDto {

    @NotBlank(message = "로그인 아이디는 필수 입력 값입니다.")
    @Size(min = 4, max = 20, message = "로그인 아이디는 4자 이상 20자 이하로 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "로그인 아이디는 영어, 숫자, 대시(-), 언더스코어(_)만 사용 가능합니다.")
    private String loginId; // 요구사항: ID (영어, 숫자, 특수문자 일부 허용 (- , _ ), 최소 8자, 최대 20자)

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.") // 명세서엔 2~20자, 보안상 8자 권장
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,20}$",
            message = "비밀번호는 영문, 숫자, 특수문자를 최소 1개 이상 포함하여 8~20자로 입력해주세요.")
    private String password; // 요구사항: 비밀번호 (영어, 숫자, 특수문자 일부 허용 (!@#$%^&*), 최소 2자, 최대 20자)
    // 보안을 위해 최소 8자 및 복잡도(영문,숫자,특수문자 조합) 패턴 추가

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Size(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하로 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣_-]+$", message = "닉네임은 한글, 영어, 숫자, 대시(-), 언더스코어(_)만 사용 가능합니다.")
    private String nickname; // 요구사항: 닉네임 (한글, 영어, 숫자, 특수문자 일부 허용 ( - , _ ), 최소 4자, 최대 20자)

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.") // 이메일 필드는 명세서에 있지만, 회원가입 시 필수인지 선택인지는 확인 필요
    private String email; // 명세서: 이메일 인증

    @NotBlank(message = "이메일 인증번호를 입력해주세요.")
    @Size(min = 4, max = 4, message = "인증번호는 4자리여야 합니다.")
    private String verificationCode;

    // 필요시 추가 정보 (프로필 이미지, 자기소개 등 초기값)
    // private String profileImage;
    // private String bio;

    // Lombok @Builder를 사용할 경우
    // @Builder
    // public SignUpRequestDto(String loginId, String password, String nickname, String email) {
    //     this.loginId = loginId;
    //     this.password = password;
    //     this.nickname = nickname;
    //     this.email = email;
    // }
}