package com.uestc.group14.backend.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CourseAddDTO {
    @NotBlank(message = "课程编号不能为空")
    private String courseCode;

    @NotBlank(message = "课程名称不能为空")
    private String courseName;

    private String courseType;

    private Integer classHours;

    private Double credit;

    @NotNull(message = "教师ID不能为空")
    private Long teacherId;

    private String introduction;

    private String outline;
}