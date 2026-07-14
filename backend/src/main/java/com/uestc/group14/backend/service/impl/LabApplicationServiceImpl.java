package com.uestc.group14.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.uestc.group14.backend.common.exception.BusinessException;
import com.uestc.group14.backend.common.exception.ErrorCode;
import com.uestc.group14.backend.dao.LabApplicationMapper;
import com.uestc.group14.backend.dto.*;
import com.uestc.group14.backend.Entity.LabApplication;
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

    @Override
    @Transactional
    public Long createApplication(LabApplicationCreateDTO createDTO, Long userId) {
        // 简单检查时间冲突暂略
        LabApplication entity = new LabApplication();
        BeanUtils.copyProperties(createDTO, entity);
        entity.setNumber("APP-" + System.currentTimeMillis());
        entity.setStatus(0); // 待审批
        entity.setCreateTime(LocalDateTime.now());
        // 注意：表里没有 user_id，所以无法关联用户，只存申请人姓名等
        this.save(entity);
        return entity.getId();
    }

    @Override
    public IPage<LabApplication> queryUserApplications(Long userId, LabApplicationQueryDTO queryDTO) {
        // 由于没有 user_id 字段，这里无法按用户过滤，简单返回所有（待完善）
        Page<LabApplication> page = new Page<>(queryDTO.getPageNo(), queryDTO.getPageSize());
        LambdaQueryWrapper<LabApplication> wrapper = new LambdaQueryWrapper<>();
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