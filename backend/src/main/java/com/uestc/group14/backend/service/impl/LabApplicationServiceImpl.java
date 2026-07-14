package com.uestc.group14.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.uestc.group14.backend.common.exception.BusinessException;
import com.uestc.group14.backend.common.exception.ErrorCode;
import com.uestc.group14.backend.dao.LabApplicationMapper;
import com.uestc.group14.backend.dao.UserMapper;
import com.uestc.group14.backend.dao.UserProfileMapper;
import com.uestc.group14.backend.dto.*;
import com.uestc.group14.backend.Entity.LabApplication;
import com.uestc.group14.backend.Entity.UserEntity;
import com.uestc.group14.backend.Entity.UserProfile;
import com.uestc.group14.backend.service.LabApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LabApplicationServiceImpl extends ServiceImpl<LabApplicationMapper, LabApplication> implements LabApplicationService {

    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;

    /**
     * 获取当前用户的真实姓名和手机号
     */
    private UserInfo getCurrentUserInfo(Long userId) {
        UserEntity user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        UserProfile profile = userProfileMapper.selectOne(
                new LambdaQueryWrapper<UserProfile>().eq(UserProfile::getUserId, userId)
        );
        if (profile == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        return new UserInfo(profile.getRealName(), user.getPhone());
    }

    private record UserInfo(String realName, String phone) {}

    @Override
    @Transactional
    public Long createApplication(LabApplicationCreateDTO createDTO, Long userId) {
        // 强制从当前用户获取姓名和手机号，覆盖前端传入的值
        UserInfo userInfo = getCurrentUserInfo(userId);
        // 检查时间冲突（示例简单判断，实际可查询已有申请）
        // 这里省略，可参考 SQL 查询

        LabApplication entity = new LabApplication();
        BeanUtils.copyProperties(createDTO, entity);
        // 强制设置申请人信息为当前登录用户
        entity.setApplicantName(userInfo.realName());
        entity.setContactPhone(userInfo.phone());
        entity.setNumber("APP-" + System.currentTimeMillis());
        entity.setStatus(0); // 待审批
        entity.setCreateTime(LocalDateTime.now());
        // 注意：如果表中有 update_time，需自动填充，由 MetaObjectHandler 处理
        this.save(entity);
        return entity.getId();
    }

    @Override
    public IPage<LabApplication> queryUserApplications(Long userId, LabApplicationQueryDTO queryDTO) {
        // 强制根据当前用户信息过滤
        UserInfo userInfo = getCurrentUserInfo(userId);

        Page<LabApplication> page = new Page<>(queryDTO.getPageNo(), queryDTO.getPageSize());
        LambdaQueryWrapper<LabApplication> wrapper = new LambdaQueryWrapper<>();
        // 强制添加申请人过滤条件：必须匹配当前用户的真实姓名或手机号（或两者都匹配，这里采用姓名+手机号组合）
        wrapper.eq(LabApplication::getApplicantName, userInfo.realName())
                .eq(LabApplication::getContactPhone, userInfo.phone());
        // 如果前端传了 status 过滤
        if (queryDTO.getStatus() != null) {
            wrapper.eq(LabApplication::getStatus, queryDTO.getStatus());
        }
        wrapper.orderByDesc(LabApplication::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public IPage<LabApplication> queryAdminApplications(AdminLabApplicationQueryDTO queryDTO) {
        Page<LabApplication> page = new Page<>(queryDTO.getPageNo(), queryDTO.getPageSize());
        LambdaQueryWrapper<LabApplication> wrapper = new LambdaQueryWrapper<>();
        if (queryDTO.getStatus() != null) {
            wrapper.eq(LabApplication::getStatus, queryDTO.getStatus());
        }
        if (queryDTO.getLabId() != null) {
            wrapper.eq(LabApplication::getLabId, queryDTO.getLabId());
        }
        wrapper.orderByDesc(LabApplication::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    @Transactional
    public LabApplication auditApplication(Long id, AuditLabAppDTO auditDTO) {
        LabApplication app = this.getById(id);
        if (app == null) {
            throw new BusinessException(ErrorCode.LAB_APPLICATION_NOT_FOUND);
        }
        if (app.getStatus() != 0) {
            throw new BusinessException(ErrorCode.LAB_APPLICATION_ALREADY_APPROVED);
        }
        app.setStatus(auditDTO.getStatus());
        app.setAuditRemark(auditDTO.getAuditRemark());
        app.setApprovalTime(LocalDateTime.now());
        this.updateById(app);
        return app;
    }
}