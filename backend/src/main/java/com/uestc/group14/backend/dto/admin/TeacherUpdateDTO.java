package com.uestc.group14.backend.dto.admin;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class TeacherUpdateDTO {
    @NotNull(message = "ID不能为空")
    private Long id;

    private String name;

    private String type;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    private String email;

    private String company;

    private Boolean onJob;
}