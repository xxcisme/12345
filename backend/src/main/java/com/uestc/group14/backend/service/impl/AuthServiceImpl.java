package com.uestc.group14.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.uestc.group14.backend.common.enums.AuthErrorCodeConstants;
import com.uestc.group14.backend.common.exception.BusinessException;
import com.uestc.group14.backend.common.utils.JwtUtil;
import com.uestc.group14.backend.common.utils.Md5Util;
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

/**
 * 认证Service实现类
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserProfileMapper userProfileMapper;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 用户登录
     *
     * @param loginRequest 登录请求
     * @param request      HttpServletRequest
     * @return 登录响应
     */
    @Override
    public LoginResponse login(LoginRequest loginRequest, HttpServletRequest request) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPasswordHash();

        // 1. 参数校验
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            log.warn("登录失败 - 用户名或密码为空");
            throw new BusinessException(AuthErrorCodeConstants.PASSWORD_INVALID);
        }

        // 2. 查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(queryWrapper);

        // 3. 用户不存在
        if (user == null) {
            log.warn("登录失败 - 用户名不存在: {}", username);
            throw new BusinessException(AuthErrorCodeConstants.USER_NOT_FOUND);
        }

        // 4. 校验密码
        String encryptedPassword = Md5Util.encrypt(password);
        if (!encryptedPassword.equals(user.getPasswordHash())) {
            log.warn("登录失败 - 密码错误: {}", username);
            throw new BusinessException(AuthErrorCodeConstants.PASSWORD_ERROR);
        }

        // 5. 校验账号状态
        if (user.getStatu() != null && user.getStatu() == 0) {
            log.warn("登录失败 - 账号已停用: {}", username);
            throw new BusinessException(AuthErrorCodeConstants.ACCOUNT_DISABLED);
        }

        // 6. 更新最后登录信息
        user.setLastLoginTime(LocalDateTime.now());
        user.setLastLoginIp(RequestUtil.getClientIp(request));
        userMapper.updateById(user);

        // 7. 生成JWT Token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());

        log.info("用户登录成功 - username: {}, userId: {}", username, user.getId());

        // 8. 构建响应
        return LoginResponse.builder()
                .token(token)
                .userId(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

    /**
     * 用户注册
     *
     * @param registerRequest 注册请求
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterRequest registerRequest) {
        String username = registerRequest.getUsername();
        String password = registerRequest.getPasswordHash(); // 注意：这里实际传入的是明文密码
        String confirmPassword = registerRequest.getConfirmPassword();
        String realName = registerRequest.getRealName();

        // 1. 参数校验
        if (!StringUtils.hasText(username)) {
            throw new BusinessException(1002, "用户名不能为空");
        }
        if (!StringUtils.hasText(password)) {
            throw new BusinessException(1002, "密码不能为空");
        }
        if (!StringUtils.hasText(confirmPassword)) {
            throw new BusinessException(1002, "确认密码不能为空");
        }

        // 2. 校验两次密码是否一致
        if (!password.equals(confirmPassword)) {
            log.warn("注册失败 - 两次密码不一致: {}", username);
            throw new BusinessException(AuthErrorCodeConstants.PASSWORD_MISMATCH);
        }

        // 3. 校验用户名是否已存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        Long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            log.warn("注册失败 - 用户名已存在: {}", username);
            throw new BusinessException(AuthErrorCodeConstants.USERNAME_EXISTS);
        }

        // 4. 创建用户
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(Md5Util.encrypt(password));
        user.setRole(3); // 社会人士角色
        user.setPhone(registerRequest.getPhone());
        user.setEmail(registerRequest.getEmail());
        user.setStatu(1); // 默认启用
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        userMapper.insert(user);

        // 5. 创建用户详细信息
        if (StringUtils.hasText(realName)) {
            UserProfile userProfile = new UserProfile();
            userProfile.setUserId(user.getId());
            userProfile.setRealName(realName);
            userProfile.setCreateTime(LocalDateTime.now());
            userProfile.setUpdateTime(LocalDateTime.now());
            userProfileMapper.insert(userProfile);
        }

        log.info("用户注册成功 - username: {}, userId: {}", username, user.getId());
    }
}