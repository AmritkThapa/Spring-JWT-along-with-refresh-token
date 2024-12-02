package com.amrit.jwt.controller;

import com.amrit.jwt.constant.ApiConstant;
import com.amrit.jwt.dto.ApiResponse;
import com.amrit.jwt.service.ApiTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiConstant.TEST)
public class ApiTestController {
    private final ApiTestService apiTestService;

    @GetMapping(ApiConstant.USERS)
    public ApiResponse<?> getAllUsers() {
        return apiTestService.getAllUsers();
    }

}
