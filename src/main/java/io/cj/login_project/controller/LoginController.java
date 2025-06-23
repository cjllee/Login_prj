package io.cj.login_project.controller;

import io.cj.login_project.service.UserService;
import io.cj.login_project.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/signup")
    public String signupForm() {
        return "signup";
    }

    @GetMapping("/welcome")
    @PreAuthorize("hasRole('ADMIN')") // Controller-level role check
    public String welcome(Model model, Authentication authentication) {
        String username;
        List<String> roles = new ArrayList<>();

        // OAuth2 사용자 또는 폼 로그인 사용자 처리
        if (authentication.getPrincipal() instanceof DefaultOAuth2User) {
            DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
            username = oAuth2User.getAttribute("kakao_account") != null
                    ? ((java.util.Map<String, String>) oAuth2User.getAttribute("kakao_account")).get("email")
                    : oAuth2User.getName(); // 이메일 또는 기본 이름
            for (GrantedAuthority authority : oAuth2User.getAuthorities()) {
                roles.add(authority.getAuthority());
            }
        } else if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername(); // 이메일
            for (GrantedAuthority authority : userDetails.getAuthorities()) {
                roles.add(authority.getAuthority());
            }
        } else {
            throw new IllegalStateException("Unknown principal type: " + authentication.getPrincipal().getClass());
        }

        // JWT 토큰 생성
        String token = jwtUtil.generateToken(username, roles);

        // 모델에 토큰 추가
        model.addAttribute("token", token);
        return "welcome";
    }
}