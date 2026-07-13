package com.uestc.group14.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.uestc.group14.backend.Entity.ResourceEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 资源主表 Mapper 接口
 */
@Mapper
public interface ResourceMapper extends BaseMapper<ResourceEntity> {
}