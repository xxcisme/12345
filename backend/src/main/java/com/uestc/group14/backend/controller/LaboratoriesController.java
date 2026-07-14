package com.uestc.group14.backend.controller;

import com.uestc.group14.backend.common.result.CommonResult;
import com.uestc.group14.backend.dto.LabQueryDTO;
import com.uestc.group14.backend.Entity.Laboratory;
import com.uestc.group14.backend.service.LaboratoryService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/laboratories")
@RequiredArgsConstructor
@Tag(name = "前台实验室", description = "实验室浏览")
public class LaboratoriesController {

    private final LaboratoryService laboratoryService;

    @GetMapping
    @Operation(summary = "查询实验室列表")
    public CommonResult<IPage<Laboratory>> list(LabQueryDTO queryDTO) {
        IPage<Laboratory> page = laboratoryService.queryLaboratories(queryDTO);
        return CommonResult.success(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取实验室详情")
    public CommonResult<Laboratory> detail(@PathVariable Long id) {
        Laboratory lab = laboratoryService.getLaboratoryDetail(id);
        return CommonResult.success(lab);
    }
}