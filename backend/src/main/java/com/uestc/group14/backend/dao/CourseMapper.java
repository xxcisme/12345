package com.uestc.group14.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.uestc.group14.backend.Entity.CourseEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 课程表 Mapper 接口
 */
@Mapper
public interface CourseMapper extends BaseMapper<CourseEntity> {
}