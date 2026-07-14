package com.uestc.group14.backend.vo.admin;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class GradeVO {
    private Long id;
    private Long scheduleId;
    private Long studentId;
    private String studentName;
    private String studentNo;           // ✅ 新增（学号）
    private Long courseId;
    private String courseName;
    private Long experimentId;
    private String experimentName;
    private BigDecimal overallScore;
    private String comment;
    private Integer publishStatus;
    private String publishStatusName;
    private LocalDateTime gradedAt;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}