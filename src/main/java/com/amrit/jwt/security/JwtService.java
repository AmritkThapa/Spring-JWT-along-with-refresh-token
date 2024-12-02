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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;


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

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date(System.currentTimeMillis()));
    }

    public boolean isTokenValid(String token, UserDetails appUser) {
        String username = extractUsername(token);
        Boolean isValidToken = tokenRepo.findByToken(token)
                .map(t->!t.isLoggedOut()).orElse(false);
        return (username.equals(appUser.getUsername())) && !isTokenExpired(token) && isValidToken;
    }


    public String generateToken(AppUser appUser) {
        if (appUser == null) {
            log.info("Error: AppUser argument cannot be null.");
            throw new IllegalArgumentException("AppUser argument cannot be null.");
        }
        return Jwts.builder()
                .subject(appUser.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .claim("roles", appUser.getAuthorities())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSighingKey())
                .compact();
    }


}
