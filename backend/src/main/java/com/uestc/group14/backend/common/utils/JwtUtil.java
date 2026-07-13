package com.uestc.group14.backend.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT工具类（合并版本）
 * 兼容两种生成方式：①仅userId（subject存userId）②userId+username+role（subject存username，claims存userId&role）
 */
@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    // 缓存密钥，避免重复生成
    private SecretKey secretKey;

    /**
     * 获取SecretKey（懒加载）
     */
    private SecretKey getSecretKey() {
        if (secretKey == null) {
            secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        }
        return secretKey;
    }

    // ==================== 生成Token ====================

    /**
     * 生成简单Token（仅存储userId），兼容旧版调用
     * subject = userId.toString()
     * claim "userId" = userId（便于统一解析）
     */
    public String generateToken(Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        return Jwts.builder()
                .setSubject(userId.toString())           // 兼容旧版getUserIdFromToken从subject解析
                .claim("userId", userId)                 // 同时存入claim，新版解析优先使用
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 生成完整Token（存储userId, username, role）
     * subject = username
     * claim "userId" = userId
     * claim "role" = role
     */
    public String generateToken(Long userId, String username, Integer role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ==================== 解析Token ====================

    /**
     * 从Token中获取用户名（适用于完整版Token）
     */
    public String getUsernameFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims.getSubject();
    }

    /**
     * 从Token中获取用户ID（兼容两种Token）
     * 优先从claim中获取，若不存在则从subject解析（兼容旧版简单Token）
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = parseClaims(token);
        Long userId = claims.get("userId", Long.class);
        if (userId == null) {
            // 如果claim中没有，则从subject解析（兼容旧版）
            String subject = claims.getSubject();
            if (subject != null) {
                try {
                    return Long.parseLong(subject);
                } catch (NumberFormatException e) {
                    log.warn("无法从subject解析userId: {}", subject);
                }
            }
        }
        return userId;
    }

    /**
     * 从Token中获取用户角色（适用于完整版Token）
     */
    public Integer getRoleFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims.get("role", Integer.class);
    }

    /**
     * 验证Token是否有效
     */
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Token验证失败: {}", e.getMessage());
            return false;
        }
    }

    // ==================== 私有辅助方法 ====================

    /**
     * 解析Token并获取Claims
     */
    private Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}