package com.uestc.group14.backend.dto.admin;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CourseUpdateDTO {
    @NotNull(message = "ID不能为空")
    private Long id;

    private String courseName;

    private String courseType;

    private Integer classHours;

    private Double credit;

    private Long teacherId;

    private String introduction;

    private String outline;
}