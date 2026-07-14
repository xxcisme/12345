package com.uestc.group14.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.uestc.group14.backend.dto.admin.CourseAddDTO;
import com.uestc.group14.backend.dto.admin.CourseUpdateDTO;
import com.uestc.group14.backend.vo.CourseVO;

public interface CourseService {
    IPage<CourseVO> listCourses(Integer pageNo, Integer pageSize, String courseName, String courseCode,
                                String courseType, Integer status, Long teacherId);
    Long addCourse(CourseAddDTO dto);
    CourseVO updateCourse(CourseUpdateDTO dto);
    CourseVO publishCourse(Long id);
    CourseVO unpublishCourse(Long id);
    void deleteCourse(Long id);
}