package com.uestc.group14.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.uestc.group14.backend.common.exception.BusinessException;
import com.uestc.group14.backend.common.exception.ErrorCode;
import com.uestc.group14.backend.dao.DeviceMapper;
import com.uestc.group14.backend.dto.DeviceQueryDTO;
import com.uestc.group14.backend.dto.DeviceCreateDTO;
import com.uestc.group14.backend.dto.DeviceUpdateDTO;
import com.uestc.group14.backend.Entity.Device;
import com.uestc.group14.backend.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements DeviceService {

    @Override
    public IPage<Device> queryDevices(DeviceQueryDTO queryDTO) {
        Page<Device> page = new Page<>(queryDTO.getPageNo(), queryDTO.getPageSize());
        LambdaQueryWrapper<Device> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Device::getDelFlag, 0);
        if (StringUtils.hasText(queryDTO.getName())) {
            wrapper.like(Device::getName, queryDTO.getName());
        }
        if (StringUtils.hasText(queryDTO.getNumber())) {
            wrapper.like(Device::getNumber, queryDTO.getNumber());
        }
        if (queryDTO.getLaboratoryId() != null) {
            wrapper.eq(Device::getLaboratoryId, queryDTO.getLaboratoryId());
        }
        if (StringUtils.hasText(queryDTO.getType())) {
            wrapper.eq(Device::getType, queryDTO.getType());
        }
        if (queryDTO.getStatus() != null) {
            wrapper.eq(Device::getStatus, queryDTO.getStatus());
        }
        wrapper.orderByDesc(Device::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public Device getDeviceDetail(Long id) {
        Device device = this.getById(id);
        if (device == null || device.getDelFlag() == 1) {
            throw new BusinessException(ErrorCode.DEVICE_NOT_FOUND);
        }
        return device;
    }

    @Override
    @Transactional
    public Long createDevice(DeviceCreateDTO createDTO) {
        // 编号唯一性
        LambdaQueryWrapper<Device> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Device::getNumber, createDTO.getNumber());
        if (this.count(wrapper) > 0) {
            throw new BusinessException(ErrorCode.DEVICE_NUMBER_EXIST);
        }
        Device entity = new Device();
        BeanUtils.copyProperties(createDTO, entity);
        entity.setStatus(0); // 默认空闲
        entity.setCreateTime(LocalDateTime.now());
        this.save(entity);
        return entity.getId();
    }

    @Override
    @Transactional
    public Device updateDevice(DeviceUpdateDTO updateDTO) {
        Device entity = this.getById(updateDTO.getId());
        if (entity == null || entity.getDelFlag() == 1) {
            throw new BusinessException(ErrorCode.DEVICE_NOT_FOUND);
        }
        if (StringUtils.hasText(updateDTO.getName())) entity.setName(updateDTO.getName());
        if (StringUtils.hasText(updateDTO.getType())) entity.setType(updateDTO.getType());
        if (StringUtils.hasText(updateDTO.getVersionNumber())) entity.setVersionNumber(updateDTO.getVersionNumber());
        if (updateDTO.getLaboratoryId() != null) entity.setLaboratoryId(updateDTO.getLaboratoryId());
        if (updateDTO.getStatus() != null) entity.setStatus(updateDTO.getStatus());
        entity.setUpdateTime(LocalDateTime.now());
        this.updateById(entity);
        return entity;
    }

    @Override
    @Transactional
    public void deleteDevice(Long id) {
        Device entity = this.getById(id);
        if (entity == null || entity.getDelFlag() == 1) {
            throw new BusinessException(ErrorCode.DEVICE_NOT_FOUND);
        }
        this.removeById(id);
    }
}