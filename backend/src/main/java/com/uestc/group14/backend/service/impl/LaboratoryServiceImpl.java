package com.uestc.group14.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.uestc.group14.backend.common.exception.BusinessException;
import com.uestc.group14.backend.common.exception.ErrorCode;
import com.uestc.group14.backend.dao.LaboratoryMapper;
import com.uestc.group14.backend.dto.LabQueryDTO;
import com.uestc.group14.backend.dto.LabCreateDTO;
import com.uestc.group14.backend.dto.LabUpdateDTO;
import com.uestc.group14.backend.Entity.Laboratory;
import com.uestc.group14.backend.service.LaboratoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LaboratoryServiceImpl extends ServiceImpl<LaboratoryMapper, Laboratory> implements LaboratoryService {

    @Override
    public IPage<Laboratory> queryLaboratories(LabQueryDTO queryDTO) {
        Page<Laboratory> page = new Page<>(queryDTO.getPageNo(), queryDTO.getPageSize());
        LambdaQueryWrapper<Laboratory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Laboratory::getDelFlag, 0);
        if (StringUtils.hasText(queryDTO.getName())) {
            wrapper.like(Laboratory::getName, queryDTO.getName());
        }
        if (StringUtils.hasText(queryDTO.getNumber())) {
            wrapper.like(Laboratory::getNumber, queryDTO.getNumber());
        }
        if (StringUtils.hasText(queryDTO.getAddress())) {
            wrapper.like(Laboratory::getAddress, queryDTO.getAddress());
        }
        if (queryDTO.getMinStation() != null) {
            wrapper.ge(Laboratory::getStationNum, queryDTO.getMinStation());
        }
        wrapper.orderByDesc(Laboratory::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public Laboratory getLaboratoryDetail(Long id) {
        Laboratory lab = this.getById(id);
        if (lab == null || lab.getDelFlag() == 1) {
            throw new BusinessException(ErrorCode.LABORATORY_NOT_FOUND);
        }
        // 若需携带设备列表，可以在此查询，略
        return lab;
    }

    @Override
    @Transactional
    public Long createLaboratory(LabCreateDTO createDTO) {
        // 编号唯一性校验
        LambdaQueryWrapper<Laboratory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Laboratory::getNumber, createDTO.getNumber());
        if (this.count(wrapper) > 0) {
            throw new BusinessException(ErrorCode.LABORATORY_NUMBER_EXIST);
        }
        Laboratory entity = new Laboratory();
        BeanUtils.copyProperties(createDTO, entity);
        entity.setCreateTime(LocalDateTime.now());
        this.save(entity);
        return entity.getId();
    }

    @Override
    @Transactional
    public Laboratory updateLaboratory(LabUpdateDTO updateDTO) {
        Laboratory entity = this.getById(updateDTO.getId());
        if (entity == null || entity.getDelFlag() == 1) {
            throw new BusinessException(ErrorCode.LABORATORY_NOT_FOUND);
        }
        if (StringUtils.hasText(updateDTO.getName())) entity.setName(updateDTO.getName());
        if (updateDTO.getStationNum() != null) entity.setStationNum(updateDTO.getStationNum());
        if (updateDTO.getArea() != null) entity.setArea(updateDTO.getArea());
        if (StringUtils.hasText(updateDTO.getAddress())) entity.setAddress(updateDTO.getAddress());
        if (StringUtils.hasText(updateDTO.getProfile())) entity.setProfile(updateDTO.getProfile());
        if (StringUtils.hasText(updateDTO.getDescription())) entity.setDescription(updateDTO.getDescription());
        entity.setUpdateTime(LocalDateTime.now());
        this.updateById(entity);
        return entity;
    }

    @Override
    @Transactional
    public void deleteLaboratory(Long id) {
        Laboratory entity = this.getById(id);
        if (entity == null || entity.getDelFlag() == 1) {
            throw new BusinessException(ErrorCode.LABORATORY_NOT_FOUND);
        }
        // 检查是否有关联设备（实际可查询 res_device 表，略）
        // 若有设备，抛出 LABORATORY_IN_USE
        this.removeById(id); // 逻辑删除
    }
}