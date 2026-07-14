package com.uestc.group14.backend.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdminResourceVO extends ResourceVO {
    private Long uploaderId;
    private String uploaderName;
}