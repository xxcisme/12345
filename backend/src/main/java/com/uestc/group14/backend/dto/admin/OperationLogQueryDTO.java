package com.uestc.group14.backend.dto.admin;

import lombok.Data;

@Data
public class OperationLogQueryDTO {
    private Integer pageNo = 1;
    private Integer pageSize = 10;
    private String username;      // 模糊搜索
    private String userRole;      // 账号类型
    private String action;        // 模糊搜索操作内容
    private String startTime;     // 开始时间 yyyy-MM-dd HH:mm:ss
    private String endTime;       // 结束时间 yyyy-MM-dd HH:mm:ss
}