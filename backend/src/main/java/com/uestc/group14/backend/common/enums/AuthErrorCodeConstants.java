package com.uestc.group14.backend.common.enums;

import com.uestc.group14.backend.common.result.ErrorCode;

/**
 * 认证模块错误码
 */
public interface AuthErrorCodeConstants {

    // 用户名不存在
    ErrorCode USER_NOT_FOUND = new ErrorCode(4001, "用户名不存在");

    // 密码错误
    ErrorCode PASSWORD_ERROR = new ErrorCode(4002, "密码错误");

    // 账号已停用
    ErrorCode ACCOUNT_DISABLED = new ErrorCode(4003, "账号已停用");

    // 用户名已存在
    ErrorCode USERNAME_EXISTS = new ErrorCode(4004, "用户名已存在");

    // 密码不符合规则
    ErrorCode PASSWORD_INVALID = new ErrorCode(4005, "密码不符合规则");

    // 手机号格式错误
    ErrorCode PHONE_FORMAT_ERROR = new ErrorCode(4006, "手机号格式错误");

    // 两次密码不一致
    ErrorCode PASSWORD_MISMATCH = new ErrorCode(4007, "两次密码不一致");

    // Token无效或过期
    ErrorCode TOKEN_INVALID = new ErrorCode(4008, "Token无效或过期");
}