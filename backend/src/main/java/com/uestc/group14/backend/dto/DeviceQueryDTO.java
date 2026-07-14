package com.uestc.group14.backend.dto;

import lombok.Data;

@Data
public class DeviceQueryDTO {
    private Integer pageNo = 1;
    private Integer pageSize = 10;
    private String name;
    private String number;
    private Long laboratoryId;
    private String type;
    private Integer status; // 0空闲 1使用中 2保修 3损坏
}