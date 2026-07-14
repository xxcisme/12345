package com.uestc.group14.backend.dto.admin;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserResetPwdDTO {
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    private String newPassword = "123456";  // 默认重置为123456
}