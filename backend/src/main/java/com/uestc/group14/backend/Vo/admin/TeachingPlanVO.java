package com.uestc.group14.backend.vo.admin;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TeachingPlanVO {
    private Long id;
    private String name;
    private String semester;
    private Long classId;
    private String className;
    private Long teacherId;           // ✅ 原 createId → teacherId
    private String teacherName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer status;
    private String statusName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<Long> experiments;   // ✅ 原 experimentIds → experiments
}