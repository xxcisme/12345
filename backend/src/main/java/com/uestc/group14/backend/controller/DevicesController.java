package com.uestc.group14.backend.controller;

import com.uestc.group14.backend.common.result.CommonResult;
import com.uestc.group14.backend.dto.DeviceQueryDTO;
import com.uestc.group14.backend.Entity.Device;
import com.uestc.group14.backend.service.DeviceService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/devices")
@RequiredArgsConstructor
@Tag(name = "前台设备", description = "设备浏览")
public class DevicesController {

    private final DeviceService deviceService;

    @GetMapping
    @Operation(summary = "查询设备列表")
    public CommonResult<IPage<Device>> list(DeviceQueryDTO queryDTO) {
        IPage<Device> page = deviceService.queryDevices(queryDTO);
        return CommonResult.success(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取设备详情")
    public CommonResult<Device> detail(@PathVariable Long id) {
        Device device = deviceService.getDeviceDetail(id);
        return CommonResult.success(device);
    }
}