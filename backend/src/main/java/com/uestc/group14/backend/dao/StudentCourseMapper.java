package com.uestc.group14.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.uestc.group14.backend.Entity.StudentCourse;
import org.apache.ibatis.annotations.Mapper;

/**
 * 学生选课表 Mapper 接口
 */
@Mapper
public interface StudentCourseMapper extends BaseMapper<StudentCourse> {
}