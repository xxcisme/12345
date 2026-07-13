package com.uestc.group14.backend.service;

import com.uestc.group14.backend.dto.LoginRequest;
import com.uestc.group14.backend.dto.LoginResponse;
import com.uestc.group14.backend.dto.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 认证Service接口
 */
public interface AuthService {

    /**
     * 用户登录
     *
     * @param loginRequest 登录请求
     * @param request HttpServletRequest
     * @return 登录响应
     */
    LoginResponse login(LoginRequest loginRequest, HttpServletRequest request);

    /**
     * 用户注册
     *
     * @param registerRequest 注册请求
     */
    void register(RegisterRequest registerRequest);
}