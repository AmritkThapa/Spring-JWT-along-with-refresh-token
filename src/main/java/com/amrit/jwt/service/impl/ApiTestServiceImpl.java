package com.amrit.jwt.service.impl;

import com.amrit.jwt.dto.ApiResponse;
import com.amrit.jwt.dto.AppUserModel;
import com.amrit.jwt.entity.AppUser;
import com.amrit.jwt.repository.AppUserRepo;
import com.amrit.jwt.service.ApiTestService;
import com.amrit.jwt.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiTestServiceImpl implements ApiTestService {
    private final AppUserRepo appUserRepo;
    @Override
    public ApiResponse<?> getAllUsers() {
        List<AppUser> appUsers = appUserRepo.findAll();
        List<AppUserModel> appUserModels = appUsers.stream()
                .map(appUser -> new AppUserModel(appUser.getUsername(), appUser.getName()))
                .toList();
        return ResponseUtil.getSuccessfulApiResponse(appUserModels, "Users fetched successfully");
    }
}
