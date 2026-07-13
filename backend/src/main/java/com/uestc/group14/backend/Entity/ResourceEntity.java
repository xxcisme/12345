package com.uestc.group14.backend.Entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("res_resource")
public class ResourceEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String number;
    private String name;
    private Integer type;        // 1视频 2音频 3文档
    private String thumbnail;
    private String school;
    private String leader;
    private String category;
    private Integer status;      // 0待审核 1已驳回 2已发布
    private String auditRemark;
    private Integer isShared;
    private String profile;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableLogic
    private Integer delFlag;
}