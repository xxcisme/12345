package com.uestc.group14.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.uestc.group14.backend.Entity.Laboratory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LaboratoryMapper extends BaseMapper<Laboratory> {
}