package com.uestc.group14.backend.common.exception;

import com.uestc.group14.backend.common.result.CommonResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public CommonResult<?> handleBusinessException(BusinessException ex, HttpServletRequest request) {
        log.warn("业务异常 - URI: {}, 错误码: {}, 错误信息: {}",
                request.getRequestURI(), ex.getCode(), ex.getMessage());
        return CommonResult.error(ex.getCode(), ex.getMessage());
    }

    /**
     * 处理参数校验异常（@Valid）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResult<?> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldError().getDefaultMessage();
        log.warn("参数校验异常 - URI: {}, 错误信息: {}", request.getRequestURI(), message);
        return CommonResult.error(400, message);
    }

    /**
     * 处理参数绑定异常
     */
    @ExceptionHandler(BindException.class)
    public CommonResult<?> handleBindException(BindException ex, HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldError().getDefaultMessage();
        log.warn("参数绑定异常 - URI: {}, 错误信息: {}", request.getRequestURI(), message);
        return CommonResult.error(400, message);
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    public CommonResult<?> handleException(Exception ex, HttpServletRequest request) {
        log.error("系统异常 - URI: {}, 错误信息: {}", request.getRequestURI(), ex.getMessage(), ex);
        return CommonResult.error(500, "系统异常");
    }
}