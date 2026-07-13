package com.uestc.group14.backend.controller;

import com.uestc.group14.backend.Entity.UserEntity;
import com.uestc.group14.backend.common.result.CommonResult;
import com.uestc.group14.backend.common.utils.JwtUtil;
import com.uestc.group14.backend.dao.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 临时测试接口 - 用于获取 JWT Token（测试完成后可删除）
 */
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestAuthController {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    @GetMapping("/token")
    public CommonResult<String> getToken(@RequestParam Long userId) {
        UserEntity user = userMapper.selectById(userId);
        if (user == null) {
            return CommonResult.error(404, "用户不存在，请确保数据库中已有用户数据");
        }
        String token = jwtUtil.generateToken(userId);
        return CommonResult.success(token);
    }
}