package io.cj.login_project.service;

import io.cj.login_project.entity.EmailVerification;
import io.cj.login_project.repository.EmailVerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailVerificationRepository emailVerificationRepository;

    public void sendVerificationCode(String email) {
        String verificationCode = UUID.randomUUID().toString().substring(0, 6); // 6자리 랜덤 코드
        EmailVerification verification = EmailVerification.createVerification(email, verificationCode, 10); // 10분 유효
        emailVerificationRepository.save(verification);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("회원가입 인증 코드");
        message.setText("안녕하세요! 귀하의 인증 코드는 다음과 같습니다: " + verificationCode + "\n10분 내에 입력해 주세요.");
        mailSender.send(message);
    }

    public boolean verifyCode(String email, String code) {
        Optional<EmailVerification> optionalVerification = emailVerificationRepository.findTopByEmailOrderByCreatedAtDesc(email);
        if (optionalVerification.isPresent()) {
            EmailVerification verification = optionalVerification.get();
            if (verification.getVerificationCode().equals(code) && !verification.isExpired()) {
                verification.setVerified(true);
                emailVerificationRepository.save(verification);
                return true;
            }
        }
        return false;
    }
}