package com.amrit.jwt.config;

import com.amrit.jwt.dto.ApiResponse;
import com.amrit.jwt.entity.Token;
import com.amrit.jwt.repository.TokenRepo;
import com.amrit.jwt.util.ResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
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
//            ApiResponse<Object> apiResponse =
            ResponseUtil.getFailureResponse("Invalid token");
//            writeResponse(response, apiResponse);
//            return;
        }

        String token = authHeader.substring(7);
        Token stroedToken = tokenRepo.findByAccessToken(token).orElse(null);

        if (stroedToken != null) {
            stroedToken.setLoggedOut(true);
            tokenRepo.save(stroedToken);
            log.info("Logged out successfully");
            ResponseUtil.getSuccessfulApiResponse("Logged out successfully");
//            writeResponse(response, apiResponse);
//            return;
        }
//        ApiResponse<Object> apiResponse =
        ResponseUtil.getFailureResponse("Logout Unsuccessful");
//        writeResponse(response, apiResponse);
    }

//    private void writeResponse(HttpServletResponse response, ApiResponse<?> apiResponse) {
//        response.setContentType("application/json");
//        try {
//            response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
