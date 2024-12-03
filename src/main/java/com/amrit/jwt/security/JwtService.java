package com.amrit.jwt.security;

import com.amrit.jwt.entity.AppUser;
import com.amrit.jwt.repository.TokenRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;


@Service
@Slf4j
@RequiredArgsConstructor
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;
    private final TokenRepo tokenRepo;

    private SecretKey getSighingKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSighingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenNotExpired(String token) {
        return !extractExpiration(token).before(new Date(System.currentTimeMillis()));
    }

    public boolean isTokenValid(String token, UserDetails appUser) {
        String username = extractUsername(token);
        Boolean isValidToken = tokenRepo.findByAccessToken(token)
                .map(t->!t.isLoggedOut()).orElse(false);
        return (username.equals(appUser.getUsername())) && isTokenNotExpired(token) && isValidToken;
    }
    public boolean isValidRefreshToken(String token, AppUser appUser) {
        String username = extractUsername(token);
        Boolean isValidToken = tokenRepo.findByRefreshToken(token)
                .map(t->!t.isLoggedOut()).orElse(false);
        return (username.equals(appUser.getUsername())) && isTokenNotExpired(token) && isValidToken;
    }

    private String generateToken(AppUser appUser, long expireTime) {
        if (appUser == null) {
            log.info("Error: AppUser argument cannot be null.");
            throw new IllegalArgumentException("AppUser argument cannot be null.");
        }
        return Jwts.builder()
                .subject(appUser.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .claim("roles", appUser.getAuthorities())
                .expiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(getSighingKey())
                .compact();
    }

    public String generateAccessToken(AppUser appUser) {
        return generateToken(appUser, 1000 * 30);
    }

    public String generateRefreshToken(AppUser appUser) {
        return generateToken(appUser, 1000 * 60 * 60 * 24 * 7);
    }



}
