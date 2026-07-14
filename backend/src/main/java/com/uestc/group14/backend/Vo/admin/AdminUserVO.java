package com.uestc.group14.backend.vo.admin;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AdminUserVO {
    private Long id;
    private String username;
    private Integer role;
    private String roleName;       // 学生/老师/社会人士/管理员
    private String phone;
    private String email;
    private Integer status;
    private String statusName;     // 启用/停用
    private String realName;       // 真实姓名（来自UserProfile）
    private String schoolCode;
    private String className;
    private String occupationType;
    private LocalDateTime lastLoginTime;
    private String lastLoginIp;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}