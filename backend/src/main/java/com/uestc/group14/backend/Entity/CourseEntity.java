package com.uestc.group14.backend.Entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("tc_course")
public class CourseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String courseCode;
    private String courseName;
    private String courseType;
    private Integer classHours;
    private Double credit;
    private Long teacherId;
    private String introduction;
    private String outline;
    private Integer status;      // 0草稿 1已发布 2已下架
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}