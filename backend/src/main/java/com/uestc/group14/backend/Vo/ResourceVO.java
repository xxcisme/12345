package com.uestc.group14.backend.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ResourceVO {
    private Long id;
    private String number;
    private String name;
    private Integer type;
    private String typeName;          // 视频/音频/文档
    private String thumbnail;
    private String school;
    private String leader;
    private String category;
    private Boolean isShared;         // 是否共享（前端使用布尔值）
    private Double avgScore;          // 平均分
    private Integer ratingCount;      // 评分人数
    private String fileUrl;           // 资源文件地址（来自 res_media）
    private String profile;
    private String auditRemark;
    private Integer status;
    private String statusName;        // 待审核/已驳回/已发布
    private LocalDateTime createTime;
}