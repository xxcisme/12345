package com.uestc.group14.backend.controller.admin;

import com.uestc.group14.backend.common.result.CommonResult;
import com.uestc.group14.backend.service.SystemDashboardService;
import com.uestc.group14.backend.vo.admin.SystemDashboardVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin/dashboard")
@RequiredArgsConstructor
@Tag(name = "系统管理-系统概览", description = "仪表盘统计数据")
public class AdminDashboardController {

    private final SystemDashboardService systemDashboardService;

    @GetMapping
    @Operation(summary = "获取系统概览数据")
    public CommonResult<SystemDashboardVO> dashboard() {
        log.info("获取系统概览数据");
        return CommonResult.success(systemDashboardService.getDashboard());
    }
}