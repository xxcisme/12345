package com.uestc.group14.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.uestc.group14.backend.Entity.LabReport;
import com.uestc.group14.backend.vo.CourseVO;
import com.uestc.group14.backend.vo.ExperimentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LabReportMapper extends BaseMapper<LabReport> {

    // 已有的实验报告联表查询方法
    IPage<ExperimentVO> selectExperimentVoPage(Page<ExperimentVO> page,
                                               @Param("studentId") Long studentId,
                                               @Param("status") Integer status,
                                               @Param("courseId") Long courseId);

    // 新增：查询学生参与的课程列表（去重）
    IPage<CourseVO> selectCourseVoPage(Page<CourseVO> page,
                                       @Param("studentId") Long studentId,
                                       @Param("status") Integer status);
}