package com.uestc.group14.backend.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CourseVO {
    private Long id;
    private String courseCode;
    private String courseName;
    private String courseType;
    private Integer classHours;
    private Double credit;
    private Long teacherId;
    private String teacherName;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // ========== 新增字段（课程详情专用） ==========
    private String introduction;   // 课程介绍
    private String outline;        // 课程大纲
}