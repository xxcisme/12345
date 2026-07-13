package com.uestc.group14.backend.Entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sm_user_favorite")
public class UserFavoriteEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long resourceId;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}