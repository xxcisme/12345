package com.uestc.group14.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.uestc.group14.backend.Entity.CourseEntity;
import com.uestc.group14.backend.Entity.Experiment;
import com.uestc.group14.backend.Entity.LabReport;
import com.uestc.group14.backend.common.exception.BusinessException;
import com.uestc.group14.backend.common.exception.ErrorCode;
import com.uestc.group14.backend.dao.CourseMapper;
import com.uestc.group14.backend.dao.ExperimentMapper;
import com.uestc.group14.backend.dao.LabReportMapper;
import com.uestc.group14.backend.dto.admin.ExperimentAddDTO;
import com.uestc.group14.backend.dto.admin.ExperimentUpdateDTO;
import com.uestc.group14.backend.service.ExperimentService;
import com.uestc.group14.backend.vo.ExperimentVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExperimentServiceImpl implements ExperimentService {

    private final ExperimentMapper experimentMapper;
    private final CourseMapper courseMapper;
    private final LabReportMapper labReportMapper;

    @Override
    public IPage<ExperimentVO> listExperiments(Integer pageNo, Integer pageSize, String name, String number,
                                               Long courseId, Integer status) {
        Page<Experiment> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<Experiment> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(name)) {
            wrapper.like(Experiment::getName, name);
        }
        if (StringUtils.hasText(number)) {
            wrapper.eq(Experiment::getNumber, number);
        }
        if (courseId != null) {
            wrapper.eq(Experiment::getCourseId, courseId);
        }
        if (status != null) {
            wrapper.eq(Experiment::getStatus, status);
        }
        wrapper.orderByDesc(Experiment::getCreateTime);
        IPage<Experiment> entityPage = experimentMapper.selectPage(page, wrapper);

        List<ExperimentVO> voList = entityPage.getRecords().stream().map(this::convertToVO).collect(Collectors.toList());
        Page<ExperimentVO> voPage = new Page<>(pageNo, pageSize, entityPage.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    @Transactional
    public Long addExperiment(ExperimentAddDTO dto) {
        // 校验编号唯一性
        LambdaQueryWrapper<Experiment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Experiment::getNumber, dto.getNumber());
        if (experimentMapper.selectCount(wrapper) > 0) {
            throw new BusinessException(400, "实验编号已存在");
        }
        // 校验课程存在
        if (dto.getCourseId() != null) {
            CourseEntity course = courseMapper.selectById(dto.getCourseId());
            if (course == null) {
                throw new BusinessException(ErrorCode.COURSE_NOT_FOUND);
            }
        }
        Experiment experiment = new Experiment();
        BeanUtils.copyProperties(dto, experiment);
        experiment.setStatus(0); // 草稿
        experiment.setCreateTime(LocalDateTime.now());
        experiment.setUpdateTime(LocalDateTime.now());
        experimentMapper.insert(experiment);
        return experiment.getId();
    }

    @Override
    @Transactional
    public ExperimentVO updateExperiment(ExperimentUpdateDTO dto) {
        Experiment experiment = experimentMapper.selectById(dto.getId());
        if (experiment == null) {
            throw new BusinessException(ErrorCode.EXPERIMENT_NOT_FOUND);
        }
        if (StringUtils.hasText(dto.getName())) {
            experiment.setName(dto.getName());
        }
        if (StringUtils.hasText(dto.getObjective())) {
            experiment.setObjective(dto.getObjective());
        }
        if (StringUtils.hasText(dto.getSteps())) {
            experiment.setSteps(dto.getSteps());
        }
        if (StringUtils.hasText(dto.getReportTemplate())) {
            experiment.setReportTemplate(dto.getReportTemplate());
        }
        // 修改课程
        if (dto.getCourseId() != null && !dto.getCourseId().equals(experiment.getCourseId())) {
            CourseEntity course = courseMapper.selectById(dto.getCourseId());
            if (course == null) {
                throw new BusinessException(ErrorCode.COURSE_NOT_FOUND);
            }
            experiment.setCourseId(dto.getCourseId());
        }
        // 如果已发布，改为草稿
        if (experiment.getStatus() == 1) {
            experiment.setStatus(0);
        }
        experiment.setUpdateTime(LocalDateTime.now());
        experimentMapper.updateById(experiment);
        return convertToVO(experiment);
    }

    @Override
    @Transactional
    public ExperimentVO publishExperiment(Long id) {
        Experiment experiment = experimentMapper.selectById(id);
        if (experiment == null) {
            throw new BusinessException(ErrorCode.EXPERIMENT_NOT_FOUND);
        }
        if (experiment.getStatus() == 1) {
            throw new BusinessException(400, "实验已是发布状态");
        }
        experiment.setStatus(1);
        experiment.setUpdateTime(LocalDateTime.now());
        experimentMapper.updateById(experiment);
        return convertToVO(experiment);
    }

    @Override
    @Transactional
    public void deleteExperiment(Long id) {
        Experiment experiment = experimentMapper.selectById(id);
        if (experiment == null) {
            throw new BusinessException(ErrorCode.EXPERIMENT_NOT_FOUND);
        }
        // 检查是否有学生提交报告
        LambdaQueryWrapper<LabReport> reportWrapper = new LambdaQueryWrapper<>();
        reportWrapper.inSql(LabReport::getScheduleId,
                "SELECT id FROM tc_plan_detail WHERE experiment_id = " + id);
        Long count = labReportMapper.selectCount(reportWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.EXPERIMENT_HAS_REPORT);
        }
        experimentMapper.deleteById(id);
    }

    private ExperimentVO convertToVO(Experiment entity) {
        ExperimentVO vo = new ExperimentVO();
        BeanUtils.copyProperties(entity, vo);
        if (entity.getCourseId() != null) {
            CourseEntity course = courseMapper.selectById(entity.getCourseId());
            if (course != null) {
                vo.setCourseName(course.getCourseName());
            }
        }

        return vo;
    }


}