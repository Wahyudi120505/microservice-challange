package com.example.user_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.user_service.models.User;
import com.example.user_service.models.UserToken;

public interface UserTokenRepository extends JpaRepository<UserToken, String> {
    Optional<UserToken> findByUserAndExpiredFalse(User user);
    Optional<UserToken> findByRefreshTokenAndExpiredFalse(String refreshToken);
}
