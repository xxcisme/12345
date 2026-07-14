package com.uestc.group14.backend.vo.admin;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class GradeStatisticsVO {
    private BigDecimal avgScore;
    private BigDecimal maxScore;
    private BigDecimal minScore;
    private Double passRate; // 百分比
    private Long totalCount;
    private Long passCount;

    public static GradeStatisticsVO empty() {
        GradeStatisticsVO vo = new GradeStatisticsVO();
        vo.setAvgScore(BigDecimal.ZERO);
        vo.setMaxScore(BigDecimal.ZERO);
        vo.setMinScore(BigDecimal.ZERO);
        vo.setPassRate(0.0);
        vo.setTotalCount(0L);
        vo.setPassCount(0L);
        return vo;
    }
}