package com.uestc.group14.backend.dto.admin;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OperationLogCleanDTO {
    @NotNull(message = "保留天数不能为空")
    private Integer daysToKeep;  // 保留最近N天，之前的删除
}