package com.uestc.group14.backend.dto.admin;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ExperimentUpdateDTO {
    @NotNull(message = "ID不能为空")
    private Long id;

    private String name;

    private String objective;

    private String steps;

    private String reportTemplate;

    private Long courseId;
}