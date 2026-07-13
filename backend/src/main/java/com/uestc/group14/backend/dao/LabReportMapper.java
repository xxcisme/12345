package com.uestc.group14.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.uestc.group14.backend.Entity.LabReport;
import org.apache.ibatis.annotations.Mapper;

/**
 * 实验报告表 Mapper 接口
 */
@Mapper
public interface LabReportMapper extends BaseMapper<LabReport> {
}