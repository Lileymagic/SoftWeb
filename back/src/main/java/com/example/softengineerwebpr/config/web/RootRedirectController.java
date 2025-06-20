package com.example.softengineerwebpr.config.web;


import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootRedirectController {

    //루트 경로 접근 시 올바른 경로로 리다이렉트
    @GetMapping("/")
    public String redirectRoot(Authentication authentication) {
        // 인증된 사용자는 프로젝트 리스트로, 아니면 로그인 페이지로
        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {
            return "redirect:/front/projectlist.html";
        } else {
            return "redirect:/front/index.html";
        }
    }

    //front 경로 접근 시 올바른 경로로 리다이렉트
    @GetMapping("/front")
    public String redirectFront(Authentication authentication) {
        return redirectRoot(authentication);
    }

    @GetMapping("/front/")
    public String redirectFrontSlash(Authentication authentication) {
        return redirectRoot(authentication);
    }
}