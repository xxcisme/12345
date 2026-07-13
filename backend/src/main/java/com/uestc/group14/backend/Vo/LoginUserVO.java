package com.uestc.group14.backend.Vo;


import lombok.Data;
import java.time.LocalDateTime;

/**
 * 登录用户信息VO
 */
@Data
public class LoginUserVO {

    private Long id;
    private String username;
    private Integer role;
    private String roleName;
    private String realName;
    private String phone;
    private String email;
    private String token;
    private Long expireTime;
    private LocalDateTime lastLoginTime;
}