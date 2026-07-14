package com.uestc.group14.backend.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class LabUpdateDTO {
    @NotNull(message = "实验室ID不能为空")
    private Long id;
    private String name;
    private Integer stationNum;
    private BigDecimal area;
    private String address;
    private String profile;
    private String description;
}