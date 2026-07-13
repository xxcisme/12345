package com.uestc.group14.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.uestc.group14.backend.common.enums.AuthErrorCodeConstants;
import com.uestc.group14.backend.common.exception.BusinessException;
import com.uestc.group14.backend.common.utils.JwtUtil;
import com.uestc.group14.backend.common.utils.Sha256Util;
import com.uestc.group14.backend.common.utils.RequestUtil;
import com.uestc.group14.backend.dao.UserMapper;
import com.uestc.group14.backend.dao.UserProfileMapper;
import com.uestc.group14.backend.dto.LoginRequest;
import com.uestc.group14.backend.dto.LoginResponse;
import com.uestc.group14.backend.dto.RegisterRequest;
import com.uestc.group14.backend.entity.User;
import com.uestc.group14.backend.entity.UserProfile;
import com.uestc.group14.backend.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserProfileMapper userProfileMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public LoginResponse login(LoginRequest loginRequest, HttpServletRequest request) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPasswordHash();

        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            log.warn("登录失败 - 用户名或密码为空");
            throw new BusinessException(AuthErrorCodeConstants.PASSWORD_INVALID);
        }

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(queryWrapper);

        if (user == null) {
            log.warn("登录失败 - 用户名不存在: {}", username);
            throw new BusinessException(AuthErrorCodeConstants.USER_NOT_FOUND);
        }

        if (!Sha256Util.verify(password, user.getPasswordHash())) {
            log.warn("登录失败 - 密码错误: {}", username);
            throw new BusinessException(AuthErrorCodeConstants.PASSWORD_ERROR);
        }

        if (user.getStatu() != null && user.getStatu() == 0) {
            log.warn("登录失败 - 账号已停用: {}", username);
            throw new BusinessException(AuthErrorCodeConstants.ACCOUNT_DISABLED);
        }

        user.setLastLoginTime(LocalDateTime.now());
        user.setLastLoginIp(RequestUtil.getClientIp(request));
        userMapper.updateById(user);

        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());

        log.info("用户登录成功 - username: {}, userId: {}", username, user.getId());

        return LoginResponse.builder()
                .token(token)
                .userId(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterRequest registerRequest) {
        String username = registerRequest.getUsername();
        String password = registerRequest.getPasswordHash();
        String confirmPassword = registerRequest.getConfirmPassword();
        String realName = registerRequest.getRealName();

        if (!StringUtils.hasText(username)) {
            throw new BusinessException(1002, "用户名不能为空");
        }
        if (!StringUtils.hasText(password)) {
            throw new BusinessException(1002, "密码不能为空");
        }
        if (!StringUtils.hasText(confirmPassword)) {
            throw new BusinessException(1002, "确认密码不能为空");
        }

        if (!password.equals(confirmPassword)) {
            throw new BusinessException(AuthErrorCodeConstants.PASSWORD_MISMATCH);
        }

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        Long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(AuthErrorCodeConstants.USERNAME_EXISTS);
        }

        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(Sha256Util.encrypt(password));
        user.setRole(3);
        user.setPhone(registerRequest.getPhone());
        user.setEmail(registerRequest.getEmail());
        user.setStatu(1);
        user.setDelFlag(0);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        userMapper.insert(user);

        if (StringUtils.hasText(realName)) {
            UserProfile userProfile = new UserProfile();
            userProfile.setUserId(user.getId());
            userProfile.setRealName(realName);
            userProfile.setSchoolCode(registerRequest.getSchoolCode());
            userProfile.setClassName(registerRequest.getClassName());
            userProfile.setOccupationType("社会人士");
            userProfile.setCreateTime(LocalDateTime.now());
            userProfile.setUpdateTime(LocalDateTime.now());
            userProfileMapper.insert(userProfile);
        }

        log.info("用户注册成功 - username: {}, userId: {}", username, user.getId());
    }
}