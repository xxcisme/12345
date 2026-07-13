package com.uestc.group14.backend.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("tc_plan_detail")
public class PlanDetail {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long planId;
    private Long courseId;
    private Long experimentId;
    private LocalDate scheduleDate;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}