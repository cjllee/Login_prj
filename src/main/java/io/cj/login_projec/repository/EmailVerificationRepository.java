package io.cj.login_projec.repository;

import io.cj.login_projec.entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {

    // 이메일로 가장 최근 인증 정보 찾기
    Optional<EmailVerification> findTopByEmailOrderByCreatedAtDesc(String email);

    // 이메일과 인증 코드로 인증 정보 찾기
    Optional<EmailVerification> findByEmailAndVerificationCode(String email, String verificationCode);
}

