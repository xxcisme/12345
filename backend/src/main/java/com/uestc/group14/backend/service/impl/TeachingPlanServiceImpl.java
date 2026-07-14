package com.uestc.group14.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.uestc.group14.backend.Entity.*;
import com.uestc.group14.backend.common.exception.BusinessException;
import com.uestc.group14.backend.common.exception.ErrorCode;
import com.uestc.group14.backend.dao.*;
import com.uestc.group14.backend.dto.admin.TeachingPlanAddDTO;
import com.uestc.group14.backend.dto.admin.TeachingPlanUpdateDTO;
import com.uestc.group14.backend.service.TeachingPlanService;
import com.uestc.group14.backend.vo.admin.TeachingPlanVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeachingPlanServiceImpl implements TeachingPlanService {

    private final TeachingPlanMapper planMapper;
    private final PlanDetailMapper detailMapper;
    private final ClassMapper classMapper;
    private final TeacherMapper teacherMapper;
    private final CourseMapper courseMapper;
    private final ExperimentMapper experimentMapper;
    private final UserMapper userMapper;

    @Override
    public IPage<TeachingPlanVO> listPlans(Integer pageNo, Integer pageSize, String name, String semester,
                                           Integer status, Long teacherId) {
        Page<TeachingPlan> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<TeachingPlan> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(name)) {
            wrapper.like(TeachingPlan::getName, name);
        }
        if (StringUtils.hasText(semester)) {
            wrapper.eq(TeachingPlan::getSemester, semester);
        }
        if (status != null) {
            wrapper.eq(TeachingPlan::getStatus, status);
        }
        // 查询参数 teacherId 对应实体字段 createId（教师ID）
        if (teacherId != null) {
            wrapper.eq(TeachingPlan::getCreateId, teacherId);
        }
        wrapper.orderByDesc(TeachingPlan::getCreateTime);
        IPage<TeachingPlan> entityPage = planMapper.selectPage(page, wrapper);

        List<TeachingPlanVO> voList = entityPage.getRecords().stream().map(this::convertToVO).collect(Collectors.toList());
        Page<TeachingPlanVO> voPage = new Page<>(pageNo, pageSize, entityPage.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    @Transactional
    public Long addPlan(TeachingPlanAddDTO dto) {
        ClassEntity clazz = classMapper.selectById(dto.getClassId());
        if (clazz == null) {
            throw new BusinessException(400, "班级不存在");
        }
        TeacherEntity teacher = teacherMapper.selectById(dto.getTeacherId());
        if (teacher == null) {
            throw new BusinessException(ErrorCode.TEACHER_NOT_FOUND);
        }
        CourseEntity course = courseMapper.selectById(dto.getCourseId());
        if (course == null) {
            throw new BusinessException(ErrorCode.COURSE_NOT_FOUND);
        }

        if (dto.getExperimentIds() == null || dto.getScheduleDates() == null ||
                dto.getExperimentIds().size() != dto.getScheduleDates().size()) {
            throw new BusinessException(400, "实验列表和日期列表必须一一对应");
        }

        for (Long expId : dto.getExperimentIds()) {
            Experiment exp = experimentMapper.selectById(expId);
            if (exp == null) {
                throw new BusinessException(ErrorCode.EXPERIMENT_NOT_FOUND);
            }
            if (!exp.getCourseId().equals(dto.getCourseId())) {
                throw new BusinessException(400, "实验 " + expId + " 不属于该课程");
            }
        }

        TeachingPlan plan = new TeachingPlan();
        BeanUtils.copyProperties(dto, plan);
        plan.setStatus(0);
        plan.setCreateTime(LocalDateTime.now());
        plan.setUpdateTime(LocalDateTime.now());
        planMapper.insert(plan);

        List<PlanDetail> details = new ArrayList<>();
        for (int i = 0; i < dto.getExperimentIds().size(); i++) {
            PlanDetail detail = new PlanDetail();
            detail.setPlanId(plan.getId());
            detail.setCourseId(dto.getCourseId());
            detail.setExperimentId(dto.getExperimentIds().get(i));
            detail.setScheduleDate(LocalDate.parse(dto.getScheduleDates().get(i)));
            detail.setCreateTime(LocalDateTime.now());
            detail.setUpdateTime(LocalDateTime.now());
            details.add(detail);
        }

        Map<LocalDate, Long> dateCount = details.stream()
                .collect(Collectors.groupingBy(PlanDetail::getScheduleDate, Collectors.counting()));
        dateCount.forEach((date, count) -> {
            if (count > 1) {
                throw new BusinessException(400, "同一天不能安排多个实验，日期 " + date + " 冲突");
            }
        });

        for (PlanDetail detail : details) {
            detailMapper.insert(detail);
        }

        return plan.getId();
    }

    @Override
    @Transactional
    public TeachingPlanVO updatePlan(TeachingPlanUpdateDTO dto) {
        TeachingPlan plan = planMapper.selectById(dto.getId());
        if (plan == null) {
            throw new BusinessException(ErrorCode.PLAN_NOT_FOUND);
        }
        if (plan.getStatus() == 1) {
            throw new BusinessException(ErrorCode.PLAN_NOT_DRAFT);
        }

        if (StringUtils.hasText(dto.getName())) {
            plan.setName(dto.getName());
        }
        if (StringUtils.hasText(dto.getSemester())) {
            plan.setSemester(dto.getSemester());
        }
        if (dto.getStartDate() != null) {
            plan.setStartDate(dto.getStartDate());
        }
        if (dto.getEndDate() != null) {
            plan.setEndDate(dto.getEndDate());
        }
        plan.setUpdateTime(LocalDateTime.now());
        planMapper.updateById(plan);

        if (dto.getExperimentIds() != null && dto.getScheduleDates() != null) {
            if (dto.getExperimentIds().size() != dto.getScheduleDates().size()) {
                throw new BusinessException(400, "实验列表和日期列表必须一一对应");
            }

            Long existingCourseId = null;
            LambdaQueryWrapper<PlanDetail> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(PlanDetail::getPlanId, plan.getId())
                    .last("LIMIT 1");
            PlanDetail oldDetail = detailMapper.selectOne(queryWrapper);
            if (oldDetail != null) {
                existingCourseId = oldDetail.getCourseId();
            } else {
                Long firstExpId = dto.getExperimentIds().get(0);
                Experiment exp = experimentMapper.selectById(firstExpId);
                if (exp == null) {
                    throw new BusinessException(ErrorCode.EXPERIMENT_NOT_FOUND);
                }
                existingCourseId = exp.getCourseId();
            }

            for (Long expId : dto.getExperimentIds()) {
                Experiment exp = experimentMapper.selectById(expId);
                if (exp == null) {
                    throw new BusinessException(ErrorCode.EXPERIMENT_NOT_FOUND);
                }
                if (!exp.getCourseId().equals(existingCourseId)) {
                    throw new BusinessException(400, "实验 " + expId + " 不属于同一课程，无法更新");
                }
            }

            LambdaQueryWrapper<PlanDetail> deleteWrapper = new LambdaQueryWrapper<>();
            deleteWrapper.eq(PlanDetail::getPlanId, plan.getId());
            detailMapper.delete(deleteWrapper);

            List<PlanDetail> details = new ArrayList<>();
            for (int i = 0; i < dto.getExperimentIds().size(); i++) {
                PlanDetail detail = new PlanDetail();
                detail.setPlanId(plan.getId());
                detail.setCourseId(existingCourseId);
                detail.setExperimentId(dto.getExperimentIds().get(i));
                detail.setScheduleDate(LocalDate.parse(dto.getScheduleDates().get(i)));
                detail.setCreateTime(LocalDateTime.now());
                detail.setUpdateTime(LocalDateTime.now());
                details.add(detail);
            }

            Map<LocalDate, Long> dateCount = details.stream()
                    .collect(Collectors.groupingBy(PlanDetail::getScheduleDate, Collectors.counting()));
            dateCount.forEach((date, count) -> {
                if (count > 1) {
                    throw new BusinessException(400, "同一天不能安排多个实验，日期 " + date + " 冲突");
                }
            });

            for (PlanDetail detail : details) {
                detailMapper.insert(detail);
            }
        }

        return convertToVO(plan);
    }

    @Override
    @Transactional
    public TeachingPlanVO publishPlan(Long id) {
        TeachingPlan plan = planMapper.selectById(id);
        if (plan == null) {
            throw new BusinessException(ErrorCode.PLAN_NOT_FOUND);
        }
        if (plan.getStatus() == 1) {
            throw new BusinessException(400, "计划已是发布状态");
        }
        plan.setStatus(1);
        plan.setUpdateTime(LocalDateTime.now());
        planMapper.updateById(plan);
        return convertToVO(plan);
    }

    @Override
    @Transactional
    public void deletePlan(Long id) {
        TeachingPlan plan = planMapper.selectById(id);
        if (plan == null) {
            throw new BusinessException(ErrorCode.PLAN_NOT_FOUND);
        }
        LambdaQueryWrapper<PlanDetail> detailWrapper = new LambdaQueryWrapper<>();
        detailWrapper.eq(PlanDetail::getPlanId, id);
        detailMapper.delete(detailWrapper);
        planMapper.deleteById(id);
    }

    private TeachingPlanVO convertToVO(TeachingPlan plan) {
        TeachingPlanVO vo = new TeachingPlanVO();
        BeanUtils.copyProperties(plan, vo);
        vo.setTeacherId(plan.getCreateId());

        // 班级名称
        ClassEntity clazz = classMapper.selectById(plan.getClassId());
        if (clazz != null) {
            vo.setClassName(clazz.getClassName());
        }

        // ✅ 两步查询：user.id → user.username → teacher.teacher_id
        String teacherName = null;
        if (plan.getCreateId() != null) {
            // 第一步：通过 user.id 查用户，获取 username
            UserEntity user = userMapper.selectById(plan.getCreateId());
            if (user != null) {
                // 第二步：通过 username 匹配 teacher_id
                LambdaQueryWrapper<TeacherEntity> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(TeacherEntity::getTeacherId, user.getUsername());
                TeacherEntity teacher = teacherMapper.selectOne(wrapper);
                if (teacher != null) {
                    teacherName = teacher.getName();
                } else {
                    // 如果教师表没有记录，用 username 作为备选
                    teacherName = user.getUsername();
                }
            }
        }
        vo.setTeacherName(teacherName);

        // 状态名称
        vo.setStatusName(plan.getStatus() == 0 ? "草稿" : "已发布");

        // 实验列表
        LambdaQueryWrapper<PlanDetail> detailWrapper = new LambdaQueryWrapper<>();
        detailWrapper.eq(PlanDetail::getPlanId, plan.getId())
                .select(PlanDetail::getExperimentId);
        List<PlanDetail> details = detailMapper.selectList(detailWrapper);
        vo.setExperiments(details.stream().map(PlanDetail::getExperimentId).collect(Collectors.toList()));

        return vo;
    }
}