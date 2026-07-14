package com.uestc.group14.backend.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.uestc.group14.backend.common.result.CommonResult;
import com.uestc.group14.backend.dto.admin.OperationLogCleanDTO;
import com.uestc.group14.backend.dto.admin.OperationLogQueryDTO;
import com.uestc.group14.backend.service.OperationLogService;
import com.uestc.group14.backend.vo.admin.OperationLogVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin/logs")
@RequiredArgsConstructor
@Tag(name = "系统管理-操作日志")
public class AdminLogController {

    private final OperationLogService operationLogService;

    @GetMapping
    @Operation(summary = "查询操作日志列表")
    public CommonResult<IPage<OperationLogVO>> list(OperationLogQueryDTO queryDTO) {
        return CommonResult.success(operationLogService.listLogs(queryDTO));
    }

    @GetMapping("/export")
    @Operation(summary = "导出操作日志（CSV格式）")
    public void export(OperationLogQueryDTO queryDTO, HttpServletResponse response) {
        operationLogService.exportLogs(queryDTO, response);
    }

    @DeleteMapping("/clean")
    @Operation(summary = "清理过期日志（按保留天数）")
    public CommonResult<Integer> clean(@Valid @RequestBody OperationLogCleanDTO dto) {
        int deleted = operationLogService.cleanLogs(dto.getDaysToKeep());
        return CommonResult.success(deleted);
    }
}