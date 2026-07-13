package com.uestc.group14.backend.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserInfoVO {
    private Long id;
    private String username;
    private Integer role;
    private String phone;
    private String email;
    private Integer status;
    private String realName;
    private String schoolCode;
    private String className;        // 新增
    private String occupationType;
    private Long classId;
    private LocalDateTime lastLoginTime;
    private String lastLoginIp;
}