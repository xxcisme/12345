package com.uestc.group14.backend.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class TeachingPlanAddDTO {
    @NotBlank(message = "计划名称不能为空")
    private String name;

    private String semester;

    @NotNull(message = "班级ID不能为空")
    private Long classId;

    @NotNull(message = "课程ID不能为空")
    private Long courseId;

    @NotNull(message = "教师ID不能为空")
    private Long teacherId;

    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull(message = "实验列表不能为空")
    private List<Long> experimentIds;

    @NotNull(message = "上课日期列表不能为空")
    private List<String> scheduleDates; // 格式 yyyy-MM-dd
}