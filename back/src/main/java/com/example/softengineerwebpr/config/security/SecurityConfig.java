package com.example.softengineerwebpr.config.security;

import com.example.softengineerwebpr.domain.auth.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer; // CSRF disable 시 필요, 현재는 불필요
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository; // CSRF 쿠키 방식 사용 시 필요

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 보호 설정 (기본 활성화 유지 권장)
                // CSRF(Cross-Site Request Forgery) 보호 비활성화
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                new AntPathRequestMatcher("/front/"),
                                new AntPathRequestMatcher("/front/index.html"),
                                new AntPathRequestMatcher("/front/login.html"),
                                new AntPathRequestMatcher("/front/join.html"),
                                new AntPathRequestMatcher("/front/findmyid.html"),
                                new AntPathRequestMatcher("/front/findmypw.html"),
                                new AntPathRequestMatcher("/css/**"),
                                new AntPathRequestMatcher("/assets/**"),
                                new AntPathRequestMatcher("/js/**"),
                                new AntPathRequestMatcher("/icon/**"),
                                new AntPathRequestMatcher("/*.png"),
                                new AntPathRequestMatcher("/*.ico"),
                                new AntPathRequestMatcher("/error"),
                                new AntPathRequestMatcher("/api/auth/**") // 인증 관련 API는 대부분 permitAll (회원가입, 로그인 등)
                        ).permitAll()
                        // OAuth2 관련 경로도 permitAll (로그인 시도 경로)
                        .requestMatchers(new AntPathRequestMatcher("/front/login"), new AntPathRequestMatcher("/oauth2/**")).permitAll()

                        // --- 업무(Task) 관련 API 경로 추가 ---
                        // 프로젝트 내 업무 생성 및 조회
                        .requestMatchers(new AntPathRequestMatcher("/api/projects/*/tasks/**")).authenticated()
                        // 개별 업무 조회, 수정, 삭제, 멤버 관리 등
                        .requestMatchers(new AntPathRequestMatcher("/api/tasks/**")).authenticated()
                        // ------------------------------------

                        // 기존 프로젝트 API 경로 (이미 authenticated()로 설정되어 있음)
                        .requestMatchers(new AntPathRequestMatcher("/api/projects/**")).authenticated()
                        // 사용자 프로필 API 경로 (예: /api/users/me)
                        .requestMatchers(new AntPathRequestMatcher("/api/users/me")).authenticated()
                        // 기타 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                )

                .formLogin(formLogin -> formLogin
                        .loginPage("/front/login.html") // ① 사용자가 인증되지 않았을 때 보여줄 로그인 폼 페이지 경로 (정적 파일 경로)
                        .loginProcessingUrl("/api/auth/perform_login") // ② 로그인 폼 데이터를 POST로 보낼 URL (Spring Security가 처리)
                        .usernameParameter("loginId") // ③ 프론트엔드 폼의 아이디 필드 name 속성
                        .passwordParameter("password")// ④ 프론트엔드 폼의 비밀번호 필드 name 속성
                        .defaultSuccessUrl("/front/projectlist.html", true)     // ⑤ 로그인 성공 시 리다이렉트될 기본 URL
                        .failureUrl("/front/login.html?error=true") // ⑥ 로그인 실패 시 리다이렉트될 URL
                        .permitAll()                 // ⑦ 로그인 페이지 자체 및 처리 URL은 누구나 접근 가능해야 함
                )

                .oauth2Login(oauth2 -> oauth2
                                .loginPage("/front/login.html")
                                .defaultSuccessUrl("/front/projectlist.html", true) // 성공 핸들러를 사용하면 이 설정은 무시될 수 있음
                                .userInfoEndpoint(userInfo -> userInfo
                                        .userService(customOAuth2UserService)
                                )
                        // (선택적) 로그인 성공/실패 시 추가 로직을 위한 핸들러 등록
                        // .successHandler(yourAuthenticationSuccessHandler()) // 직접 구현한 성공 핸들러
                        // .failureHandler(yourAuthenticationFailureHandler()) // 직접 구현한 실패 핸들러
                )

                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/api/auth/logout"))
                        .logoutSuccessUrl("/front/login.html")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID") // 세션 쿠키 이름 (기본값)
                        .permitAll()
                )

                .sessionManagement(session -> session
                                .maximumSessions(1) // 동시 세션 제어: 한 사용자당 하나의 세션만 허용
                                .expiredUrl("/front/login.html?expired=true")
                        // .maxSessionsPreventsLogin(true) // 선택: true면 새 로그인 차단, false(기본)면 이전 세션 만료
                );

        // Remember Me 설정 (선택적)
        // .rememberMe(rememberMe -> rememberMe
        //         .key("yourRememberMeKey") // 암호화 키 (필수, 강력한 문자열 사용)
        //         .tokenValiditySeconds(86400 * 14) // 예: 14일 (초 단위)
        //         // .userDetailsService(yourUserDetailsService) // 사용자 정보 조회 서비스 필요 (Spring Security UserDetailsService 구현체)
        //         // .rememberMeParameter("remember-me") // 로그인 폼의 '로그인 유지' 체크박스 name
        // );

        return http.build();
    }

    // (선택적) OAuth2 로그인 성공/실패 핸들러 Bean 등록
    // @Bean
    // public AuthenticationSuccessHandler yourAuthenticationSuccessHandler() {
    //     return new YourCustomAuthenticationSuccessHandler(); // 직접 구현
    // }

    // @Bean
    // public AuthenticationFailureHandler yourAuthenticationFailureHandler() {
    //     return new YourCustomAuthenticationFailureHandler(); // 직접 구현
    // }
}