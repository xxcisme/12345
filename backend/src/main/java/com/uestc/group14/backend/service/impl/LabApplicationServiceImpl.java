package com.uestc.group14.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.uestc.group14.backend.common.exception.BusinessException;
import com.uestc.group14.backend.common.exception.ErrorCode;
import com.uestc.group14.backend.dao.LabApplicationMapper;
import com.uestc.group14.backend.dao.LaboratoryMapper;
import com.uestc.group14.backend.dao.UserMapper;
import com.uestc.group14.backend.dao.UserMessageMapper;
import com.uestc.group14.backend.dao.UserProfileMapper;
import com.uestc.group14.backend.dto.*;
import com.uestc.group14.backend.Entity.LabApplication;
import com.uestc.group14.backend.Entity.Laboratory;
import com.uestc.group14.backend.Entity.UserEntity;
import com.uestc.group14.backend.Entity.UserMessage;
import com.uestc.group14.backend.Entity.UserProfile;
import com.uestc.group14.backend.service.LabApplicationService;
import com.uestc.group14.backend.vo.LabApplicationVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LabApplicationServiceImpl extends ServiceImpl<LabApplicationMapper, LabApplication> implements LabApplicationService {

    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;
    private final LaboratoryMapper laboratoryMapper;
    private final UserMessageMapper userMessageMapper;

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
        UserInfo userInfo = getCurrentUserInfo(userId);

        LabApplication entity = new LabApplication();
        BeanUtils.copyProperties(createDTO, entity);
        entity.setApplicantName(userInfo.realName());
        entity.setContactPhone(userInfo.phone());
        entity.setNumber("APP-" + System.currentTimeMillis());
        entity.setStatus(0);
        entity.setCreateTime(LocalDateTime.now());
        this.save(entity);
        return entity.getId();
    }

    @Override
    public IPage<LabApplicationVO> queryUserApplications(Long userId, LabApplicationQueryDTO queryDTO) {
        UserInfo userInfo = getCurrentUserInfo(userId);

        Page<LabApplication> page = new Page<>(queryDTO.getPageNo(), queryDTO.getPageSize());
        LambdaQueryWrapper<LabApplication> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LabApplication::getApplicantName, userInfo.realName())
                .eq(LabApplication::getContactPhone, userInfo.phone());
        if (queryDTO.getStatus() != null) {
            wrapper.eq(LabApplication::getStatus, queryDTO.getStatus());
        }
        wrapper.orderByDesc(LabApplication::getCreateTime);
        IPage<LabApplication> resultPage = this.page(page, wrapper);
        
        return convertToVO(resultPage);
    }

    @Override
    public IPage<LabApplicationVO> queryAdminApplications(AdminLabApplicationQueryDTO queryDTO) {
        Page<LabApplication> page = new Page<>(queryDTO.getPageNo(), queryDTO.getPageSize());
        LambdaQueryWrapper<LabApplication> wrapper = new LambdaQueryWrapper<>();
        if (queryDTO.getStatus() != null) {
            wrapper.eq(LabApplication::getStatus, queryDTO.getStatus());
        }
        if (queryDTO.getLabId() != null) {
            wrapper.eq(LabApplication::getLabId, queryDTO.getLabId());
        }
        wrapper.orderByDesc(LabApplication::getCreateTime);
        IPage<LabApplication> resultPage = this.page(page, wrapper);
        
        return convertToVO(resultPage);
    }

    private IPage<LabApplicationVO> convertToVO(IPage<LabApplication> page) {
        List<LabApplication> records = page.getRecords();
        
        List<Long> labIds = records.stream()
                .map(LabApplication::getLabId)
                .distinct()
                .collect(Collectors.toList());
        
        Map<Long, String> labNameMap = new HashMap<>();
        if (!labIds.isEmpty()) {
            List<Laboratory> labs = laboratoryMapper.selectBatchIds(labIds);
            for (Laboratory lab : labs) {
                labNameMap.put(lab.getId(), lab.getName());
            }
        }
        
        List<LabApplicationVO> voList = records.stream()
                .map(app -> {
                    LabApplicationVO vo = new LabApplicationVO();
                    BeanUtils.copyProperties(app, vo);
                    vo.setLabName(labNameMap.getOrDefault(app.getLabId(), ""));
                    return vo;
                })
                .collect(Collectors.toList());
        
        IPage<LabApplicationVO> voPage = new Page<>();
        voPage.setRecords(voList);
        voPage.setTotal(page.getTotal());
        voPage.setPages(page.getPages());
        voPage.setCurrent(page.getCurrent());
        voPage.setSize(page.getSize());
        
        return voPage;
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
        
        sendAuditMessage(app);
        
        return app;
    }
    
    private void sendAuditMessage(LabApplication app) {
        LambdaQueryWrapper<UserProfile> profileWrapper = new LambdaQueryWrapper<>();
        profileWrapper.eq(UserProfile::getRealName, app.getApplicantName());
        UserProfile profile = userProfileMapper.selectOne(profileWrapper);
        
        if (profile == null) {
            return;
        }
        
        UserMessage message = new UserMessage();
        message.setUserId(profile.getUserId());
        message.setType(1);
        
        if (app.getStatus() == 1) {
            message.setTitle("审核通过");
            message.setContent("您的实验室申请（编号：" + app.getNumber() + "）已通过审核。");
        } else if (app.getStatus() == 2) {
            message.setTitle("审核拒绝");
            String remark = app.getAuditRemark() != null ? app.getAuditRemark() : "无";
            message.setContent("您的实验室申请（编号：" + app.getNumber() + "）未通过审核，原因：" + remark + "。");
        }
        
        message.setIsRead(0);
        message.setCreateTime(LocalDateTime.now());
        userMessageMapper.insert(message);
    }
}