package com.example.softengineerwebpr.domain.user.repository;

import com.example.softengineerwebpr.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일로 사용자 조회 (소셜 로그인 시 기존 가입 여부 확인 등)
    Optional<User> findByEmail(String email);

    // 닉네임으로 사용자 조회 (프로필 조회, 친구 검색 등)
    Optional<User> findByNickname(String nickname);

    // 닉네임에 특정 문자열을 포함하고, 대소문자를 구분하지 않는 사용자 목록 검색
    List<User> findAllByNicknameContainingIgnoreCase(String nickname); // << 새로 추가된 메소드

    // 식별 코드로 사용자 조회 (친구 검색 등)
    Optional<User> findByIdentificationCode(String identificationCode);

    // 이메일 존재 여부 확인
    boolean existsByEmail(String email);

    // 닉네임 존재 여부 확인
    boolean existsByNickname(String nickname);
}
