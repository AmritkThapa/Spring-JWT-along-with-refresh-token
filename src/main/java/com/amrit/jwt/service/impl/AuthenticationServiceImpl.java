package com.amrit.jwt.service.impl;

import com.amrit.jwt.dto.ApiResponse;
import com.amrit.jwt.dto.AuthenticationRequest;
import com.amrit.jwt.entity.AppUser;
import com.amrit.jwt.entity.Token;
import com.amrit.jwt.repository.AppUserRepo;
import com.amrit.jwt.repository.TokenRepo;
import com.amrit.jwt.security.JwtService;
import com.amrit.jwt.service.AuthenticationService;
import com.amrit.jwt.util.ResponseUtil;
import com.amrit.jwt.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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
            String token = TokenUtil.saveToken(appUser, jwtService.generateToken(appUser.get()), tokenRepo);
            if (token!=null) {
                return ResponseUtil.getSuccessfulApiResponse(token, "Successfully Logged in.");
            }
            return ResponseUtil.getFailureResponse("Token generation failed");
        }catch (Exception e){
            log.info("Error: {}", e.getMessage());
            return ResponseUtil.getFailureResponse("Invalid username or password");
        }
    }




}
