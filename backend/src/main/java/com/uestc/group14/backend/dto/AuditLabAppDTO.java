package com.uestc.group14.backend.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class AuditLabAppDTO {
    @NotNull(message = "审批状态不能为空")
    private Integer status; // 1通过 2拒绝
    private String auditRemark;
}