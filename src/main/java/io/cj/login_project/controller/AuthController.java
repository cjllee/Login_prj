package io.cj.login_project.controller;

import io.cj.login_project.entity.User;
import io.cj.login_project.service.EmailService;
import io.cj.login_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class AuthController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @PostMapping("/send-verification-code")
    public ResponseEntity<String> sendVerificationCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (userService.existsByEmail(email)) {
            return ResponseEntity.badRequest().body("이미 등록된 이메일입니다.");
        }
        emailService.sendVerificationCode(email);
        return ResponseEntity.ok("인증 코드가 " + email + "로 발송되었습니다.");
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestParam String name,
                                         @RequestParam String email,
                                         @RequestParam String password,
                                         @RequestParam String verificationCode) {
        if (!emailService.verifyCode(email, verificationCode)) {
            return ResponseEntity.badRequest().body("잘못된 인증 코드입니다.");
        }
        User user = User.createUser(email, password, name);
        userService.saveUser(user);
        return ResponseEntity.ok("회원가입이 완료되었습니다. 로그인 화면으로 이동합니다.");
    }
}