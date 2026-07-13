package com.uestc.group14.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.uestc.group14.backend.Entity.*;
import com.uestc.group14.backend.common.enums.GlobalErrorCodeConstants;
// 移除 Md5Util 导入，改用 Sha256Util
import com.uestc.group14.backend.common.utils.Sha256Util;
import com.uestc.group14.backend.dao.*;
import com.uestc.group14.backend.dto.*;
import com.uestc.group14.backend.service.UserCenterService;
import com.uestc.group14.backend.vo.*;
import lombok.RequiredArgsConstructor;
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

    // ==================== ★ 修改点：changePassword 方法 ====================
    @Override
    @Transactional
    public void changePassword(Long userId, ChangePasswordDTO dto) {
        if (!Objects.equals(dto.getNewPassword(), dto.getConfirmPassword())) {
            throw new RuntimeException("两次输入密码不一致");
        }
        UserEntity user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException(GlobalErrorCodeConstants.NOT_FOUND.getMsg());
        }
        // 统一使用 SHA-256 加密，与注册/登录保持一致
        String oldEncrypted = Sha256Util.encrypt(dto.getOldPassword());
        if (!oldEncrypted.equals(user.getPasswordHash())) {
            throw new RuntimeException("原密码错误");
        }
        // 新密码同样使用 SHA-256
        user.setPasswordHash(Sha256Util.encrypt(dto.getNewPassword()));
        userMapper.updateById(user);
    }

    @Override
    public IPage<FavoriteVO> getFavorites(Long userId, Integer pageNo, Integer pageSize, Integer resourceType) {
        // 1. 先查该用户所有收藏的 resourceId（不分页）
        LambdaQueryWrapper<UserFavoriteEntity> allWrapper = new LambdaQueryWrapper<UserFavoriteEntity>()
                .eq(UserFavoriteEntity::getUserId, userId)
                .select(UserFavoriteEntity::getResourceId);
        List<Long> allResourceIds = userFavoriteMapper.selectList(allWrapper).stream()
                .map(UserFavoriteEntity::getResourceId)
                .collect(Collectors.toList());

        if (allResourceIds.isEmpty()) {
            return new Page<>(pageNo, pageSize, 0);
        }

        // 2. 根据资源条件过滤，只保留符合条件的 resourceId
        LambdaQueryWrapper<ResourceEntity> resWrapper = new LambdaQueryWrapper<ResourceEntity>()
                .in(ResourceEntity::getId, allResourceIds)
                .eq(ResourceEntity::getStatus, 2);   // 只显示已发布的资源
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

        // 3. 再按过滤后的 resourceIds 分页查询收藏记录
        Page<UserFavoriteEntity> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<UserFavoriteEntity> wrapper = new LambdaQueryWrapper<UserFavoriteEntity>()
                .eq(UserFavoriteEntity::getUserId, userId)
                .in(UserFavoriteEntity::getResourceId, filteredResourceIds)
                .orderByDesc(UserFavoriteEntity::getCreateTime);
        IPage<UserFavoriteEntity> favoritePage = userFavoriteMapper.selectPage(page, wrapper);

        // 4. 组装 VO（使用 Map 提高性能）
        Map<Long, ResourceEntity> resourceMap = resources.stream()
                .collect(Collectors.toMap(ResourceEntity::getId, Function.identity()));

        List<FavoriteVO> voList = favoritePage.getRecords().stream().map(f -> {
            FavoriteVO vo = new FavoriteVO();
            vo.setResourceId(f.getResourceId());
            vo.setCreateTime(f.getCreateTime());
            ResourceEntity r = resourceMap.get(f.getResourceId());
            if (r != null) {
                vo.setResourceName(r.getName());
                vo.setResourceType(r.getType());
                vo.setThumbnail(r.getThumbnail());
            }
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
        Page<CourseVO> page = new Page<>(pageNo, pageSize);
        // 联表查询（通过实验报告关联排课、课程、教师）
        return labReportMapper.selectCourseVoPage(page, userId, status);
    }

    // 在 UserCenterServiceImpl 中
    @Override
    public IPage<ExperimentVO> getExperiments(Long userId, Integer pageNo, Integer pageSize,
                                              Integer status, Long courseId) {
        Page<ExperimentVO> page = new Page<>(pageNo, pageSize);
        return labReportMapper.selectExperimentVoPage(page, userId, status, courseId);
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