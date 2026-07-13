package com.uestc.group14.backend.Entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("tc_teaching_plan")
public class TeachingPlan {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;
    private String semester;
    private Long classId;
    private Long createId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer status;    // 0-草稿 1-已发布

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}