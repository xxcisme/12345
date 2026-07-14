package com.uestc.group14.backend.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class TeacherAddDTO {
    @NotBlank(message = "教师ID不能为空")
    private String teacherId;

    @NotBlank(message = "姓名不能为空")
    private String name;

    private String type; // 0实训老师，1非实训老师

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    private String email;

    private String company;

    private Boolean onJob = true;
}