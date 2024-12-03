package com.amrit.jwt.service;

import com.amrit.jwt.dto.ApiResponse;
import com.amrit.jwt.dto.AuthenticationRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthenticationService {
    ApiResponse<?> authenticateUser(AuthenticationRequest authenticationRequest);
    ApiResponse<?> refreshToken(HttpServletRequest request, HttpServletResponse response);
}
