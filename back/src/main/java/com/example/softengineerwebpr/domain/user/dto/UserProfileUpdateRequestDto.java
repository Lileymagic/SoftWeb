package com.example.softengineerwebpr.domain.user.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserProfileUpdateRequestDto {

    @Size(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하로 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣_-]+$", message = "닉네임은 한글, 영어, 숫자, 대시(-), 언더스코어(_)만 사용 가능합니다.")
    private String nickname; // 사용자 명세서 및 profile.html 참고: 2-20자

    @Size(max = 65535, message = "자기소개는 65535자를 넘을 수 없습니다.") // User 엔티티의 bio 필드가 TEXT 타입인 것을 고려
    private String bio;

    @Size(max = 255, message = "프로필 이미지 경로는 255자를 넘을 수 없습니다.") // User 엔티티의 profileImage 필드가 VARCHAR(255)인 것을 고려
    // 필요시 @URL(message = "올바른 URL 형식이 아닙니다.") 어노테이션 추가 가능
    private String profileImage; // 프로필 이미지 URL

    // 모든 필드를 받는 생성자 (필요에 따라 Lombok @AllArgsConstructor 사용 가능)
    public UserProfileUpdateRequestDto(String nickname, String bio, String profileImage) {
        this.nickname = nickname;
        this.bio = bio;
        this.profileImage = profileImage;
    }
}