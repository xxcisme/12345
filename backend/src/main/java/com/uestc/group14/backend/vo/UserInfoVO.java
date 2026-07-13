package com.uestc.group14.backend.vo;

import lombok.Data;

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
    private String className;
    private String occupationType;
}