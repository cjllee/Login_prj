package io.cj.login_project.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    private final String secret;
    private final long accessTokenValidity;

    public JwtUtil(@Value("${jwt.secret:default-secret-key}") String secret,
                   @Value("${jwt.access-token-validity:3600000}") long accessTokenValidity) {
        if (secret == null || secret.isEmpty()) {
            throw new IllegalArgumentException("JWT secret key must not be null or empty");
        }
        this.secret = secret;
        this.accessTokenValidity = accessTokenValidity;
    }

    public String generateToken(String username, List<String> roles) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + accessTokenValidity))
                .signWith(SignatureAlgorithm.HS256, secret.getBytes()) // 바이트 배열로 변환
                .compact();
    }

    public String getSecret() {
        return secret;
    }
}