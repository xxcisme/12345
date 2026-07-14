package com.uestc.group14.backend.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class LabApplicationCreateDTO {
    @NotNull(message = "实验室ID不能为空")
    private Long labId;
    @NotBlank(message = "申请人姓名不能为空")
    private String applicantName;
    @NotBlank(message = "联系电话不能为空")
    private String contactPhone;
    private String purpose;
    @NotBlank(message = "实验名称不能为空")
    private String name;
    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startTime;
    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;
}