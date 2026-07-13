package com.uestc.group14.backend.Entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("tc_experiment")
public class Experiment {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String number;
    private String name;
    private Long courseId;
    private String objective;
    private String steps;
    private String reportTemplate;
    private Integer status;      // 0草稿 1已发布
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}