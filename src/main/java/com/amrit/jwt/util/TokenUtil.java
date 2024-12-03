package com.amrit.jwt.util;

import com.amrit.jwt.entity.AppUser;
import com.amrit.jwt.entity.Token;
import com.amrit.jwt.repository.TokenRepo;

import java.util.List;
import java.util.Optional;

public class TokenUtil {
    public static Token saveToken(Optional<AppUser> appUser, String accessToken, TokenRepo tokenRepo, String refreshToken) {
        Token token = new Token();
        token.setAppUser(appUser.get());
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setLoggedOut(false);
        return tokenRepo.save(token);
    }

    public static Boolean revokeAllTokensByUser(Optional<AppUser> appUser, TokenRepo tokenRepo) {
        List<Token> tokens = tokenRepo.findByAppUserAndIsLoggedOutFalse(appUser.get());
        if (!tokens.isEmpty()) {
            tokens.forEach(token -> {
                token.setLoggedOut(true);
                tokenRepo.save(token);
            });
            return true;
        }
        return true;
    }
}
