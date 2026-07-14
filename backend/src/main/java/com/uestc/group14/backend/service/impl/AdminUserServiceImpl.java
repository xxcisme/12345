package com.uestc.group14.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.uestc.group14.backend.Entity.UserEntity;
import com.uestc.group14.backend.Entity.UserProfile;
import com.uestc.group14.backend.common.exception.BusinessException;
import com.uestc.group14.backend.common.exception.ErrorCode;
import com.uestc.group14.backend.common.utils.Sha256Util;
import com.uestc.group14.backend.dao.UserMapper;
import com.uestc.group14.backend.dao.UserProfileMapper;
import com.uestc.group14.backend.dto.admin.UserAddDTO;
import com.uestc.group14.backend.dto.admin.UserQueryDTO;
import com.uestc.group14.backend.dto.admin.UserResetPwdDTO;
import com.uestc.group14.backend.dto.admin.UserRoleUpdateDTO;
import com.uestc.group14.backend.dto.admin.UserStatusUpdateDTO;
import com.uestc.group14.backend.service.AdminUserService;
import com.uestc.group14.backend.vo.admin.AdminUserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;

    private static final Map<Integer, String> ROLE_MAP = Map.of(
            1, "学生",
            2, "老师",
            3, "社会人士",
            4, "管理员"
    );
    private static final Map<Integer, String> STATUS_MAP = Map.of(
            0, "停用",
            1, "启用"
    );

    @Override
    public IPage<AdminUserVO> listUsers(UserQueryDTO queryDTO) {
        Page<UserEntity> page = new Page<>(queryDTO.getPageNo(), queryDTO.getPageSize());
        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserEntity::getDelFlag, 0);
        if (StringUtils.hasText(queryDTO.getUsername())) {
            wrapper.like(UserEntity::getUsername, queryDTO.getUsername());
        }
        if (StringUtils.hasText(queryDTO.getPhone())) {
            wrapper.like(UserEntity::getPhone, queryDTO.getPhone());
        }
        if (queryDTO.getRole() != null) {
            wrapper.eq(UserEntity::getRole, queryDTO.getRole());
        }
        if (queryDTO.getStatus() != null) {
            wrapper.eq(UserEntity::getStatus, queryDTO.getStatus());
        }
        wrapper.orderByDesc(UserEntity::getCreateTime);

        IPage<UserEntity> entityPage = userMapper.selectPage(page, wrapper);

        // 批量查询 UserProfile
        List<Long> userIds = entityPage.getRecords().stream()
                .map(UserEntity::getId)
                .collect(Collectors.toList());

        final Map<Long, UserProfile> profileMap;
        if (!userIds.isEmpty()) {
            LambdaQueryWrapper<UserProfile> profileWrapper = new LambdaQueryWrapper<>();
            profileWrapper.in(UserProfile::getUserId, userIds);
            List<UserProfile> profiles = userProfileMapper.selectList(profileWrapper);
            profileMap = profiles.stream()
                    .collect(Collectors.toMap(UserProfile::getUserId, Function.identity()));
        } else {
            profileMap = Collections.emptyMap();
        }

        List<AdminUserVO> voList = entityPage.getRecords().stream().map(user -> {
            AdminUserVO vo = new AdminUserVO();
            BeanUtils.copyProperties(user, vo);
            vo.setRoleName(ROLE_MAP.getOrDefault(user.getRole(), "未知"));
            vo.setStatusName(STATUS_MAP.getOrDefault(user.getStatus(), "未知"));
            UserProfile profile = profileMap.get(user.getId());
            if (profile != null) {
                vo.setRealName(profile.getRealName());
                vo.setSchoolCode(profile.getSchoolCode());
                vo.setClassName(profile.getClassName());
                vo.setOccupationType(profile.getOccupationType());
            }
            return vo;
        }).collect(Collectors.toList());

        Page<AdminUserVO> voPage = new Page<>(queryDTO.getPageNo(), queryDTO.getPageSize(), entityPage.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public AdminUserVO getUserDetail(Long userId) {
        UserEntity user = userMapper.selectById(userId);
        if (user == null || user.getDelFlag() == 1) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        AdminUserVO vo = new AdminUserVO();
        BeanUtils.copyProperties(user, vo);
        vo.setRoleName(ROLE_MAP.getOrDefault(user.getRole(), "未知"));
        vo.setStatusName(STATUS_MAP.getOrDefault(user.getStatus(), "未知"));

        UserProfile profile = userProfileMapper.selectOne(
                new LambdaQueryWrapper<UserProfile>().eq(UserProfile::getUserId, userId)
        );
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
    public void updateStatus(UserStatusUpdateDTO dto) {
        UserEntity user = userMapper.selectById(dto.getUserId());
        if (user == null || user.getDelFlag() == 1) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        user.setStatus(dto.getStatus());
        userMapper.updateById(user);
    }

    @Override
    @Transactional
    public void updateRole(UserRoleUpdateDTO dto) {
        UserEntity user = userMapper.selectById(dto.getUserId());
        if (user == null || user.getDelFlag() == 1) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        if (!ROLE_MAP.containsKey(dto.getRole())) {
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }
        user.setRole(dto.getRole());
        userMapper.updateById(user);
    }

    @Override
    @Transactional
    public void resetPassword(UserResetPwdDTO dto) {
        UserEntity user = userMapper.selectById(dto.getUserId());
        if (user == null || user.getDelFlag() == 1) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        String newPwd = StringUtils.hasText(dto.getNewPassword()) ? dto.getNewPassword() : "123456";
        user.setPasswordHash(Sha256Util.encrypt(newPwd));
        userMapper.updateById(user);
    }

    @Override
    @Transactional
    public Long addUser(UserAddDTO dto) {
        // 检查用户名唯一性
        LambdaQueryWrapper<UserEntity> usernameWrapper = new LambdaQueryWrapper<>();
        usernameWrapper.eq(UserEntity::getUsername, dto.getUsername());
        if (userMapper.selectCount(usernameWrapper) > 0) {
            throw new BusinessException(ErrorCode.USERNAME_EXIST);
        }

        // 检查手机号唯一性
        LambdaQueryWrapper<UserEntity> phoneWrapper = new LambdaQueryWrapper<>();
        phoneWrapper.eq(UserEntity::getPhone, dto.getPhone());
        if (userMapper.selectCount(phoneWrapper) > 0) {
            throw new BusinessException(ErrorCode.PHONE_EXIST);
        }

        UserEntity user = new UserEntity();
        user.setUsername(dto.getUsername());
        user.setPasswordHash(Sha256Util.encrypt(dto.getPassword()));
        user.setRole(dto.getRole());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setStatus(1); // 默认启用
        user.setDelFlag(0);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.insert(user);

        // 创建用户详细信息
        if (StringUtils.hasText(dto.getRealName()) || StringUtils.hasText(dto.getSchoolCode()) ||
                StringUtils.hasText(dto.getOccupationType())) {
            UserProfile profile = new UserProfile();
            profile.setUserId(user.getId());
            profile.setRealName(dto.getRealName());
            profile.setSchoolCode(dto.getSchoolCode());
            profile.setOccupationType(dto.getOccupationType());
            userProfileMapper.insert(profile);
        }

        log.info("管理员新增用户成功 - username: {}, role: {}", dto.getUsername(), dto.getRole());
        return user.getId();
    }
}