package com.uestc.group14.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.uestc.group14.backend.Entity.CourseEntity;
import com.uestc.group14.backend.Entity.Experiment;
import com.uestc.group14.backend.Entity.TeacherEntity;
import com.uestc.group14.backend.common.exception.BusinessException;
import com.uestc.group14.backend.common.exception.ErrorCode;
import com.uestc.group14.backend.dao.CourseMapper;
import com.uestc.group14.backend.dao.ExperimentMapper;
import com.uestc.group14.backend.dao.TeacherMapper;
import com.uestc.group14.backend.dto.admin.CourseAddDTO;
import com.uestc.group14.backend.dto.admin.CourseUpdateDTO;
import com.uestc.group14.backend.service.CourseService;
import com.uestc.group14.backend.vo.CourseVO;
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
public class CourseServiceImpl implements CourseService {

    private final CourseMapper courseMapper;
    private final TeacherMapper teacherMapper;
    private final ExperimentMapper experimentMapper;

    @Override
    public IPage<CourseVO> listCourses(Integer pageNo, Integer pageSize, String courseName, String courseCode,
                                       String courseType, Integer status, Long teacherId) {
        Page<CourseEntity> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<CourseEntity> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(courseName)) {
            wrapper.like(CourseEntity::getCourseName, courseName);
        }
        if (StringUtils.hasText(courseCode)) {
            wrapper.eq(CourseEntity::getCourseCode, courseCode);
        }
        if (StringUtils.hasText(courseType)) {
            wrapper.eq(CourseEntity::getCourseType, courseType);
        }
        if (status != null) {
            wrapper.eq(CourseEntity::getStatus, status);
        }
        if (teacherId != null) {
            wrapper.eq(CourseEntity::getTeacherId, teacherId);
        }
        wrapper.orderByDesc(CourseEntity::getCreateTime);
        IPage<CourseEntity> entityPage = courseMapper.selectPage(page, wrapper);

        List<CourseVO> voList = entityPage.getRecords().stream().map(this::convertToVO).collect(Collectors.toList());
        Page<CourseVO> voPage = new Page<>(pageNo, pageSize, entityPage.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    @Transactional
    public Long addCourse(CourseAddDTO dto) {
        // 校验课程编号唯一性
        LambdaQueryWrapper<CourseEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CourseEntity::getCourseCode, dto.getCourseCode());
        if (courseMapper.selectCount(wrapper) > 0) {
            throw new BusinessException(ErrorCode.COURSE_CODE_EXIST);
        }
        // 校验教师存在
        TeacherEntity teacher = teacherMapper.selectById(dto.getTeacherId());
        if (teacher == null) {
            throw new BusinessException(ErrorCode.TEACHER_NOT_FOUND);
        }
        CourseEntity course = new CourseEntity();
        BeanUtils.copyProperties(dto, course);
        course.setStatus(0); // 草稿
        course.setCreateTime(LocalDateTime.now());
        course.setUpdateTime(LocalDateTime.now());
        courseMapper.insert(course);
        return course.getId();
    }

    @Override
    @Transactional
    public CourseVO updateCourse(CourseUpdateDTO dto) {
        CourseEntity course = courseMapper.selectById(dto.getId());
        if (course == null) {
            throw new BusinessException(ErrorCode.COURSE_NOT_FOUND);
        }
        // 若修改了教师，校验存在
        if (dto.getTeacherId() != null && !dto.getTeacherId().equals(course.getTeacherId())) {
            TeacherEntity teacher = teacherMapper.selectById(dto.getTeacherId());
            if (teacher == null) {
                throw new BusinessException(ErrorCode.TEACHER_NOT_FOUND);
            }
            course.setTeacherId(dto.getTeacherId());
        }
        if (StringUtils.hasText(dto.getCourseName())) {
            course.setCourseName(dto.getCourseName());
        }
        if (StringUtils.hasText(dto.getCourseType())) {
            course.setCourseType(dto.getCourseType());
        }
        if (dto.getClassHours() != null) {
            course.setClassHours(dto.getClassHours());
        }
        if (dto.getCredit() != null) {
            course.setCredit(dto.getCredit());
        }
        if (StringUtils.hasText(dto.getIntroduction())) {
            course.setIntroduction(dto.getIntroduction());
        }
        if (StringUtils.hasText(dto.getOutline())) {
            course.setOutline(dto.getOutline());
        }
        // 如果当前状态是已发布，改为草稿（需重新发布）
        if (course.getStatus() == 1) {
            course.setStatus(0);
        }
        course.setUpdateTime(LocalDateTime.now());
        courseMapper.updateById(course);
        return convertToVO(course);
    }

    @Override
    @Transactional
    public CourseVO publishCourse(Long id) {
        CourseEntity course = courseMapper.selectById(id);
        if (course == null) {
            throw new BusinessException(ErrorCode.COURSE_NOT_FOUND);
        }
        if (course.getStatus() == 1) {
            throw new BusinessException(400, "课程已是发布状态");
        }
        course.setStatus(1);
        course.setUpdateTime(LocalDateTime.now());
        courseMapper.updateById(course);
        return convertToVO(course);
    }

    @Override
    @Transactional
    public CourseVO unpublishCourse(Long id) {
        CourseEntity course = courseMapper.selectById(id);
        if (course == null) {
            throw new BusinessException(ErrorCode.COURSE_NOT_FOUND);
        }
        if (course.getStatus() != 1) {
            throw new BusinessException(400, "只有已发布的课程才能下架");
        }
        course.setStatus(2);
        course.setUpdateTime(LocalDateTime.now());
        courseMapper.updateById(course);
        return convertToVO(course);
    }

    @Override
    @Transactional
    public void deleteCourse(Long id) {
        CourseEntity course = courseMapper.selectById(id);
        if (course == null) {
            throw new BusinessException(ErrorCode.COURSE_NOT_FOUND);
        }
        // 检查是否有实验或学生关联（这里简单检查是否有实验）
        LambdaQueryWrapper<Experiment> expWrapper = new LambdaQueryWrapper<>();
        expWrapper.eq(Experiment::getCourseId, id);
        Long expCount = experimentMapper.selectCount(expWrapper);
        if (expCount > 0) {
            throw new BusinessException(ErrorCode.COURSE_HAS_STUDENT); // 复用提示，表示存在关联实验
        }
        // 如果有学生关联（实际可通过计划明细等复杂关联，暂时略）
        courseMapper.deleteById(id);
    }

    private CourseVO convertToVO(CourseEntity entity) {
        CourseVO vo = new CourseVO();
        BeanUtils.copyProperties(entity, vo);
        // 查询教师名称
        if (entity.getTeacherId() != null) {
            TeacherEntity teacher = teacherMapper.selectById(entity.getTeacherId());
            if (teacher != null) {
                vo.setTeacherName(teacher.getName());
            }
        }
        return vo;
    }

}