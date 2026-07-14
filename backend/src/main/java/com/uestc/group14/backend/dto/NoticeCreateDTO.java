package com.uestc.group14.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NoticeCreateDTO {
    @NotBlank(message = "标题不能为空")
    private String title;

    @NotBlank(message = "内容不能为空")
    private String content;
}