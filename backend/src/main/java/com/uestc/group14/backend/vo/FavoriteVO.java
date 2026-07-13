package com.uestc.group14.backend.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FavoriteVO {
    private Long id;
    private Long resourceId;
    private String resourceName;
    private Integer resourceType;
    private String resourceTypeName;
    private String thumbnail;
    private LocalDateTime createTime;
}