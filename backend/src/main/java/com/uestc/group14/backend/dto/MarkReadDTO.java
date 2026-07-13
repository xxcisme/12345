package com.uestc.group14.backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class MarkReadDTO {
    private List<Long> messageIds; // 为空则标记全部已读
}