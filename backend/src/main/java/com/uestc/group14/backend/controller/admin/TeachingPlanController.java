package com.uestc.group14.backend.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.uestc.group14.backend.common.result.CommonResult;
import com.uestc.group14.backend.dto.admin.TeachingPlanAddDTO;
import com.uestc.group14.backend.dto.admin.TeachingPlanUpdateDTO;
import com.uestc.group14.backend.service.TeachingPlanService;
import com.uestc.group14.backend.vo.admin.TeachingPlanVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin/teaching-plans")
@RequiredArgsConstructor
@Tag(name = "教学计划管理（后台）", description = "教师管理教学计划")
public class TeachingPlanController {

    private final TeachingPlanService teachingPlanService;

    @GetMapping
    @Operation(summary = "查询教学计划列表")
    public CommonResult<IPage<TeachingPlanVO>> listPlans(
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String semester,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long teacherId) {
        log.info("查询教学计划列表 - pageNo:{}, pageSize:{}, name:{}, semester:{}, status:{}, teacherId:{}",
                pageNo, pageSize, name, semester, status, teacherId);
        IPage<TeachingPlanVO> page = teachingPlanService.listPlans(pageNo, pageSize, name, semester, status, teacherId);
        return CommonResult.success(page);
    }

    @PostMapping
    @Operation(summary = "新增教学计划")
    public CommonResult<Long> addPlan(@Valid @RequestBody TeachingPlanAddDTO dto) {
        log.info("新增教学计划 - name:{}", dto.getName());
        Long id = teachingPlanService.addPlan(dto);
        return CommonResult.success(id);
    }

    @PutMapping
    @Operation(summary = "编辑教学计划")
    public CommonResult<TeachingPlanVO> updatePlan(@Valid @RequestBody TeachingPlanUpdateDTO dto) {
        log.info("编辑教学计划 - id:{}", dto.getId());
        TeachingPlanVO vo = teachingPlanService.updatePlan(dto);
        return CommonResult.success(vo);
    }

    @PutMapping("/{id}/publish")
    @Operation(summary = "发布教学计划")
    public CommonResult<TeachingPlanVO> publishPlan(@PathVariable Long id) {
        log.info("发布教学计划 - id:{}", id);
        TeachingPlanVO vo = teachingPlanService.publishPlan(id);
        return CommonResult.success(vo);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除教学计划")
    public CommonResult<Void> deletePlan(@PathVariable Long id) {
        log.info("删除教学计划 - id:{}", id);
        teachingPlanService.deletePlan(id);
        return CommonResult.success();
    }
}