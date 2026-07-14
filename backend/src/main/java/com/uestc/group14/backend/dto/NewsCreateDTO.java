package com.uestc.group14.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NewsCreateDTO {
    @NotBlank(message = "标题不能为空")
    private String title;

    private String origin;

    @NotBlank(message = "内容不能为空")
    private String content;

    private String enclosure;   // 图片地址
}