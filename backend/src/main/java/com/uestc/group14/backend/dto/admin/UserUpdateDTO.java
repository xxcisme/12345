package com.uestc.group14.backend.dto.admin;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UserUpdateDTO {
    @NotNull(message = "用户ID不能为空")
    private Long id;


    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Email(message = "邮箱格式不正确")
    private String email;

    private String realName;
    private String schoolCode;
    private Long classId;
    private String occupationType;
}