package com.uestc.group14.backend.vo;

import lombok.Data;

@Data
public class CourseVO {
    private Long id;
    private String courseName;
    private String teacherName;
    private Integer status; // 0未开始 1进行中 2已完成
}