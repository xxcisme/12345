package com.uestc.group14.backend.dto;

import lombok.Data;

@Data
public class AdminLabApplicationQueryDTO {
    private Integer pageNo = 1;
    private Integer pageSize = 10;
    private Integer status;
    private Long labId;
}