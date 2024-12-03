package com.amrit.jwt.controller;

import com.amrit.jwt.constant.ApiConstant;
import com.amrit.jwt.dto.ApiResponse;
import com.amrit.jwt.dto.AuthenticationRequest;
import com.amrit.jwt.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping()
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping(ApiConstant.LOGIN)
    public ApiResponse<?> authenticateUser(@RequestBody AuthenticationRequest authenticationRequest) {
        return authenticationService.authenticateUser(authenticationRequest);
    }

    @PostMapping(ApiConstant.REFRESH_TOKEN)
    public ApiResponse<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        return authenticationService.refreshToken(request, response);
    }
}
