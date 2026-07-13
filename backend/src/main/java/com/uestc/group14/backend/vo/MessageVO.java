package com.uestc.group14.backend.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MessageVO {
    private Long id;
    private String title;
    private String content;
    private Integer type;
    private String typeName;
    private Integer isRead;
    private LocalDateTime createTime;
}