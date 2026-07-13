package com.uestc.group14.backend.common.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

/**
 * 请求工具类
 */
public class RequestUtil {

    private static final String UNKNOWN = "unknown";

    /**
     * 获取客户端IP地址
     *
     * @param request HttpServletRequest
     * @return 客户端IP地址
     */
    public static String getClientIp(HttpServletRequest request) {
        // 优先取 X-Forwarded-For
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(xForwardedFor) && !UNKNOWN.equalsIgnoreCase(xForwardedFor)) {
            // 多个IP时，第一个是客户端真实IP
            return xForwardedFor.split(",")[0].trim();
        }

        // 其次取 X-Real-IP
        String xRealIp = request.getHeader("X-Real-IP");
        if (StringUtils.hasText(xRealIp) && !UNKNOWN.equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }

        // 再次取 Proxy-Client-IP
        String proxyClientIp = request.getHeader("Proxy-Client-IP");
        if (StringUtils.hasText(proxyClientIp) && !UNKNOWN.equalsIgnoreCase(proxyClientIp)) {
            return proxyClientIp;
        }

        // 再次取 WL-Proxy-Client-IP
        String wlProxyClientIp = request.getHeader("WL-Proxy-Client-IP");
        if (StringUtils.hasText(wlProxyClientIp) && !UNKNOWN.equalsIgnoreCase(wlProxyClientIp)) {
            return wlProxyClientIp;
        }

        // 最后取 RemoteAddr
        String remoteAddr = request.getRemoteAddr();
        if (StringUtils.hasText(remoteAddr) && !UNKNOWN.equalsIgnoreCase(remoteAddr)) {
            return remoteAddr;
        }

        return "127.0.0.1";
    }
}