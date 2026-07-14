package com.uestc.group14.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.uestc.group14.backend.Entity.Rating;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RatingMapper extends BaseMapper<Rating> {
    // 新增：获取资源平均分
    @Select("SELECT AVG(score) FROM res_rating WHERE resource_id = #{resourceId}")
    Double selectAvgScore(@Param("resourceId") Long resourceId);
}