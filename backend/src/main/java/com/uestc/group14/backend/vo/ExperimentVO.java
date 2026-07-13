package com.uestc.group14.backend.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ExperimentVO {
    private Long id;
    private String number;
    private String name;
    private Long courseId;
    private String courseName;
    private String objective;
    private String steps;
    private String reportTemplate;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // 以下字段来自 tc_lab_report（若有提交）
    private BigDecimal grade;
    private Integer evaluationStatus; // 0待评定 1已评定
    private LocalDateTime submittedAt;

    // ========== 新增字段（实验详情专用） ==========
    private String report;           // 学生已提交的报告内容（对应 LabReport.reportContent）
}