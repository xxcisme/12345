package com.uestc.group14.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.uestc.group14.backend.Entity.TeacherEntity;
import com.uestc.group14.backend.Entity.UserEntity;
import com.uestc.group14.backend.common.exception.BusinessException;
import com.uestc.group14.backend.common.exception.ErrorCode;
import com.uestc.group14.backend.common.utils.Sha256Util;
import com.uestc.group14.backend.dao.CourseMapper;
import com.uestc.group14.backend.dao.TeacherMapper;
import com.uestc.group14.backend.dao.UserMapper;
import com.uestc.group14.backend.dao.UserProfileMapper;
import com.uestc.group14.backend.dto.admin.TeacherAddDTO;
import com.uestc.group14.backend.dto.admin.TeacherUpdateDTO;
import com.uestc.group14.backend.service.TeacherService;
import com.uestc.group14.backend.vo.admin.TeacherVO;
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
public class TeacherServiceImpl implements TeacherService {

    private final TeacherMapper teacherMapper;
    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;
    private final CourseMapper courseMapper;

    @Override
    public IPage<TeacherVO> listTeachers(Integer pageNo, Integer pageSize, String name, String teacherId,
                                         String company, Boolean onJob) {
        Page<TeacherEntity> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<TeacherEntity> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(name)) {
            wrapper.like(TeacherEntity::getName, name);
        }
        if (StringUtils.hasText(teacherId)) {
            wrapper.eq(TeacherEntity::getTeacherId, teacherId);
        }
        if (StringUtils.hasText(company)) {
            wrapper.like(TeacherEntity::getCompany, company);
        }
        if (onJob != null) {
            wrapper.eq(TeacherEntity::getOnJob, onJob ? 1 : 0);
        }
        wrapper.orderByDesc(TeacherEntity::getCreateTime);
        IPage<TeacherEntity> entityPage = teacherMapper.selectPage(page, wrapper);

        List<TeacherVO> voList = entityPage.getRecords().stream().map(this::convertToVO).collect(Collectors.toList());
        Page<TeacherVO> voPage = new Page<>(pageNo, pageSize, entityPage.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public TeacherVO getTeacherDetail(Long id) {
        TeacherEntity teacher = teacherMapper.selectById(id);
        if (teacher == null) {
            throw new BusinessException(ErrorCode.TEACHER_NOT_FOUND);
        }
        TeacherVO vo = convertToVO(teacher);
        // 查询该教师负责的课程列表（简略）
        LambdaQueryWrapper<com.uestc.group14.backend.Entity.CourseEntity> courseWrapper =
                new LambdaQueryWrapper<>();
        courseWrapper.eq(com.uestc.group14.backend.Entity.CourseEntity::getTeacherId, id)
                .select(com.uestc.group14.backend.Entity.CourseEntity::getId,
                        com.uestc.group14.backend.Entity.CourseEntity::getCourseName,
                        com.uestc.group14.backend.Entity.CourseEntity::getCourseCode);
        List<com.uestc.group14.backend.Entity.CourseEntity> courses = courseMapper.selectList(courseWrapper);
        vo.setCourses(courses.stream().map(c -> {
            TeacherVO.CourseInfo info = new TeacherVO.CourseInfo();
            info.setId(c.getId());
            info.setCourseName(c.getCourseName());
            info.setCourseCode(c.getCourseCode());
            return info;
        }).collect(Collectors.toList()));
        return vo;
    }

    @Override
    @Transactional
    public Long addTeacher(TeacherAddDTO dto) {
        // 校验教师ID唯一性
        LambdaQueryWrapper<TeacherEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TeacherEntity::getTeacherId, dto.getTeacherId());
        if (teacherMapper.selectCount(wrapper) > 0) {
            throw new BusinessException(ErrorCode.TEACHER_NUMBER_EXIST);
        }
        // 同时创建用户账号（角色=2教师）
        UserEntity user = new UserEntity();
        user.setUsername(dto.getTeacherId()); // 以教师ID为用户名
        user.setPasswordHash(Sha256Util.encrypt("123456"));
        user.setRole(2);
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setStatus(1);
        user.setDelFlag(0);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.insert(user);

        // 创建教师记录
        TeacherEntity teacher = new TeacherEntity();
        BeanUtils.copyProperties(dto, teacher);
        teacher.setOnJob(dto.getOnJob() ? 1 : 0);
        teacher.setCreateTime(LocalDateTime.now());
        teacher.setUpdateTime(LocalDateTime.now());
        teacherMapper.insert(teacher);

        // 可选的：在user_profile中记录关联，但teacherId与userId是独立体系，不做强制关联
        return teacher.getId();
    }

