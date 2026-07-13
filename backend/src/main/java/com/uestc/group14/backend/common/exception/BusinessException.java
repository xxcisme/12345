package com.uestc.group14.backend.common.exception;

import com.uestc.group14.backend.common.result.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final Integer code;
    private final String message;

    // 构造1：接受 result.ErrorCode（普通类）
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.code = errorCode.getCode();
        this.message = errorCode.getMsg();
    }

    // 构造2：接受 exception.ErrorCode（枚举）
    public BusinessException(com.uestc.group14.backend.common.exception.ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    // 构造3：直接传 code 和 message
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}