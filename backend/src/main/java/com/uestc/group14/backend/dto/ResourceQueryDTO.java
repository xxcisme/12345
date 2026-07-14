package com.uestc.group14.backend.dto;

import lombok.Data;

@Data
public class ResourceQueryDTO {
    private Integer pageNo = 1;
    private Integer pageSize = 10;
    private String name;
    private Integer type;       // 1视频 2音频 3文档
    private String category;
    private Boolean isShared;
    private String sortBy;      // createTime / score
    private String order;       // asc / desc
}