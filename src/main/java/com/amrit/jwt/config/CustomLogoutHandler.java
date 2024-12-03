package com.amrit.jwt.config;

import com.amrit.jwt.constant.ServerResponseCodeConstant;
import com.amrit.jwt.dto.ApiResponse;
import com.amrit.jwt.entity.Token;
import com.amrit.jwt.repository.TokenRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomLogoutHandler implements LogoutHandler {
    private final ObjectMapper objectMapper;
    private final TokenRepo tokenRepo;

    public CustomLogoutHandler(TokenRepo tokenRepo) {
        this.tokenRepo = tokenRepo;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        String token = authHeader.substring(7);
        Token stroedToken = tokenRepo.findByAccessToken(token).orElse(null);

        if (stroedToken != null) {
            stroedToken.setLoggedOut(true);
            tokenRepo.save(stroedToken);
            response.setContentType("application/json");
            ApiResponse<?> apiResponse = ApiResponse.builder()
                    .code(ServerResponseCodeConstant.SUCCESS)
                    .message("Logged out successfully")
                    .httpStatus(HttpStatus.OK)
                    .timestamp(LocalDateTime.now())
                    .build();
            try {
                response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
