package io.cj.login_project.service;

import io.cj.login_project.entity.User;
import io.cj.login_project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public void saveUser(User user) {
        // 비밀번호를 평문으로 저장
        userRepository.save(user);
    }
}