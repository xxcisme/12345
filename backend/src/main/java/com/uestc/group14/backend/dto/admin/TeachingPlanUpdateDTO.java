package com.uestc.group14.backend.dto.admin;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class TeachingPlanUpdateDTO {
    @NotNull(message = "ID不能为空")
    private Long id;

    private String name;

    private String semester;

    private LocalDate startDate;

    private LocalDate endDate;

    private List<Long> experimentIds;

    private List<String> scheduleDates; // 格式 yyyy-MM-dd
}