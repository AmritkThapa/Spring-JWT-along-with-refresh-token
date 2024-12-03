package com.amrit.jwt.repository;

import com.amrit.jwt.entity.AppUser;
import com.amrit.jwt.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepo extends JpaRepository<Token, Long> {
    List<Token> findByAppUserAndIsLoggedOutFalse(AppUser appUser);

    Optional<Token> findByAccessToken(String accessToken);
    Optional<Token> findByRefreshToken(String refreshToken);
}
