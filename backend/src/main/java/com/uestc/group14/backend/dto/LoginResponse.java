package com.uestc.group14.backend.dto;

import lombok.*;

/**
 * 登录响应DTO
 */
@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    /**
     * JWT令牌
     */
    private String token;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户角色：1-学生，2-老师，3-社会人士，4-管理员
     */
    private Integer role;
}