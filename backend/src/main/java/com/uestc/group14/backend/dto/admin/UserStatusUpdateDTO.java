package com.uestc.group14.backend.dto.admin;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserStatusUpdateDTO {
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotNull(message = "状态不能为空")
    private Integer status;   // 0停用 1启用
}