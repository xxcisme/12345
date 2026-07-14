package com.uestc.group14.backend.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
public class LabCreateDTO {
    @NotBlank(message = "实验室编号不能为空")
    private String number;
    @NotBlank(message = "实验室名称不能为空")
    private String name;
    @NotNull(message = "工位数量不能为空")
    @Positive(message = "工位数量必须为正整数")
    private Integer stationNum;
    private BigDecimal area;
    private String address;
    private String profile;
    private String description;
}