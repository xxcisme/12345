package com.uestc.group14.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.uestc.group14.backend.Entity.*;
import com.uestc.group14.backend.common.exception.BusinessException;
import com.uestc.group14.backend.common.exception.ErrorCode;
import com.uestc.group14.backend.dao.*;
import com.uestc.group14.backend.dto.admin.GradeAddDTO;
import com.uestc.group14.backend.dto.admin.GradeUpdateDTO;
import com.uestc.group14.backend.service.GradeService;
import com.uestc.group14.backend.vo.admin.GradeStatisticsVO;
import com.uestc.group14.backend.vo.admin.GradeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {

    private final GradeMapper gradeMapper;
    private final PlanDetailMapper planDetailMapper;
    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;
    private final CourseMapper courseMapper;
    private final ExperimentMapper experimentMapper;
    private final ClassMapper classMapper;

    @Override
    public IPage<GradeVO> listGrades(Integer pageNo, Integer pageSize, Long courseId, Long classId,
                                     Integer status, String studentName) {
        Page<Grade> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<Grade> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(Grade::getPublishStatus, status);
        }
        wrapper.orderByDesc(Grade::getCreateTime);
        IPage<Grade> gradePage = gradeMapper.selectPage(page, wrapper);

        // ✅ 修复：使用过滤参数正确过滤
        List<GradeVO> voList = gradePage.getRecords().stream()
                .map(grade -> {
                    GradeVO vo = convertToVO(grade);
                    // 应用过滤条件
                    boolean match = true;
                    if (StringUtils.hasText(studentName) && vo.getStudentName() != null) {
                        if (!vo.getStudentName().contains(studentName)) {
                            match = false;
                        }
                    }
                    if (courseId != null && vo.getCourseId() != null) {
                        if (!courseId.equals(vo.getCourseId())) {
                            match = false;
                        }
                    }
                    if (classId != null && vo.getStudentId() != null) {
                        // 查询学生所属班级
                        LambdaQueryWrapper<UserProfile> profileWrapper = new LambdaQueryWrapper<>();
                        profileWrapper.eq(UserProfile::getUserId, vo.getStudentId());
                        UserProfile profile = userProfileMapper.selectOne(profileWrapper);
                        if (profile == null || !classId.equals(profile.getClassId())) {
                            match = false;
                        }
                    }
                    return match ? vo : null;
                })
                .filter(vo -> vo != null)
                .collect(Collectors.toList());

        Page<GradeVO> voPage = new Page<>(pageNo, pageSize, gradePage.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    @Transactional
    public Long addGrade(GradeAddDTO dto) {
        if (dto.getScore().compareTo(BigDecimal.ZERO) < 0 || dto.getScore().compareTo(new BigDecimal(100)) > 0) {
            throw new BusinessException(ErrorCode.GRADE_RANGE_ERROR);
        }
        PlanDetail detail = planDetailMapper.selectById(dto.getScheduleId());
        if (detail == null) {
            throw new BusinessException(ErrorCode.SCHEDULE_NOT_FOUND);
        }
        UserEntity user = userMapper.selectById(dto.getStudentId());
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        LambdaQueryWrapper<Grade> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Grade::getScheduleId, dto.getScheduleId())
                .eq(Grade::getStudentId, dto.getStudentId());
        Grade exist = gradeMapper.selectOne(wrapper);
        if (exist != null) {
            throw new BusinessException(400, "该学生该实验已评定成绩");
        }
        Grade grade = new Grade();
        grade.setScheduleId(dto.getScheduleId());
        grade.setStudentId(dto.getStudentId());
        grade.setOverallScore(dto.getScore());
        grade.setComment(dto.getComment());
        grade.setPublishStatus(dto.getPublish() ? 1 : 0);
        grade.setGradedAt(LocalDateTime.now());
        grade.setCreateTime(LocalDateTime.now());
        grade.setUpdateTime(LocalDateTime.now());
        gradeMapper.insert(grade);
        return grade.getId();
    }

    @Override
    @Transactional
    public GradeVO updateGrade(GradeUpdateDTO dto) {
        Grade grade = gradeMapper.selectById(dto.getId());
        if (grade == null) {
            throw new BusinessException(ErrorCode.GRADE_NOT_FOUND);
        }
        if (grade.getPublishStatus() == 1) {
            throw new BusinessException(ErrorCode.GRADE_ALREADY_PUBLISHED);
        }
        if (dto.getScore().compareTo(BigDecimal.ZERO) < 0 || dto.getScore().compareTo(new BigDecimal(100)) > 0) {
            throw new BusinessException(ErrorCode.GRADE_RANGE_ERROR);
        }
        grade.setOverallScore(dto.getScore());
        grade.setComment(dto.getComment());
        grade.setPublishStatus(dto.getPublish() ? 1 : 0);
        grade.setGradedAt(LocalDateTime.now());
        grade.setUpdateTime(LocalDateTime.now());
        gradeMapper.updateById(grade);
        return convertToVO(grade);
    }

    @Override
    @Transactional
    public GradeVO publishGrade(Long id) {
        Grade grade = gradeMapper.selectById(id);
        if (grade == null) {
            throw new BusinessException(ErrorCode.GRADE_NOT_FOUND);
        }
        if (grade.getPublishStatus() == 1) {
            throw new BusinessException(400, "成绩已发布");
        }
        grade.setPublishStatus(1);
        grade.setUpdateTime(LocalDateTime.now());
        gradeMapper.updateById(grade);
        return convertToVO(grade);
    }

    @Override
    public GradeStatisticsVO getStatistics(Long courseId, Long experimentId, Long classId) {
        LambdaQueryWrapper<PlanDetail> detailWrapper = new LambdaQueryWrapper<>();
        if (courseId != null) {
            detailWrapper.eq(PlanDetail::getCourseId, courseId);
        }
        if (experimentId != null) {
            detailWrapper.eq(PlanDetail::getExperimentId, experimentId);
        }
        List<PlanDetail> details = planDetailMapper.selectList(detailWrapper);
        if (details.isEmpty()) {
            return GradeStatisticsVO.empty();
        }
        List<Long> scheduleIds = details.stream().map(PlanDetail::getId).collect(Collectors.toList());

        LambdaQueryWrapper<Grade> gradeWrapper = new LambdaQueryWrapper<>();
        gradeWrapper.in(Grade::getScheduleId, scheduleIds)
                .eq(Grade::getPublishStatus, 1);
        List<Grade> grades = gradeMapper.selectList(gradeWrapper);
        if (grades.isEmpty()) {
            return GradeStatisticsVO.empty();
        }

        // 班级过滤：如果指定了班级，筛选属于该班级的学生
        if (classId != null) {
            // 获取该班级的所有学生ID
            LambdaQueryWrapper<UserProfile> profileWrapper = new LambdaQueryWrapper<>();
            profileWrapper.eq(UserProfile::getClassId, classId);
            List<UserProfile> profiles = userProfileMapper.selectList(profileWrapper);
            List<Long> studentIds = profiles.stream().map(UserProfile::getUserId).collect(Collectors.toList());
            grades = grades.stream()
                    .filter(g -> studentIds.contains(g.getStudentId()))
                    .collect(Collectors.toList());
            if (grades.isEmpty()) {
                return GradeStatisticsVO.empty();
            }
        }

        BigDecimal sum = grades.stream().map(Grade::getOverallScore)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal avg = sum.divide(new BigDecimal(grades.size()), 2, RoundingMode.HALF_UP);
        BigDecimal max = grades.stream().map(Grade::getOverallScore)
                .max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        BigDecimal min = grades.stream().map(Grade::getOverallScore)
                .min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        long passCount = grades.stream().filter(g -> g.getOverallScore().compareTo(new BigDecimal(60)) >= 0).count();
        double passRate = (double) passCount / grades.size() * 100;

        GradeStatisticsVO vo = new GradeStatisticsVO();
        vo.setAvgScore(avg);
        vo.setMaxScore(max);
        vo.setMinScore(min);
        vo.setPassRate(passRate);
        vo.setTotalCount((long) grades.size());
        vo.setPassCount(passCount);
        return vo;
    }

    private GradeVO convertToVO(Grade grade) {
        GradeVO vo = new GradeVO();
        BeanUtils.copyProperties(grade, vo);

        // 查询排课信息
        PlanDetail detail = planDetailMapper.selectById(grade.getScheduleId());
        if (detail != null) {
            vo.setScheduleId(detail.getId());
            CourseEntity course = courseMapper.selectById(detail.getCourseId());
            if (course != null) {
                vo.setCourseId(course.getId());
                vo.setCourseName(course.getCourseName());
            }
            Experiment experiment = experimentMapper.selectById(detail.getExperimentId());
            if (experiment != null) {
                vo.setExperimentId(experiment.getId());
                vo.setExperimentName(experiment.getName());
            }
        }

        // 学生信息
        UserEntity user = userMapper.selectById(grade.getStudentId());
        if (user != null) {
            vo.setStudentName(user.getUsername());
            // ✅ 新增：学号（使用 username 作为学号）
            vo.setStudentNo(user.getUsername());
        }

        vo.setPublishStatusName(grade.getPublishStatus() == 0 ? "未发布" : "已发布");
        return vo;
    }
}