    @Override
    @Transactional
    public TeacherVO updateTeacher(TeacherUpdateDTO dto) {
        TeacherEntity teacher = teacherMapper.selectById(dto.getId());
        if (teacher == null) {
            throw new BusinessException(ErrorCode.TEACHER_NOT_FOUND);
        }
        if (StringUtils.hasText(dto.getName())) {
            teacher.setName(dto.getName());
        }
        if (StringUtils.hasText(dto.getType())) {
            teacher.setType(dto.getType());
        }
        if (StringUtils.hasText(dto.getPhone())) {
            teacher.setPhone(dto.getPhone());
        }
        if (StringUtils.hasText(dto.getEmail())) {
            teacher.setEmail(dto.getEmail());
        }
        if (StringUtils.hasText(dto.getCompany())) {
            teacher.setCompany(dto.getCompany());
        }
        if (dto.getOnJob() != null) {
            teacher.setOnJob(dto.getOnJob() ? 1 : 0);
        }
        teacher.setUpdateTime(LocalDateTime.now());
        teacherMapper.updateById(teacher);
        return convertToVO(teacher);
    }

    @Override
    @Transactional
    public void deleteTeacher(Long id) {
        // 检查是否有课程关联
        LambdaQueryWrapper<com.uestc.group14.backend.Entity.CourseEntity> courseWrapper =
                new LambdaQueryWrapper<>();
        courseWrapper.eq(com.uestc.group14.backend.Entity.CourseEntity::getTeacherId, id);
        Long count = courseMapper.selectCount(courseWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.TEACHER_HAS_COURSE);
        }
        int deleted = teacherMapper.deleteById(id);
        if (deleted == 0) {
            throw new BusinessException(ErrorCode.TEACHER_NOT_FOUND);
        }
        // 注意：不删除对应的user账号，可保留或标记停用，这里仅删除教师记录
    }

    @Override
    @Transactional
    public void resetPassword(Long id) {
        TeacherEntity teacher = teacherMapper.selectById(id);
        if (teacher == null) {
            throw new BusinessException(ErrorCode.TEACHER_NOT_FOUND);
        }
        // 找到对应的用户账号（假设用户名=teacherId）
        LambdaQueryWrapper<UserEntity> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(UserEntity::getUsername, teacher.getTeacherId());
        UserEntity user = userMapper.selectOne(userWrapper);
        if (user == null) {
            // 没有对应账号则创建（但一般都会存在）
            user = new UserEntity();
            user.setUsername(teacher.getTeacherId());
            user.setPasswordHash(Sha256Util.encrypt("123456"));
            user.setRole(2);
            user.setPhone(teacher.getPhone());
            user.setEmail(teacher.getEmail());
            user.setStatus(1);
            user.setDelFlag(0);
            userMapper.insert(user);
        } else {
            user.setPasswordHash(Sha256Util.encrypt("123456"));
            userMapper.updateById(user);
        }
    }

    private TeacherVO convertToVO(TeacherEntity entity) {
        TeacherVO vo = new TeacherVO();
        BeanUtils.copyProperties(entity, vo);
        vo.setOnJob(entity.getOnJob() == 1);
        vo.setTypeName("0".equals(entity.getType()) ? "实训老师" : "非实训老师");
        return vo;
    }
}