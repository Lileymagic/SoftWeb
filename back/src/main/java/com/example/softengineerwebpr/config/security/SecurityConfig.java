package com.example.softengineerwebpr.config.security;

import com.example.softengineerwebpr.domain.auth.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
                                new AntPathRequestMatcher("/api/auth/**")
                        ).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/front/login"), new AntPathRequestMatcher("/oauth2/**")).permitAll()

                        // --- 게시글(Post) 및 댓글(Comment) 관련 API 경로 ---
                        .requestMatchers(new AntPathRequestMatcher("/api/tasks/*/posts", "GET")).authenticated()  // 특정 업무의 게시글 목록 조회
                        .requestMatchers(new AntPathRequestMatcher("/api/posts/*", "GET")).authenticated()       // 특정 게시글 상세 조회 (수정된 부분)
                        .requestMatchers(new AntPathRequestMatcher("/api/tasks/*/posts", "POST")).authenticated() // 특정 업무에 게시글 작성
                        .requestMatchers(new AntPathRequestMatcher("/api/posts/*", "DELETE")).authenticated() // 특정 게시글 삭제
                        .requestMatchers(new AntPathRequestMatcher("/api/posts/*", "PUT")).authenticated() // 특정 게시글 수정

                        .requestMatchers(new AntPathRequestMatcher("/api/posts/*/comments", "GET")).authenticated()
                        .requestMatchers(new AntPathRequestMatcher("/api/posts/*/comments", "POST")).authenticated()
                        .requestMatchers(new AntPathRequestMatcher("/api/comments/*", "PUT")).authenticated()
                        .requestMatchers(new AntPathRequestMatcher("/api/comments/*", "DELETE")).authenticated()
                        // ----------------------------------------------------

                        // --- 그룹(Group) 관련 API 경로 추가 ---
                        .requestMatchers(new AntPathRequestMatcher("/api/projects/*/groups", "GET")).authenticated()  // 특정 프로젝트의 그룹 목록 조회
                        .requestMatchers(new AntPathRequestMatcher("/api/projects/*/groups", "POST")).authenticated() // 특정 프로젝트에 그룹 생성
                        .requestMatchers(new AntPathRequestMatcher("/api/groups/*", "GET")).authenticated()        // 특정 그룹 상세 조회
                        .requestMatchers(new AntPathRequestMatcher("/api/groups/*", "PUT")).authenticated()        // 특정 그룹 정보 수정
                        .requestMatchers(new AntPathRequestMatcher("/api/groups/*", "DELETE")).authenticated()     // 특정 그룹 삭제
                        .requestMatchers(new AntPathRequestMatcher("/api/groups/*/members", "GET")).authenticated()  // 그룹 멤버 목록 조회
                        //.requestMatchers(new AntPathRequestMatcher("/api/groups/*/members", "POST")).authenticated() // 그룹에 멤버 추가
                        //.requestMatchers(new AntPathRequestMatcher("/api/groups/*/members", "DELETE")).authenticated()// 그룹에서 멤버 제외
                        .requestMatchers(new AntPathRequestMatcher("/api/groups/*/members", "PUT")).authenticated() // 그룹 멤버 동기화
                        .requestMatchers(new AntPathRequestMatcher("/api/groups/*/permissions", "PUT")).authenticated() // 그룹 권한 수정
                        // -----------------------------------------------------

                        .requestMatchers(new AntPathRequestMatcher("/api/projects/*/tasks/**")).authenticated()
                        .requestMatchers(new AntPathRequestMatcher("/api/tasks/**")).authenticated()

                        .requestMatchers(new AntPathRequestMatcher("/api/projects/**")).authenticated()
                        .requestMatchers(new AntPathRequestMatcher("/api/users/me")).authenticated()
                        .anyRequest().authenticated()
                )

                .formLogin(formLogin -> formLogin
                        .loginPage("/front/login.html")
                        .loginProcessingUrl("/api/auth/perform_login")
                        .usernameParameter("loginId")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/front/projectlist.html")
                        .failureUrl("/front/login.html?error=true")
                        .permitAll()
                )

                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/front/login.html")
                        .defaultSuccessUrl("/front/projectlist.html")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                )

                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/api/auth/logout"))
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )

                .sessionManagement(session -> session
                        .maximumSessions(1)
                        .expiredUrl("/front/login.html?expired=true")
                );

        return http.build();
    }
}