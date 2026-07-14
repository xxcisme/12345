package com.uestc.group14.backend.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ExperimentAddDTO {
    @NotBlank(message = "实验编号不能为空")
    private String number;

    @NotBlank(message = "实验名称不能为空")
    private String name;

    @NotNull(message = "课程ID不能为空")
    private Long courseId;

    private String objective;

    private String steps;

    private String reportTemplate;
}