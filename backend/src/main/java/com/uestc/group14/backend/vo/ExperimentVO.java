package com.uestc.group14.backend.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ExperimentVO {
    private Long id;
    private String name;
    private String courseName;
    private LocalDateTime submittedAt;
    private BigDecimal score;
    private Integer evaluationStatus; // 0待评定 1已通过 2未通过
}