package com.uestc.group14.backend.Entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("res_laboratory")
public class Laboratory {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String number;
    private String name;
    private Integer stationNum;
    private BigDecimal area;
    private String address;
    private String profile;
    private String description;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer delFlag;
}