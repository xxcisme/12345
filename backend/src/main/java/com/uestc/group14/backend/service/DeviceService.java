package com.uestc.group14.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.uestc.group14.backend.dto.DeviceQueryDTO;
import com.uestc.group14.backend.dto.DeviceCreateDTO;
import com.uestc.group14.backend.dto.DeviceUpdateDTO;
import com.uestc.group14.backend.Entity.Device;

public interface DeviceService {
    IPage<Device> queryDevices(DeviceQueryDTO queryDTO);
    Device getDeviceDetail(Long id);
    Long createDevice(DeviceCreateDTO createDTO);
    Device updateDevice(DeviceUpdateDTO updateDTO);
    void deleteDevice(Long id);
}