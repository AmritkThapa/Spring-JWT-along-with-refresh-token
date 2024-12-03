package com.amrit.jwt.exception;

import com.amrit.jwt.constant.ServerResponseCodeConstant;
import com.amrit.jwt.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public CustomAccessDeniedHandler() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json");

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(ServerResponseCodeConstant.FAILURE)
                .message("You are not authorized!")
                .httpStatus(HttpStatus.FORBIDDEN)
                .timestamp(LocalDateTime.now())
                .build();
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}
