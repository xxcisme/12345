package com.uestc.group14.backend.dto;

import lombok.Data;

@Data
public class LabApplicationQueryDTO {
    private Integer pageNo = 1;
    private Integer pageSize = 10;
    private Integer status; // 0待审批 1已通过 2已拒绝
}