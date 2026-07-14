package com.uestc.group14.backend.dto.admin;

import lombok.Data;

@Data
public class UserQueryDTO {
    private Integer pageNo = 1;
    private Integer pageSize = 10;
    private String username;      // 模糊搜索
    private String phone;         // 模糊搜索
    private Integer role;         // 1学生 2老师 3社会人士 4管理员
    private Integer status;       // 0停用 1启用
}