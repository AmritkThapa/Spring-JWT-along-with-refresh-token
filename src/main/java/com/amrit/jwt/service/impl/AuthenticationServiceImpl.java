package com.amrit.jwt.service.impl;

import com.amrit.jwt.dto.ApiResponse;
import com.amrit.jwt.dto.AuthenticationRequest;
import com.amrit.jwt.dto.AuthenticationResponse;
import com.amrit.jwt.entity.AppUser;
import com.amrit.jwt.entity.Token;
import com.amrit.jwt.repository.AppUserRepo;
import com.amrit.jwt.repository.TokenRepo;
import com.amrit.jwt.security.JwtService;
import com.amrit.jwt.service.AuthenticationService;
import com.amrit.jwt.util.ResponseUtil;
import com.amrit.jwt.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AppUserRepo appUserRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepo tokenRepo;

    @Override
    public ApiResponse<?> authenticateUser(AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(),
                    authenticationRequest.getPassword()
            ));
            Optional<AppUser> appUser = appUserRepo.findByUsername(authenticationRequest.getUsername());
            if (appUser.isEmpty()) {
                return ResponseUtil.getFailureResponse("No user found with this username");
            }

            Boolean revokeToken = TokenUtil.revokeAllTokensByUser(appUser, tokenRepo);
            if (!revokeToken) {
                return ResponseUtil.getFailureResponse("Failed to revoke previous tokens");
            }
            Token token =  TokenUtil.saveToken(appUser,
                    jwtService.generateAccessToken(appUser.get()),
                    tokenRepo,
                    jwtService.generateRefreshToken(appUser.get()));

            if (token!=null) {
                AuthenticationResponse authenticationResponse = new AuthenticationResponse();
                authenticationResponse.setAccessToken(token.getAccessToken());
                authenticationResponse.setRefreshToken(token.getRefreshToken());
                return ResponseUtil.getSuccessfulApiResponse(authenticationResponse, "Successfully Logged in.");
            }
            return ResponseUtil.getFailureResponse("Token generation failed");
        }catch (Exception e){
            log.info("Error: {}", e.getMessage());
            return ResponseUtil.getFailureResponse("Invalid username or password");
        }
    }

    @Override
    public ApiResponse<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseUtil.getFailureResponse("Invalid token");
        }

        String refreshToken = authHeader.substring(7);
        String username = jwtService.extractUsername(refreshToken);
        Optional<AppUser> appUser = appUserRepo.findByUsername(username);
        if (appUser.isEmpty()) {
            return ResponseUtil.getFailureResponse("No user found with this username");
        }
        Boolean revokeToken = TokenUtil.revokeAllTokensByUser(appUser, tokenRepo);
        if (!revokeToken) {
            return ResponseUtil.getFailureResponse("Failed to revoke previous tokens");
        }
        Token token =  TokenUtil.saveToken(appUser,
                jwtService.generateAccessToken(appUser.get()),
                tokenRepo,
                jwtService.generateRefreshToken(appUser.get()));
        if (token!=null) {
            AuthenticationResponse authenticationResponse = new AuthenticationResponse();
            authenticationResponse.setAccessToken(token.getAccessToken());
            authenticationResponse.setRefreshToken(token.getRefreshToken());
            return ResponseUtil.getSuccessfulApiResponse(authenticationResponse, "Successfully refreshed token.");
        }
        return ResponseUtil.getFailureResponse("Invalid refresh token");
    }


}
