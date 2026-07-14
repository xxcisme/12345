package com.uestc.group14.backend.common.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uestc.group14.backend.common.utils.RequestUtil;
import com.uestc.group14.backend.service.OperationLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

/**
 * 操作日志自动记录切面
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class OperationLogAspect {

    private final OperationLogService operationLogService;
    private final ObjectMapper objectMapper;

    @Pointcut("execution(* com.uestc.group14.backend.controller..*.*(..))")
    public void controllerPointcut() {}

    private static final String[] EXCLUDE_PATHS = {
            "/v3/api-docs", "/swagger-ui", "/swagger-resources", "/webjars",
            "/doc.html", "/error", "/favicon.ico"
    };

    @Around("controllerPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return joinPoint.proceed();
        }
        HttpServletRequest request = attributes.getRequest();
        String requestURI = request.getRequestURI();

        for (String exclude : EXCLUDE_PATHS) {
            if (requestURI.startsWith(exclude)) {
                return joinPoint.proceed();
            }
        }

        Long userId = null;
        String username = "anonymous";
        String userRole = "未知";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof Long) {
                userId = (Long) principal;
                Object attrUsername = request.getAttribute("username");
                Object attrRole = request.getAttribute("role");
                if (attrUsername != null) username = attrUsername.toString();
                if (attrRole != null) {
                    userRole = convertRole(Integer.parseInt(attrRole.toString()));
                }
            } else {
                // 未认证或匿名用户，不记录
                log.debug("未认证或匿名用户，跳过日志记录");
            }
        }

        // 跳过登录注册接口的自动记录（由 AuthService 手动记录）
        if (requestURI.contains("/login") || requestURI.contains("/register")) {
            return joinPoint.proceed();
        }

        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();
        String params = args.length > 0 ? Arrays.toString(args) : "";
        if (params.length() > 200) {
            params = params.substring(0, 200) + "...";
        }

        String action = className + "." + methodName + "(" + params + ")";
        String ip = RequestUtil.getClientIp(request);

        long startTime = System.currentTimeMillis();
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            log.error("操作异常: {}", action, e);
            throw e;
        } finally {
            long cost = System.currentTimeMillis() - startTime;
            if (userId != null || !"anonymous".equals(username)) {
                operationLogService.log(
                        userId,
                        userRole,
                        username,
                        action + " [耗时" + cost + "ms]",
                        ip
                );
                log.debug("操作日志已记录: user={}, action={}", username, action);
            }
        }

        return result;
    }

    private String convertRole(Integer role) {
        if (role == null) return "未知";
        switch (role) {
            case 1: return "学生";
            case 2: return "老师";
            case 3: return "社会人士";
            case 4: return "管理员";
            default: return "未知";
        }
    }
}