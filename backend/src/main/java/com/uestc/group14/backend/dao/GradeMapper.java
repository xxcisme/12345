package com.uestc.group14.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.uestc.group14.backend.Entity.Grade;
import org.apache.ibatis.annotations.Mapper;

/**
 * 成绩评定表 Mapper 接口
 */
@Mapper
public interface GradeMapper extends BaseMapper<Grade> {
}