package com.uestc.group14.backend.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NewsVO {
    private Long id;
    private String title;
    private String origin;
    private String enclosure;      // 图片地址
    private String editor;
    private LocalDateTime publishTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // 详情时返回content，列表时可为null
    private String content;
}