package com.amrit.jwt.service;

import com.amrit.jwt.dto.ApiResponse;
import com.amrit.jwt.dto.AuthenticationRequest;

public interface AuthenticationService {
    ApiResponse<?> authenticateUser(AuthenticationRequest authenticationRequest);
}
