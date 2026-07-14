package com.uestc.group14.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuditDTO {
    @NotNull(message = "审核状态不能为空")
    private Integer status;   // 1-驳回 2-通过
    private String auditRemark;
}