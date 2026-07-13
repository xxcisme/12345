package com.uestc.group14.backend.common.enums;

import com.uestc.group14.backend.common.result.ErrorCode;

/**
 * 全局错误码枚举
 * 000~099 保留
 * 100~599 参照HTTP响应状态码，见 https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Reference/Status
 * 600~900 自定义
 */
public interface GlobalErrorCodeConstants {

    // ========== 成功 ==========
    ErrorCode SUCCESS = new ErrorCode(200, "OK 请求成功");

    // ========== 客户端错误段 ==========
    ErrorCode BAD_REQUEST          = new ErrorCode(400, "Bad Request 请求参数错误");
    ErrorCode AUTHENTICATE_FAILED  = new ErrorCode(400, "Failed 账号或密码错误");
    ErrorCode UNAUTHORIZED         = new ErrorCode(401, "Unauthorized 未登录");
    ErrorCode FORBIDDEN            = new ErrorCode(403, "Forbidden 没有访问权限");
    ErrorCode NOT_FOUND            = new ErrorCode(404, "Not Found 未找到请求的资源");
    ErrorCode METHOD_NOT_ALLOWED   = new ErrorCode(405, "Method Not Allowed 不支持该请求方法");
    ErrorCode LOCKED               = new ErrorCode(423, "Locked 资源已锁定");
    ErrorCode TOO_MANY_REQUESTS    = new ErrorCode(429, "Too Many Requests 请求过于频繁，请稍后重试");

    // ========== 服务端错误段 ==========
    ErrorCode QUERY_FAILED  = new ErrorCode(500, "Failed 查询失败");
    ErrorCode ADD_FAILED    = new ErrorCode(500, "Failed 新增失败");
    ErrorCode DELETE_FAILED = new ErrorCode(500, "Failed 删除失败");
    ErrorCode UPDATE_FAILED = new ErrorCode(500, "Failed 修改失败");
    ErrorCode INTERNAL_SERVER_ERROR = new ErrorCode(500, "Internal Server Error 系统异常");
    ErrorCode NOT_IMPLEMENTED       = new ErrorCode(501, "Not Implemented 功能未开启");

    // ========== 权限错误 ==========
    ErrorCode NO_PERMISSION = new ErrorCode(3000, "无权限访问");

    // ========== 课程错误 ==========
    ErrorCode COURSE_NOT_FOUND = new ErrorCode(6003, "课程不存在");

    // ========== 实验错误 ==========
    ErrorCode EXPERIMENT_NOT_FOUND = new ErrorCode(6007, "实验不存在");

    // ========== 自定义错误段 ==========
    ErrorCode UNKNOWN = new ErrorCode(999, "Unknown 未知错误");
}