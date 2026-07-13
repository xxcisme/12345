package com.uestc.group14.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FavoriteAddDTO {
    @NotNull(message = "资源ID不能为空")
    private Long resourceId;
}