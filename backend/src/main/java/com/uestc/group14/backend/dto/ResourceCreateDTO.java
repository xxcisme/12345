package com.uestc.group14.backend.dto;

import lombok.Data;

@Data
public class ResourceCreateDTO {
    private String name;
    private Integer type;
    private String category;
    private String school;
    private String leader;
    private Boolean isShared;
    private String profile;
    // 文件单独接收，不在DTO中
}