package io.cj.login_project.controller;

import io.cj.login_project.entity.User;
import io.cj.login_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model) {
        Optional<User> optionalUser = userService.findByEmail(email);
        if (optionalUser.isPresent() && optionalUser.get().getPassword().equals(password)) { // 실제로는 암호화 비교 필요
            model.addAttribute("user", optionalUser.get());
            return "welcome";
        }
        model.addAttribute("error", "이메일 또는 비밀번호가 잘못되었습니다.");
        return "login";
    }

    @GetMapping("/signup")
    public String signupForm() {
        return "signup";
    }
}