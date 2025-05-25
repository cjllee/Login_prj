package io.cj.login_projec.repository;

import io.cj.login_projec.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일로 사용자 찾기
    Optional<User> findByEmail(String email);

    // 이메일 존재 여부 확인 (ID 중복 확인용)
    boolean existsByEmail(String email);

    // OAuth2 제공자 ID로 사용자 찾기
    Optional<User> findByProviderAndProviderId(String provider, String providerId);
}
