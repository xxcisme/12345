package com.uestc.group14.backend.controller;

import com.uestc.group14.backend.common.result.CommonResult;
import com.uestc.group14.backend.dto.DeviceCreateDTO;
import com.uestc.group14.backend.dto.DeviceUpdateDTO;
import com.uestc.group14.backend.Entity.Device;
import com.uestc.group14.backend.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/devices")
@RequiredArgsConstructor
@Tag(name = "后台设备管理")
public class AdminDevicesController {

    private final DeviceService deviceService;

    @PostMapping
    @Operation(summary = "新增设备")
    public CommonResult<Long> create(@Valid @RequestBody DeviceCreateDTO createDTO) {
        Long id = deviceService.createDevice(createDTO);
        return CommonResult.success(id);
    }

    @PutMapping
    @Operation(summary = "编辑设备")
    public CommonResult<Device> update(@Valid @RequestBody DeviceUpdateDTO updateDTO) {
        Device device = deviceService.updateDevice(updateDTO);
        return CommonResult.success(device);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除设备")
    public CommonResult<Void> delete(@PathVariable Long id) {
        deviceService.deleteDevice(id);
        return CommonResult.success();
    }
}