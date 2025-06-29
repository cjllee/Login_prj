package io.cj.login_project.service;

import io.cj.login_project.entity.RefreshToken;
import io.cj.login_project.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public String generateRefreshToken(String email) {
        String token = UUID.randomUUID().toString();
        long expiration = 60000; // 1분(60,000ms)
        RefreshToken refreshToken = RefreshToken.createToken(email, token, expiration);
        refreshTokenRepository.save(refreshToken);
        return token;
    }

    public boolean validateRefreshToken(String email, String token) {
        RefreshToken storedToken = refreshTokenRepository.findById(email).orElse(null);
        return storedToken != null && storedToken.getToken().equals(token);
    }

    public void deleteRefreshToken(String email) {
        refreshTokenRepository.deleteById(email);
    }
}