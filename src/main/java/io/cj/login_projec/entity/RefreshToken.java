package io.cj.login_projec.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("refreshToken")
public class RefreshToken {

    @Id
    private String id;  // 사용자 이메일

    private String token;  // 리프레시 토큰 값

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Long expiration;  // 만료 시간 (밀리초)

    // 리프레시 토큰 생성 메서드
    public static RefreshToken createToken(String email, String token, Long expiration) {
        return RefreshToken.builder()
                .id(email)
                .token(token)
                .expiration(expiration)
                .build();
    }
}