package com.uestc.group14.backend.dto;

import lombok.Data;

@Data
public class NewsQueryDTO {
    private Integer pageNo = 1;
    private Integer pageSize = 10;
    private String title;   // 模糊搜索
}