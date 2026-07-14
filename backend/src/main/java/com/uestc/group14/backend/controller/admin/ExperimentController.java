package com.uestc.group14.backend.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.uestc.group14.backend.common.result.CommonResult;
import com.uestc.group14.backend.dto.admin.ExperimentAddDTO;
import com.uestc.group14.backend.dto.admin.ExperimentUpdateDTO;
import com.uestc.group14.backend.service.ExperimentService;
import com.uestc.group14.backend.vo.ExperimentVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin/experiments")
@RequiredArgsConstructor
@Tag(name = "实验管理（后台）", description = "教师/管理员管理实验")
public class ExperimentController {

    private final ExperimentService experimentService;

    @GetMapping
    @Operation(summary = "查询实验列表")
    public CommonResult<IPage<ExperimentVO>> listExperiments(
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String number,
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) Integer status) {
        log.info("查询实验列表 - pageNo:{}, pageSize:{}, name:{}, number:{}, courseId:{}, status:{}",
                pageNo, pageSize, name, number, courseId, status);
        IPage<ExperimentVO> page = experimentService.listExperiments(pageNo, pageSize, name, number, courseId, status);
        return CommonResult.success(page);
    }

    @PostMapping
    @Operation(summary = "新增实验")
    public CommonResult<Long> addExperiment(@Valid @RequestBody ExperimentAddDTO dto) {
        log.info("新增实验 - number:{}", dto.getNumber());
        Long id = experimentService.addExperiment(dto);
        return CommonResult.success(id);
    }

    @PutMapping
    @Operation(summary = "编辑实验")
    public CommonResult<ExperimentVO> updateExperiment(@Valid @RequestBody ExperimentUpdateDTO dto) {
        log.info("编辑实验 - id:{}", dto.getId());
        ExperimentVO vo = experimentService.updateExperiment(dto);
        return CommonResult.success(vo);
    }

    @PutMapping("/{id}/publish")
    @Operation(summary = "发布实验")
    public CommonResult<ExperimentVO> publishExperiment(@PathVariable Long id) {
        log.info("发布实验 - id:{}", id);
        ExperimentVO vo = experimentService.publishExperiment(id);
        return CommonResult.success(vo);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除实验")
    public CommonResult<Void> deleteExperiment(@PathVariable Long id) {
        log.info("删除实验 - id:{}", id);
        experimentService.deleteExperiment(id);
        return CommonResult.success();
    }
}