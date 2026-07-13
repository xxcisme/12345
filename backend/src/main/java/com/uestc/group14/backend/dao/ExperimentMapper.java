package com.uestc.group14.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.uestc.group14.backend.Entity.Experiment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 实验表 Mapper 接口
 */
@Mapper
public interface ExperimentMapper extends BaseMapper<Experiment> {
}