package com.uestc.group14.backend.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class DeviceCreateDTO {
    @NotBlank(message = "设备编号不能为空")
    private String number;
    @NotBlank(message = "设备名称不能为空")
    private String name;
    private String type;
    private String versionNumber;
    @NotNull(message = "所属实验室不能为空")
    private Long laboratoryId;
}