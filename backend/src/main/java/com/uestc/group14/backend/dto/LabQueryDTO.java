package com.uestc.group14.backend.dto;

import lombok.Data;

@Data
public class LabQueryDTO {
    private Integer pageNo = 1;
    private Integer pageSize = 10;
    private String name;
    private String number;
    private String address;
    private Integer minStation;
}