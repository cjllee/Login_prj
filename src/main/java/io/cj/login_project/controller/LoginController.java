package io.cj.login_project.controller;

import io.cj.login_project.service.RefreshTokenService;
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

    @Autowired
    private RefreshTokenService refreshTokenService;

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/signup")
    public String signupForm() {
        return "signup";
    }

    @GetMapping("/welcome")
    @PreAuthorize("hasRole('ADMIN')")
    public String welcome(Model model, Authentication authentication) {
        String username;
        List<String> roles = new ArrayList<>();

        if (authentication.getPrincipal() instanceof DefaultOAuth2User) {
            DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
            username = oAuth2User.getAttribute("kakao_account") != null
                    ? ((java.util.Map<String, String>) oAuth2User.getAttribute("kakao_account")).get("email")
                    : oAuth2User.getName();
            for (GrantedAuthority authority : oAuth2User.getAuthorities()) {
                roles.add(authority.getAuthority());
            }
        } else if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
            for (GrantedAuthority authority : userDetails.getAuthorities()) {
                roles.add(authority.getAuthority());
            }
        } else {
            throw new IllegalStateException("Unknown principal type: " + authentication.getPrincipal().getClass());
        }

        // JWT 액세스 토큰 생성
        String token = jwtUtil.generateToken(username, roles);

        // 리프레시 토큰 생성 및 Redis에 저장
        String refreshToken = refreshTokenService.generateRefreshToken(username);

        // 모델에 토큰 추가
        model.addAttribute("token", token);
        model.addAttribute("refreshToken", refreshToken);
        return "welcome";
    }
}