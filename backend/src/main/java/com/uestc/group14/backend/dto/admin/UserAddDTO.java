package com.uestc.group14.backend.dto.admin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserAddDTO {
    @NotBlank(message = "用户名不能为空")
    @Size(min = 4, max = 16, message = "用户名必须为4-16位字母或数字")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 8, message = "密码至少8位")
    private String password;

    @NotNull(message = "角色不能为空")
    private Integer role;  // 1学生 2老师 3社会人士 4管理员

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Email(message = "邮箱格式不正确")
    private String email;

    private String realName;
    private String schoolCode;

    // 新增字段：班级ID（学生必填，其他角色可选）
    private Long classId;

    private String occupationType;
}