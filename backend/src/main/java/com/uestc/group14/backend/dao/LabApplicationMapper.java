package com.uestc.group14.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.uestc.group14.backend.Entity.LabApplication;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LabApplicationMapper extends BaseMapper<LabApplication> {
}