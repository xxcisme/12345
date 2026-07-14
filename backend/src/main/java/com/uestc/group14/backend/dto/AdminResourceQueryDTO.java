package com.uestc.group14.backend.dto;

import lombok.Data;

@Data
public class AdminResourceQueryDTO {
    private Integer pageNo = 1;
    private Integer pageSize = 10;
    private String name;
    private Integer type;
    private Integer status;     // 0待审核 1已驳回 2已发布
    private String category;
}