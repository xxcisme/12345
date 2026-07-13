package com.uestc.group14.backend.dto;

import com.uestc.group14.backend.Entity.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Data
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class LoginRequest extends UserEntity {

    /** 页码（查询用，登录时不需要） */
    private Integer pageNo;

    /** 页大小（查询用，登录时不需要） */
    private Integer pageSize;
}