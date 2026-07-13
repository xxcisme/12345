package com.uestc.group14.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.uestc.group14.backend.Entity.*;
import com.uestc.group14.backend.common.enums.GlobalErrorCodeConstants;
import com.uestc.group14.backend.common.utils.Md5Util;
import com.uestc.group14.backend.dao.*;
import com.uestc.group14.backend.dto.*;
import com.uestc.group14.backend.service.UserCenterService;
import com.uestc.group14.backend.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
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
    private final StudentCourseMapper studentCourseMapper;
    private final UserMessageMapper userMessageMapper;

    @Override
    public UserInfoVO getProfile(Long userId) {
        UserEntity user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException(GlobalErrorCodeConstants.NOT_FOUND.getMsg());
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
        if (profile != null) {
            vo.setRealName(profile.getRealName());
            vo.setSchoolCode(profile.getSchoolCode());
            vo.setClassName(profile.getClassName());
            vo.setOccupationType(profile.getOccupationType());
        }
        return vo;
    }

    @Override
    @Transactional
    public void updateProfile(Long userId, UpdateProfileDTO dto) {
        UserEntity user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException(GlobalErrorCodeConstants.NOT_FOUND.getMsg());
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
        userProfileMapper.insertOrUpdate(profile);
    }

    @Override
    public void changePassword(Long userId, ChangePasswordDTO dto) {
        if (!Objects.equals(dto.getNewPassword(), dto.getConfirmPassword())) {
            throw new RuntimeException("两次输入密码不一致");
        }
        UserEntity user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException(GlobalErrorCodeConstants.NOT_FOUND.getMsg());
        }
        String oldEncrypted = Md5Util.encrypt(dto.getOldPassword());
        if (!oldEncrypted.equals(user.getPasswordHash())) {
            throw new RuntimeException("原密码错误");
        }
        user.setPasswordHash(Md5Util.encrypt(dto.getNewPassword()));
        userMapper.updateById(user);
    }

    @Override
    public IPage<FavoriteVO> getFavorites(Long userId, Integer pageNo, Integer pageSize, Integer resourceType) {
        Page<UserFavoriteEntity> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<UserFavoriteEntity> wrapper = new LambdaQueryWrapper<UserFavoriteEntity>()
                .eq(UserFavoriteEntity::getUserId, userId)
                .orderByDesc(UserFavoriteEntity::getCreateTime);
        IPage<UserFavoriteEntity> favoritePage = userFavoriteMapper.selectPage(page, wrapper);

        if (favoritePage.getRecords().isEmpty()) {
            return new Page<>(pageNo, pageSize, 0);
        }
        List<Long> resourceIds = favoritePage.getRecords().stream()
                .map(UserFavoriteEntity::getResourceId).collect(Collectors.toList());

        LambdaQueryWrapper<ResourceEntity> resWrapper = new LambdaQueryWrapper<ResourceEntity>()
                .in(ResourceEntity::getId, resourceIds)
                .eq(ResourceEntity::getStatus, 2); // 只显示已发布
        if (resourceType != null) {
            resWrapper.eq(ResourceEntity::getType, resourceType);
        }
        List<ResourceEntity> resources = resourceMapper.selectList(resWrapper);

        List<FavoriteVO> voList = resources.stream().map(r -> {
            FavoriteVO vo = new FavoriteVO();
            vo.setResourceId(r.getId());
            vo.setResourceName(r.getName());
            vo.setResourceType(r.getType());
            vo.setThumbnail(r.getThumbnail());
            favoritePage.getRecords().stream()
                    .filter(f -> Objects.equals(f.getResourceId(), r.getId()))
                    .findFirst()
                    .ifPresent(f -> vo.setCreateTime(f.getCreateTime()));
            return vo;
        }).collect(Collectors.toList());

        Page<FavoriteVO> result = new Page<>(pageNo, pageSize, favoritePage.getTotal());
        result.setRecords(voList);
        return result;
    }

    @Override
    @Transactional
    public void addFavorite(Long userId, Long resourceId) {
        ResourceEntity resource = resourceMapper.selectById(resourceId);
        if (resource == null || !Objects.equals(resource.getStatus(), 2)) {
            throw new RuntimeException("资源不存在或未发布");
        }
        LambdaQueryWrapper<UserFavoriteEntity> wrapper = new LambdaQueryWrapper<UserFavoriteEntity>()
                .eq(UserFavoriteEntity::getUserId, userId)
                .eq(UserFavoriteEntity::getResourceId, resourceId);
        Long count = userFavoriteMapper.selectCount(wrapper);
        if (count > 0) {
            throw new RuntimeException("已收藏该资源");
        }
        UserFavoriteEntity favorite = new UserFavoriteEntity();
        favorite.setUserId(userId);
        favorite.setResourceId(resourceId);
        userFavoriteMapper.insert(favorite);
    }

    @Override
    @Transactional
    public void removeFavorite(Long userId, Long resourceId) {
        LambdaQueryWrapper<UserFavoriteEntity> wrapper = new LambdaQueryWrapper<UserFavoriteEntity>()
                .eq(UserFavoriteEntity::getUserId, userId)
                .eq(UserFavoriteEntity::getResourceId, resourceId);
        int deleted = userFavoriteMapper.delete(wrapper);
        if (deleted == 0) {
            throw new RuntimeException("收藏记录不存在");
        }
    }

    @Override
    public IPage<CourseVO> getCourses(Long userId, Integer pageNo, Integer pageSize, Integer status) {
        Page<StudentCourse> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<StudentCourse> wrapper = new LambdaQueryWrapper<StudentCourse>()
                .eq(StudentCourse::getStudentId, userId);
        if (status != null) {
            wrapper.eq(StudentCourse::getStatus, status);
        }
        wrapper.orderByDesc(StudentCourse::getCreateTime);
        IPage<StudentCourse> scPage = studentCourseMapper.selectPage(page, wrapper);
        if (scPage.getRecords().isEmpty()) {
            return new Page<>(pageNo, pageSize, 0);
        }
        List<Long> courseIds = scPage.getRecords().stream()
                .map(StudentCourse::getCourseId).collect(Collectors.toList());
        List<CourseEntity> courses = courseMapper.selectBatchIds(courseIds);

        // 获取教师名称（此处简化，实际可联查teacher表）
        List<CourseVO> voList = courses.stream().map(c -> {
            CourseVO vo = new CourseVO();
            vo.setId(c.getId());
            vo.setCourseName(c.getCourseName());
            vo.setTeacherName("教师"); // 如需真实名称，可注入TeacherMapper查询
            scPage.getRecords().stream()
                    .filter(sc -> Objects.equals(sc.getCourseId(), c.getId()))
                    .findFirst()
                    .ifPresent(sc -> vo.setStatus(sc.getStatus()));
            return vo;
        }).collect(Collectors.toList());

        Page<CourseVO> result = new Page<>(pageNo, pageSize, scPage.getTotal());
        result.setRecords(voList);
        return result;
    }

    @Override
    public IPage<ExperimentVO> getExperiments(Long userId, Integer pageNo, Integer pageSize, Integer status, Long courseId) {
        // 通过实验室报告表关联查询，此处简化为直接查询报告表
        Page<LabReport> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<LabReport> wrapper = new LambdaQueryWrapper<LabReport>()
                .eq(LabReport::getStudentId, userId);
        if (courseId != null) {
            // 需要关联实验表，此处略（可先查实验ID再过滤）
        }
        wrapper.orderByDesc(LabReport::getCreateTime);
        IPage<LabReport> reportPage = labReportMapper.selectPage(page, wrapper);
        if (reportPage.getRecords().isEmpty()) {
            return new Page<>(pageNo, pageSize, 0);
        }
        // 获取实验名称（实际应通过 scheduleId -> 排课表 -> 实验表）
        // 为简化，暂返回默认值
        List<ExperimentVO> voList = reportPage.getRecords().stream().map(r -> {
            ExperimentVO vo = new ExperimentVO();
            vo.setId(r.getId());
            vo.setName("实验名称");
            vo.setCourseName("课程");
            vo.setSubmittedAt(r.getSubmittedAt());
            vo.setScore(r.getScore());
            vo.setEvaluationStatus(r.getEvaluationStatus());
            return vo;
        }).collect(Collectors.toList());

        Page<ExperimentVO> result = new Page<>(pageNo, pageSize, reportPage.getTotal());
        result.setRecords(voList);
        return result;
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
            vo.setIsRead(m.getIsRead());
            vo.setCreateTime(m.getCreateTime());
            return vo;
        }).collect(Collectors.toList());
        Page<MessageVO> result = new Page<>(pageNo, pageSize, msgPage.getTotal());
        result.setRecords(voList);
        return result;
    }

    @Override
    @Transactional
    public void markMessagesRead(Long userId, List<Long> messageIds) {
        LambdaQueryWrapper<UserMessage> wrapper = new LambdaQueryWrapper<UserMessage>()
                .eq(UserMessage::getUserId, userId);
        if (messageIds != null && !messageIds.isEmpty()) {
            wrapper.in(UserMessage::getId, messageIds);
        }
        UserMessage update = new UserMessage();
        update.setIsRead(1);
        userMessageMapper.update(update, wrapper);
    }
}