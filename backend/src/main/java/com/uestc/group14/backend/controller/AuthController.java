package com.uestc.group14.backend.controller;

import com.uestc.group14.backend.dto.LoginRequest;
import com.uestc.group14.backend.dto.LoginResponse;
import com.uestc.group14.backend.dto.RegisterRequest;
import com.uestc.group14.backend.common.result.CommonResult;
import com.uestc.group14.backend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 认证Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "认证管理", description = "用户认证相关接口")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 用户登录
     *
     * @param loginRequest 登录请求
     * @param request HttpServletRequest
     * @return 登录响应
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录接口，返回JWT Token")
    public CommonResult<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest,
                                             HttpServletRequest request) {
        log.info("用户登录请求 - username: {}", loginRequest.getUsername());
        LoginResponse loginResponse = authService.login(loginRequest, request);
        return CommonResult.success(loginResponse);
    }

    /**
     * 用户注册
     *
     * @param registerRequest 注册请求
     * @return 成功响应
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "用户注册接口")
    public CommonResult<Void> register(@Valid @RequestBody RegisterRequest registerRequest) {
        log.info("用户注册请求 - username: {}", registerRequest.getUsername());
        authService.register(registerRequest);
        return CommonResult.success();
    }

    /**
     * 用户退出登录
     *
     * @return 成功响应
     */
    @PostMapping("/logout")
    @Operation(summary = "用户退出登录", description = "用户退出登录接口")
    public CommonResult<Void> logout() {
        log.info("用户退出登录");
        return CommonResult.success();
    }
}