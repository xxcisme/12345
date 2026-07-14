package com.uestc.group14.backend.controller;

import com.uestc.group14.backend.common.result.CommonResult;
import com.uestc.group14.backend.dto.LabCreateDTO;
import com.uestc.group14.backend.dto.LabUpdateDTO;
import com.uestc.group14.backend.Entity.Laboratory;
import com.uestc.group14.backend.service.LaboratoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/laboratories")
@RequiredArgsConstructor
@Tag(name = "后台实验室管理")
public class AdminLaboratoriesController {

    private final LaboratoryService laboratoryService;

    @PostMapping
    @Operation(summary = "新增实验室")
    public CommonResult<Long> create(@Valid @RequestBody LabCreateDTO createDTO) {
        Long id = laboratoryService.createLaboratory(createDTO);
        return CommonResult.success(id);
    }

    @PutMapping
    @Operation(summary = "编辑实验室")
    public CommonResult<Laboratory> update(@Valid @RequestBody LabUpdateDTO updateDTO) {
        Laboratory lab = laboratoryService.updateLaboratory(updateDTO);
        return CommonResult.success(lab);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除实验室")
    public CommonResult<Void> delete(@PathVariable Long id) {
        laboratoryService.deleteLaboratory(id);
        return CommonResult.success();
    }
}