package com.uestc.group14.backend.vo.admin;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TeacherVO {
    private Long id;
    private String teacherId;
    private String name;
    private String type;
    private String typeName;
    private String phone;
    private String email;
    private String company;
    private Boolean onJob;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<CourseInfo> courses; // 仅详情接口使用

    @Data
    public static class CourseInfo {
        private Long id;
        private String courseName;
        private String courseCode;
    }
}