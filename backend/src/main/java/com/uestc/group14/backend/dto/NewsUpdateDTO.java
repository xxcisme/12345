package com.uestc.group14.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NewsUpdateDTO {
    @NotNull(message = "新闻ID不能为空")
    private Long id;

    private String title;
    private String origin;
    private String content;
    private String enclosure;
}