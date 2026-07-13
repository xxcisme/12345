package com.uestc.group14.backend.Entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sm_user_profile")
public class UserProfile {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 真实姓名 */
    private String realName;

    /** 学校编号 */
    private String schoolCode;

    /** 班级名称 */
    private String className;

    /** 班级ID */
    private Long classId;

    /** 职业类型 */
    private String occupationType;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}