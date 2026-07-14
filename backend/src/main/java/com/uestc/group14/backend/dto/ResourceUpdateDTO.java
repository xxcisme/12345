package com.uestc.group14.backend.dto;

import lombok.Data;

@Data
public class ResourceUpdateDTO {
    private Long id;
    private String name;
    private String category;
    private String school;
    private String leader;
    private Boolean isShared;
    private String profile;
}