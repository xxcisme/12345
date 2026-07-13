package com.uestc.group14.backend.Entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("tc_lab_report")
public class LabReport {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long scheduleId;
    private Long studentId;
    private String reportContent;
    private String attachment;
    private BigDecimal score;
    private Integer evaluationStatus; // 0待评定 1已评定
    private LocalDateTime submittedAt;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}