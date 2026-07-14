package com.uestc.group14.backend.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // 通用错误 1000-1099
    SUCCESS(200, "操作成功"),
    SYSTEM_ERROR(1000, "系统内部错误"),
    PARAM_ERROR(1001, "参数错误"),
    PARAM_NULL(1002, "参数不能为空"),
    PARAM_FORMAT_ERROR(1003, "参数格式错误"),
    REQUEST_TOO_FREQUENT(1004, "请求过于频繁"),

    // 登录认证 2000-2099
    TOKEN_EXPIRED(2000, "登录已过期，请重新登录"),
    TOKEN_INVALID(2001, "无效的Token"),
    TOKEN_NULL(2002, "Token不能为空"),
    LOGIN_FAILED(2003, "登录失败，用户名或密码错误"),
    ACCOUNT_DISABLED(2004, "账号已被停用，请联系管理员"),
    ACCOUNT_NOT_EXIST(2005, "用户名不存在"),
    PASSWORD_ERROR(2006, "密码错误"),
    PASSWORD_FORMAT_ERROR(2007, "密码格式不符合要求"),
    OLD_PASSWORD_ERROR(2008, "原密码错误"),
    TWO_PASSWORD_NOT_MATCH(2009, "两次输入的密码不一致"),
    CAPTCHA_ERROR(2010, "验证码错误"),
    CAPTCHA_EXPIRED(2011, "验证码已过期"),

    // 注册相关 2100-2199
    USERNAME_EXIST(2100, "用户名已被占用"),
    PHONE_EXIST(2101, "手机号已被注册"),
    EMAIL_EXIST(2102, "邮箱已被注册"),
    REGISTER_FAILED(2103, "注册失败"),

    // 权限相关 3000-3099
    NO_PERMISSION(3000, "无权限访问"),
    ROLE_NOT_MATCH(3001, "角色不匹配"),
    FORBIDDEN(3002, "禁止访问"),

    // 用户中心 4000-4099
    USER_NOT_FOUND(4000, "用户不存在"),
    PROFILE_UPDATE_FAILED(4001, "个人信息更新失败"),
    FAVORITE_EXIST(4002, "已收藏该资源"),
    FAVORITE_NOT_FOUND(4003, "收藏记录不存在"),
    MESSAGE_NOT_FOUND(4004, "消息不存在"),

    // 资源中心 5000-5099
    RESOURCE_NOT_FOUND(5000, "资源不存在"),
    RESOURCE_AUDITING(5001, "资源审核中，暂不可访问"),
    RESOURCE_REJECTED(5002, "资源已被驳回"),
    RESOURCE_TYPE_ERROR(5003, "资源类型错误"),
    FILE_UPLOAD_FAILED(5004, "文件上传失败"),
    FILE_SIZE_EXCEED(5005, "文件大小超出限制"),
    FILE_FORMAT_ERROR(5006, "文件格式不支持"),
    RATING_EXIST(5007, "您已评分过该资源"),
    RATING_RANGE_ERROR(5008, "评分必须在1-5之间"),

    // 实验室/设备 5100-5199
    LABORATORY_NOT_FOUND(5100, "实验室不存在"),
    LABORATORY_NAME_EXIST(5101, "实验室名称已存在"),
    LABORATORY_NUMBER_EXIST(5102, "实验室编号已存在"),
    LABORATORY_IN_USE(5103, "实验室已被预约，不可删除"),
    DEVICE_NOT_FOUND(5104, "设备不存在"),
    DEVICE_NUMBER_EXIST(5105, "设备编号已存在"),
    DEVICE_STATUS_ERROR(5106, "设备状态异常"),
    LAB_APPLICATION_NOT_FOUND(5107, "申请记录不存在"),
    LAB_APPLICATION_TIME_CONFLICT(5108, "该时段已被预约"),
    LAB_APPLICATION_ALREADY_APPROVED(5109, "申请已处理"),

    // 实训中心 6000-6099
    TEACHER_NOT_FOUND(6000, "教师不存在"),
    TEACHER_NUMBER_EXIST(6001, "教师编号已存在"),
    TEACHER_HAS_COURSE(6002, "该教师有关联课程，不可删除"),
    COURSE_NOT_FOUND(6003, "课程不存在"),
    COURSE_CODE_EXIST(6004, "课程编号已存在"),
    COURSE_HAS_STUDENT(6005, "课程已有学生报名，不可删除"),
    COURSE_NOT_PUBLISHED(6006, "课程未发布"),
    EXPERIMENT_NOT_FOUND(6007, "实验不存在"),
    EXPERIMENT_HAS_REPORT(6008, "已有学生提交报告，不可删除"),
    PLAN_NOT_FOUND(6009, "教学计划不存在"),
    PLAN_TIME_CONFLICT(6010, "实验时间冲突"),
    PLAN_NOT_DRAFT(6011, "只有草稿状态可编辑"),
    SCHEDULE_NOT_FOUND(6012, "排课记录不存在"),

    // 成绩评定 6100-6199
    GRADE_NOT_FOUND(6100, "成绩记录不存在"),
    GRADE_ALREADY_PUBLISHED(6101, "成绩已发布，不可修改"),
    GRADE_RANGE_ERROR(6102, "成绩必须在0-100之间"),
    GRADE_NOT_PUBLISHED(6103, "成绩尚未发布"),
    REPORT_NOT_FOUND(6104, "实验报告不存在"),
    REPORT_NOT_SUBMITTED(6105, "学生尚未提交报告"),

    // 新闻公告 7000-7099
    NEWS_NOT_FOUND(7000, "新闻不存在"),
    NEWS_TYPE_ERROR(7001, "新闻类型错误"),

    // 系统管理 8000-8099
    LOG_EXPORT_FAILED(8000, "日志导出失败"),
    LOG_CLEAN_FAILED(8001, "日志清理失败"),
    IMPORT_FORMAT_ERROR(8002, "导入文件格式错误"),
    IMPORT_DATA_ERROR(8003, "导入数据格式错误"),

    // 数据库 9000-9099
    DB_ERROR(9000, "数据库操作异常"),
    DB_DUPLICATE_KEY(9001, "数据重复"),
    DB_FOREIGN_KEY(9002, "存在关联数据，无法删除");


    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}