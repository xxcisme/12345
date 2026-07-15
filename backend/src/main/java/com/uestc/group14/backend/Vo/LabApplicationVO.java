package com.uestc.group14.backend.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LabApplicationVO {
    
    private Long id;
    private String number;
    private Long labId;
    private String labName;
    private String applicantName;
    private String contactPhone;
    private String purpose;
    private String name;
    private String objective;
    private String steps;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer status;
    private String auditRemark;
    private LocalDateTime approvalTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}