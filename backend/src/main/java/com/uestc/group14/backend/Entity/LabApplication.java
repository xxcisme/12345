package com.uestc.group14.backend.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("res_lab_application")
public class LabApplication {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String number;
    private Long labId;
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