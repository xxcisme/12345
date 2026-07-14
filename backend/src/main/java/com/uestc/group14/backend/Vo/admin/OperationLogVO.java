package com.uestc.group14.backend.vo.admin;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OperationLogVO {
    private Long id;
    private Long userId;
    private String userRole;
    private String username;
    private String action;
    private String ipAddress;
    private LocalDateTime createTime;
}