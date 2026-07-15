package com.uestc.group14.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.uestc.group14.backend.Entity.*;
import com.uestc.group14.backend.common.enums.GlobalErrorCodeConstants;
import com.uestc.group14.backend.common.exception.BusinessException;
import com.uestc.group14.backend.common.utils.Sha256Util;
import com.uestc.group14.backend.dao.*;
import com.uestc.group14.backend.dto.*;
import com.uestc.group14.backend.service.UserCenterService;
import com.uestc.group14.backend.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserCenterServiceImpl implements UserCenterService {

    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;
    private final UserFavoriteMapper userFavoriteMapper;
    private final ResourceMapper resourceMapper;
    private final CourseMapper courseMapper;
    private final ExperimentMapper experimentMapper;
    private final GradeMapper gradeMapper;
    private final LabReportMapper labReportMapper;
    private final UserMessageMapper userMessageMapper;
    private final TeacherMapper teacherMapper;
    private final ClassMapper classMapper;  // 新增注入

    @Override
    public UserInfoVO getProfile(Long userId) {
        UserEntity user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(GlobalErrorCodeConstants.NOT_FOUND);
        }
        UserProfile profile = userProfileMapper.selectOne(
                new LambdaQueryWrapper<UserProfile>().eq(UserProfile::getUserId, userId)
        );
        UserInfoVO vo = new UserInfoVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setRole(user.getRole());
        vo.setPhone(user.getPhone());
        vo.setEmail(user.getEmail());
        vo.setStatus(user.getStatus());
        vo.setLastLoginTime(user.getLastLoginTime());
        vo.setLastLoginIp(user.getLastLoginIp());
        if (profile != null) {
            vo.setRealName(profile.getRealName());
            vo.setSchoolCode(profile.getSchoolCode());
            vo.setClassName(profile.getClassName());      // 直接取 profile 中的 className（若有）
            vo.setOccupationType(profile.getOccupationType());
            vo.setClassId(profile.getClassId());
            // 如果 profile 中 className 为空但 classId 有值，则从班级表查询
            if (!StringUtils.hasText(vo.getClassName()) && profile.getClassId() != null) {
                ClassEntity clazz = classMapper.selectById(profile.getClassId());
                if (clazz != null) {
                    vo.setClassName(clazz.getClassName());
                }
            }
        }
        return vo;
    }

    @Override
    @Transactional
    public UserInfoVO updateProfile(Long userId, UpdateProfileDTO dto) {
        UserEntity user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(GlobalErrorCodeConstants.NOT_FOUND);
        }
        if (StringUtils.hasText(dto.getPhone())) {
            user.setPhone(dto.getPhone());
        }
        if (StringUtils.hasText(dto.getEmail())) {
            user.setEmail(dto.getEmail());
        }
        userMapper.updateById(user);

        UserProfile profile = userProfileMapper.selectOne(
                new LambdaQueryWrapper<UserProfile>().eq(UserProfile::getUserId, userId)
        );
        if (profile == null) {
            profile = new UserProfile();
            profile.setUserId(userId);
        }
        if (StringUtils.hasText(dto.getRealName())) {
            profile.setRealName(dto.getRealName());
        }
        if (profile.getId() == null) {
            userProfileMapper.insert(profile);
        } else {
            userProfileMapper.updateById(profile);
        }

        // 返回更新后的个人信息
        return getProfile(userId);
    }

    @Override
    @Transactional
    public void changePassword(Long userId, ChangePasswordDTO dto) {
        if (!Objects.equals(dto.getNewPassword(), dto.getConfirmPassword())) {
            throw new BusinessException(400, "两次输入密码不一致");
        }
        UserEntity user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(GlobalErrorCodeConstants.NOT_FOUND);
        }
        String oldEncrypted = Sha256Util.encrypt(dto.getOldPassword());
        if (!oldEncrypted.equals(user.getPasswordHash())) {
            throw new BusinessException(400, "原密码错误");
        }
        user.setPasswordHash(Sha256Util.encrypt(dto.getNewPassword()));
        userMapper.updateById(user);
    }

    @Override
    public IPage<FavoriteVO> getFavorites(Long userId, Integer pageNo, Integer pageSize, Integer resourceType) {
        LambdaQueryWrapper<UserFavoriteEntity> allWrapper = new LambdaQueryWrapper<UserFavoriteEntity>()
                .eq(UserFavoriteEntity::getUserId, userId)
                .select(UserFavoriteEntity::getResourceId);
        List<Long> allResourceIds = userFavoriteMapper.selectList(allWrapper).stream()
                .map(UserFavoriteEntity::getResourceId)
                .collect(Collectors.toList());

        if (allResourceIds.isEmpty()) {
            return new Page<>(pageNo, pageSize, 0);
        }

        LambdaQueryWrapper<ResourceEntity> resWrapper = new LambdaQueryWrapper<ResourceEntity>()
                .in(ResourceEntity::getId, allResourceIds)
                .eq(ResourceEntity::getStatus, 2);
        if (resourceType != null) {
            resWrapper.eq(ResourceEntity::getType, resourceType);
        }
        List<ResourceEntity> resources = resourceMapper.selectList(resWrapper);
        List<Long> filteredResourceIds = resources.stream()
                .map(ResourceEntity::getId)
                .collect(Collectors.toList());

        if (filteredResourceIds.isEmpty()) {
            return new Page<>(pageNo, pageSize, 0);
        }

        Page<UserFavoriteEntity> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<UserFavoriteEntity> wrapper = new LambdaQueryWrapper<UserFavoriteEntity>()
                .eq(UserFavoriteEntity::getUserId, userId)
                .in(UserFavoriteEntity::getResourceId, filteredResourceIds)
                .orderByDesc(UserFavoriteEntity::getCreateTime);
        IPage<UserFavoriteEntity> favoritePage = userFavoriteMapper.selectPage(page, wrapper);

        Map<Long, ResourceEntity> resourceMap = resources.stream()
                .collect(Collectors.toMap(ResourceEntity::getId, Function.identity()));

        List<FavoriteVO> voList = favoritePage.getRecords().stream().map(f -> {
            FavoriteVO vo = new FavoriteVO();
            vo.setId(f.getId());
            vo.setResourceId(f.getResourceId());
            vo.setCreateTime(f.getCreateTime());
            ResourceEntity r = resourceMap.get(f.getResourceId());
            if (r != null) {
                vo.setResourceName(r.getName());
                vo.setResourceType(r.getType());
                vo.setThumbnail(r.getThumbnail());
                vo.setResourceTypeName(getResourceTypeName(r.getType()));
            }
            return vo;
        }).collect(Collectors.toList());

        Page<FavoriteVO> result = new Page<>(pageNo, pageSize, favoritePage.getTotal());
        result.setRecords(voList);
        return result;
    }

    private String getResourceTypeName(Integer type) {
        if (type == null) return null;
        switch (type) {
            case 1: return "视频";
            case 2: return "音频";
            case 3: return "文档";
            default: return null;
        }
    }

    @Override
    @Transactional
    public Long addFavorite(Long userId, Long resourceId) {
        ResourceEntity resource = resourceMapper.selectById(resourceId);
        if (resource == null || !Objects.equals(resource.getStatus(), 2)) {
            throw new BusinessException(400, "资源不存在或未发布");
        }
        LambdaQueryWrapper<UserFavoriteEntity> wrapper = new LambdaQueryWrapper<UserFavoriteEntity>()
                .eq(UserFavoriteEntity::getUserId, userId)
                .eq(UserFavoriteEntity::getResourceId, resourceId);
        Long count = userFavoriteMapper.selectCount(wrapper);
        if (count > 0) {
            throw new BusinessException(400, "已收藏该资源");
        }
        UserFavoriteEntity favorite = new UserFavoriteEntity();
        favorite.setUserId(userId);
        favorite.setResourceId(resourceId);
        userFavoriteMapper.insert(favorite);
        return favorite.getId(); // 返回新收藏ID
    }

    @Override
    @Transactional
    public void removeFavorite(Long userId, Long resourceId) {
        LambdaQueryWrapper<UserFavoriteEntity> wrapper = new LambdaQueryWrapper<UserFavoriteEntity>()
                .eq(UserFavoriteEntity::getUserId, userId)
                .eq(UserFavoriteEntity::getResourceId, resourceId);
        int deleted = userFavoriteMapper.delete(wrapper);
        if (deleted == 0) {
            throw new BusinessException(400, "收藏记录不存在");
        }
    }

    @Override
    public IPage<CourseVO> getCourses(Long userId, Integer pageNo, Integer pageSize, Integer status) {
        UserEntity user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(GlobalErrorCodeConstants.NOT_FOUND);
        }
        
        if (user.getRole() == 2) {
            Long teacherId = getTeacherIdByUserId(userId);
            if (teacherId == null) {
                return new Page<>(pageNo, pageSize, 0);
            }
            return getTeacherCourses(teacherId, pageNo, pageSize, status);
        } else {
            Page<CourseVO> page = new Page<>(pageNo, pageSize);
            return labReportMapper.selectCourseVoPage(page, userId, status);
        }
    }

    @Override
    public IPage<ExperimentVO> getExperiments(Long userId, Integer pageNo, Integer pageSize,
                                              Integer status, Long courseId) {
        UserEntity user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(GlobalErrorCodeConstants.NOT_FOUND);
        }
        
        if (user.getRole() == 2) {
            Long teacherId = getTeacherIdByUserId(userId);
            if (teacherId == null) {
                return new Page<>(pageNo, pageSize, 0);
            }
            return getTeacherExperiments(teacherId, pageNo, pageSize, status, courseId);
        } else {
            Page<ExperimentVO> page = new Page<>(pageNo, pageSize);
            return labReportMapper.selectExperimentVoPage(page, userId, status, courseId);
        }
    }

    private Long getTeacherIdByUserId(Long userId) {
        UserEntity user = userMapper.selectById(userId);
        if (user == null) {
            return null;
        }
        LambdaQueryWrapper<TeacherEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TeacherEntity::getTeacherId, user.getUsername());
        TeacherEntity teacher = teacherMapper.selectOne(wrapper);
        return teacher != null ? teacher.getId() : null;
    }

    private IPage<CourseVO> getTeacherCourses(Long teacherId, Integer pageNo, Integer pageSize, Integer status) {
        Page<CourseEntity> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<CourseEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CourseEntity::getTeacherId, teacherId);
        if (status != null) {
            wrapper.eq(CourseEntity::getStatus, status);
        }
        wrapper.orderByDesc(CourseEntity::getCreateTime);
        IPage<CourseEntity> entityPage = courseMapper.selectPage(page, wrapper);

        List<CourseVO> voList = entityPage.getRecords().stream().map(course -> {
            CourseVO vo = new CourseVO();
            BeanUtils.copyProperties(course, vo);
            TeacherEntity teacher = teacherMapper.selectById(course.getTeacherId());
            if (teacher != null) {
                vo.setTeacherName(teacher.getName());
            }
            return vo;
        }).collect(Collectors.toList());

        Page<CourseVO> voPage = new Page<>(pageNo, pageSize, entityPage.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    private IPage<ExperimentVO> getTeacherExperiments(Long teacherId, Integer pageNo, Integer pageSize,
                                                      Integer status, Long courseId) {
        LambdaQueryWrapper<CourseEntity> courseWrapper = new LambdaQueryWrapper<>();
        courseWrapper.eq(CourseEntity::getTeacherId, teacherId);
        if (courseId != null) {
            courseWrapper.eq(CourseEntity::getId, courseId);
        }
        List<CourseEntity> courses = courseMapper.selectList(courseWrapper);
        if (courses.isEmpty()) {
            return new Page<>(pageNo, pageSize, 0);
        }

        List<Long> courseIds = courses.stream().map(CourseEntity::getId).collect(Collectors.toList());

        Page<Experiment> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<Experiment> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Experiment::getCourseId, courseIds);
        if (status != null) {
            wrapper.eq(Experiment::getStatus, status);
        }
        wrapper.orderByDesc(Experiment::getCreateTime);
        IPage<Experiment> entityPage = experimentMapper.selectPage(page, wrapper);

        Map<Long, String> courseNameMap = courses.stream()
                .collect(Collectors.toMap(CourseEntity::getId, CourseEntity::getCourseName));

        List<ExperimentVO> voList = entityPage.getRecords().stream().map(exp -> {
            ExperimentVO vo = new ExperimentVO();
            BeanUtils.copyProperties(exp, vo);
            vo.setCourseName(courseNameMap.get(exp.getCourseId()));
            return vo;
        }).collect(Collectors.toList());

        Page<ExperimentVO> voPage = new Page<>(pageNo, pageSize, entityPage.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public IPage<MessageVO> getMessages(Long userId, Integer pageNo, Integer pageSize, Integer type, Boolean isRead) {
        Page<UserMessage> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<UserMessage> wrapper = new LambdaQueryWrapper<UserMessage>()
                .eq(UserMessage::getUserId, userId);
        if (type != null) {
            wrapper.eq(UserMessage::getType, type);
        }
        if (isRead != null) {
            wrapper.eq(UserMessage::getIsRead, isRead ? 1 : 0);
        }
        wrapper.orderByDesc(UserMessage::getCreateTime);
        IPage<UserMessage> msgPage = userMessageMapper.selectPage(page, wrapper);

        List<MessageVO> voList = msgPage.getRecords().stream().map(m -> {
            MessageVO vo = new MessageVO();
            vo.setId(m.getId());
            vo.setTitle(m.getTitle());
            vo.setContent(m.getContent());
            vo.setType(m.getType());
            vo.setTypeName(getMessageTypeName(m.getType()));
            vo.setIsRead(m.getIsRead());
            vo.setCreateTime(m.getCreateTime());
            return vo;
        }).collect(Collectors.toList());

        Page<MessageVO> result = new Page<>(pageNo, pageSize, msgPage.getTotal());
        result.setRecords(voList);
        return result;
    }

    private String getMessageTypeName(Integer type) {
        if (type == null) return null;
        switch (type) {
            case 1: return "审核通知";
            case 2: return "课程提醒";
            case 3: return "成绩通知";
            default: return null;
        }
    }

    @Override
    @Transactional
    public int markMessagesRead(Long userId, List<Long> messageIds) {
        LambdaQueryWrapper<UserMessage> wrapper = new LambdaQueryWrapper<UserMessage>()
                .eq(UserMessage::getUserId, userId);
        if (messageIds != null && !messageIds.isEmpty()) {
            wrapper.in(UserMessage::getId, messageIds);
        }
        UserMessage update = new UserMessage();
        update.setIsRead(1);
        return userMessageMapper.update(update, wrapper);
    }

    @Override
    public CourseVO getCourseDetail(Long userId, Long courseId) {
        // 1. 获取当前用户信息
        UserEntity user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(GlobalErrorCodeConstants.NOT_FOUND);
        }
        Integer role = user.getRole();

        // 2. 权限校验
        boolean hasPermission = false;
        if (role == 4) { // 管理员
            hasPermission = true;
        } else if (role == 2) { // 教师
            CourseEntity course = courseMapper.selectById(courseId);
            if (course != null && course.getTeacherId() != null && course.getTeacherId().equals(userId)) {
                hasPermission = true;
            }
        } else if (role == 1) { // 学生
            LambdaQueryWrapper<LabReport> reportWrapper = new LambdaQueryWrapper<>();
            reportWrapper.eq(LabReport::getStudentId, userId)
                    .inSql(LabReport::getScheduleId,
                            "SELECT id FROM tc_plan_detail WHERE course_id = " + courseId);
            Long count = labReportMapper.selectCount(reportWrapper);
            if (count > 0) {
                hasPermission = true;
            }
        }
        if (!hasPermission) {
            throw new BusinessException(GlobalErrorCodeConstants.NO_PERMISSION);
        }

        // 3. 查询课程信息
        CourseEntity course = courseMapper.selectById(courseId);
        if (course == null) {
            throw new BusinessException(GlobalErrorCodeConstants.COURSE_NOT_FOUND);
        }

        // 4. 组装 CourseVO
        CourseVO vo = new CourseVO();
        BeanUtils.copyProperties(course, vo);
        if (course.getTeacherId() != null) {
            TeacherEntity teacher = teacherMapper.selectById(course.getTeacherId());
            if (teacher != null) {
                vo.setTeacherName(teacher.getName());
            }
        }
        return vo;
    }

    @Override
    public ExperimentVO getExperimentDetail(Long userId, Long experimentId) {
        // 1. 获取当前用户信息
        UserEntity user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(GlobalErrorCodeConstants.NOT_FOUND);
        }
        Integer role = user.getRole();

        // 2. 查询实验基本信息
        Experiment experiment = experimentMapper.selectById(experimentId);
        if (experiment == null) {
            throw new BusinessException(GlobalErrorCodeConstants.EXPERIMENT_NOT_FOUND);
        }

        // 3. 权限校验
        boolean hasPermission = false;
        if (role == 4) { // 管理员
            hasPermission = true;
        } else if (role == 2) { // 教师：检查是否是该实验所属课程的教师
            if (experiment.getCourseId() != null) {
                CourseEntity course = courseMapper.selectById(experiment.getCourseId());
                if (course != null && course.getTeacherId() != null && course.getTeacherId().equals(userId)) {
                    hasPermission = true;
                }
            }
        } else if (role == 1) { // 学生
            LambdaQueryWrapper<LabReport> reportWrapper = new LambdaQueryWrapper<>();
            reportWrapper.eq(LabReport::getStudentId, userId)
                    .inSql(LabReport::getScheduleId,
                            "SELECT id FROM tc_plan_detail WHERE experiment_id = " + experimentId);
            LabReport report = labReportMapper.selectOne(reportWrapper);
            if (report != null) {
                hasPermission = true;
                // 保留report用于后续填充
            }
        }
        if (!hasPermission) {
            throw new BusinessException(GlobalErrorCodeConstants.NO_PERMISSION);
        }

        // 4. 组装 VO
        ExperimentVO vo = new ExperimentVO();
        BeanUtils.copyProperties(experiment, vo);

        if (experiment.getCourseId() != null) {
            CourseEntity course = courseMapper.selectById(experiment.getCourseId());
            if (course != null) {
                vo.setCourseName(course.getCourseName());
            }
        }

        // 如果是学生，填充报告信息（若已有报告）
        if (role == 1) {
            LambdaQueryWrapper<LabReport> reportWrapper = new LambdaQueryWrapper<>();
            reportWrapper.eq(LabReport::getStudentId, userId)
                    .inSql(LabReport::getScheduleId,
                            "SELECT id FROM tc_plan_detail WHERE experiment_id = " + experimentId);
            LabReport report = labReportMapper.selectOne(reportWrapper);
            if (report != null) {
                vo.setGrade(report.getScore());
                vo.setEvaluationStatus(report.getEvaluationStatus());
                vo.setSubmittedAt(report.getSubmittedAt());
                vo.setReport(report.getReportContent());
            }
        }

        return vo;
    }
}