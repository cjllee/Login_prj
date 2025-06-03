package io.cj.login_project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private boolean emailVerified;

    @Column(nullable = false)
    private boolean enabled;

    // OAuth2 관련 필드
    private String provider;          // 제공자 (kakao, google 등)
    private String providerId;        // 제공자에서의 ID

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // 사용자 권한 열거형
    public enum Role {
        ROLE_USER, ROLE_ADMIN
    }

    // 기본 사용자 생성 메서드
    public static User createUser(String email, String password, String name) {
        return User.builder()
                .email(email)
                .password(password)
                .name(name)
                .role(Role.ROLE_USER)
                .emailVerified(false)
                .enabled(true)
                .build();
    }

    // OAuth2 사용자 생성 메서드
    public static User createOAuth2User(String email, String name, String provider, String providerId) {
        return User.builder()
                .email(email)
                .password("")  // OAuth2 사용자는 비밀번호 없음
                .name(name)
                .role(Role.ROLE_USER)
                .emailVerified(true)  // OAuth2 사용자는 이메일 인증 필요 없음
                .enabled(true)
                .provider(provider)
                .providerId(providerId)
                .build();
    }
}