package com.uestc.group14.backend.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uestc.group14.backend.common.result.CommonResult;
import com.uestc.group14.backend.common.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * JWT认证过滤器
 * 在Spring Security过滤器链中验证JWT Token
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 白名单路径，无需JWT验证
     */
    private static final String[] WHITELIST_PATHS = {
            "/api/v1/auth/login",
            "/api/v1/auth/register",
            "/api/v1/auth/logout",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/swagger-ui",
            "/swagger-ui/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/webjars/**",
            "/error"
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        // 检查是否在白名单中
        if (isWhitelistPath(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 对所有 /api/v1/** 的请求进行JWT验证
        if (requestURI.startsWith("/api/v1/")) {
            String authHeader = request.getHeader("Authorization");

            if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
                log.warn("请求缺少有效的Authorization header - {}", requestURI);
                sendUnauthorizedError(response, "缺少有效的Authorization header");
                return;
            }

            String token = authHeader.substring(7);

            if (!jwtUtil.validateToken(token)) {
                log.warn("Token无效或已过期 - {}", requestURI);
                sendUnauthorizedError(response, "Token无效或已过期");
                return;
            }

            // Token验证成功，提取用户信息
            try {
                Long userId = jwtUtil.getUserIdFromToken(token);
                String username = jwtUtil.getUsernameFromToken(token);
                Integer role = jwtUtil.getRoleFromToken(token);

                // 将用户信息存入request attribute，供Controller使用
                request.setAttribute("userId", userId);
                request.setAttribute("username", username);
                request.setAttribute("role", role);

                // ★★★ 关键修复：将认证信息存入Spring Security上下文 ★★★
                // 这样后续的 SecurityContextHolder.getContext().getAuthentication() 才能获取到认证信息
                // 避免 Spring Security 的 anyRequest().authenticated() 返回 403
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.debug("JWT认证成功 - userId: {}, username: {}, role: {}, uri: {}", userId, username, role, requestURI);

            } catch (Exception e) {
                log.error("提取Token信息失败 - uri: {}", requestURI, e);
                sendUnauthorizedError(response, "Token信息提取失败");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 检查路径是否在白名单中
     */
    private boolean isWhitelistPath(String requestURI) {
        for (String path : WHITELIST_PATHS) {
            if (path.endsWith("/**")) {
                // 处理通配符路径
                String prefix = path.substring(0, path.length() - 3);
                if (requestURI.startsWith(prefix)) {
                    return true;
                }
            } else if (requestURI.equals(path) || requestURI.startsWith(path + "/")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 发送401未授权错误响应
     */
    private void sendUnauthorizedError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        CommonResult<?> errorResult = CommonResult.error(401, message);
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(errorResult));
    }
}