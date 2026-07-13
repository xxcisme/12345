package com.uestc.group14.backend.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("res_media")
public class Media {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long resourceId;
    private String fileUrl;
}