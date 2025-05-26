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
                                // 정적 리소스 경로는 빌드 결과에 따라 그대로 유지하거나 확인 필요
                                // (예: /css/**, /assets/**, /icon/** 등은 보통 루트 기준이므로 변경 불필요할 수 있음)
                                new AntPathRequestMatcher("/css/**"),
                                new AntPathRequestMatcher("/assets/**"),
                                new AntPathRequestMatcher("/js/**"), // 만약 사용한다면
                                new AntPathRequestMatcher("/icon/**"),
                                new AntPathRequestMatcher("/*.png"), // 루트 경로의 png (예: static/logo.png)
                                new AntPathRequestMatcher("/*.ico"),
                                new AntPathRequestMatcher("/error"),
                                // API 경로는 변경 없음
                                new AntPathRequestMatcher("/api/auth/**")
                        ).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/auth/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/front/login"), new AntPathRequestMatcher("/oauth2/**")).permitAll()
                        // API 문서화 도구 경로 (예: Swagger UI 또는 Spring REST Docs)가 있다면 permitAll 추가
                        // .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**"), new AntPathRequestMatcher("/v3/api-docs/**")).permitAll()
                        .anyRequest().authenticated()
                )

                .formLogin(formLogin -> formLogin
                        .loginPage("/front/login.html") // ① 사용자가 인증되지 않았을 때 보여줄 로그인 폼 페이지 경로 (정적 파일 경로)
                        .loginProcessingUrl("/api/auth/perform_login") // ② 로그인 폼 데이터를 POST로 보낼 URL (Spring Security가 처리)
                        .usernameParameter("loginId") // ③ 프론트엔드 폼의 아이디 필드 name 속성
                        .passwordParameter("password")// ④ 프론트엔드 폼의 비밀번호 필드 name 속성
                        .defaultSuccessUrl("/front/projectlist.html")     // ⑤ 로그인 성공 시 리다이렉트될 기본 URL
                        .failureUrl("/front/login.html?error=true") // ⑥ 로그인 실패 시 리다이렉트될 URL
                        .permitAll()                 // ⑦ 로그인 페이지 자체 및 처리 URL은 누구나 접근 가능해야 함
                )

                .oauth2Login(oauth2 -> oauth2
                                .loginPage("/front/login.html")
                                .defaultSuccessUrl("/front/index.html") // 성공 핸들러를 사용하면 이 설정은 무시될 수 있음
                                .userInfoEndpoint(userInfo -> userInfo
                                        .userService(customOAuth2UserService)
                                )
                        // (선택적) 로그인 성공/실패 시 추가 로직을 위한 핸들러 등록
                        // .successHandler(yourAuthenticationSuccessHandler()) // 직접 구현한 성공 핸들러
                        // .failureHandler(yourAuthenticationFailureHandler()) // 직접 구현한 실패 핸들러
                )

                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/api/auth/logout"))
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID") // 세션 쿠키 이름 (기본값)
                        .permitAll()
                )

                .sessionManagement(session -> session
                                .maximumSessions(1) // 동시 세션 제어: 한 사용자당 하나의 세션만 허용
                                .expiredUrl("/login?expired=true") // 세션 만료 또는 중복 로그인으로 기존 세션 만료 시 이동할 URL
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