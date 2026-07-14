package com.uestc.group14.backend.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class DeviceUpdateDTO {
    @NotNull(message = "设备ID不能为空")
    private Long id;
    private String name;
    private String type;
    private String versionNumber;
    private Long laboratoryId;
    private Integer status;
}