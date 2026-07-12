package com.uestc.group14.backend.common.enums;


import com.uestc.group14.backend.common.result.ErrorCode;

/**
 * 全局错误码枚举
 */
public interface GlobalErrorCodeConstants {

    // ========== 成功 ==========
    ErrorCode SUCCESS = new ErrorCode(200, "请求成功");

    // ========== 操作失败（服务端自定义） ==========
    ErrorCode UPDATE_ERROR = new ErrorCode(500, "修改失败");    // 表格中为“修改失败”
    ErrorCode ADD_ERROR    = new ErrorCode(500, "新增失败");    // 与表格一致
    ErrorCode DELETE_ERROR = new ErrorCode(500, "删除失败");    // 与表格一致

    // ========== 客户端错误段 ==========
    ErrorCode BAD_REQUEST          = new ErrorCode(400, "请求参数错误");        // 原“不正确”改为“错误”
    ErrorCode UNAUTHORIZED         = new ErrorCode(401, "未登录");              // 去掉“账号”二字
    ErrorCode LOGIN_ERROR          = new ErrorCode(402, "账号或密码错误");      // 表格中无此码，保留原消息
    ErrorCode FORBIDDEN            = new ErrorCode(403, "没有访问权限");        // 原“该操作权限”改为“访问权限”
    ErrorCode NOT_FOUND            = new ErrorCode(404, "未找到请求的资源");    // 使用表格标准消息
    ErrorCode METHOD_NOT_ALLOWED   = new ErrorCode(405, "不支持该请求方法");    // 原“不正确”改为“不支持”
    ErrorCode LOCKED               = new ErrorCode(423, "资源已锁定");          // 原“请求失败...”改为标准消息
    ErrorCode TOO_MANY_REQUESTS    = new ErrorCode(429, "请求过于频繁，请稍后重试"); // 与表格一致

    // ========== 服务端错误段 ==========
    ErrorCode INTERNAL_SERVER_ERROR = new ErrorCode(500, "系统异常");           // 与表格一致
    ErrorCode NOT_IMPLEMENTED       = new ErrorCode(501, "功能未开启");         // 去掉“未实现/”

    // ========== 自定义错误段（保持原样） ==========
    ErrorCode UNKNOWN = new ErrorCode(999, "未知错误");

}
