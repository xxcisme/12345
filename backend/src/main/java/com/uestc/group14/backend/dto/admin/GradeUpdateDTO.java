package com.uestc.group14.backend.dto.admin;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class GradeUpdateDTO {
    @NotNull(message = "ID不能为空")
    private Long id;

    @NotNull(message = "成绩不能为空")
    @DecimalMin(value = "0", message = "成绩不得小于0")
    @DecimalMax(value = "100", message = "成绩不得大于100")
    private BigDecimal score;

    private String comment;

    private Boolean publish = false;
}