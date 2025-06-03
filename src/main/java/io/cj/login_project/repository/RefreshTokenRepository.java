package io.cj.login_project.repository;

import io.cj.login_project.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    // 기본 CRUD 메서드 사용
}
