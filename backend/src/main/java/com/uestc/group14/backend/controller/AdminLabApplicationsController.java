package com.uestc.group14.backend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.uestc.group14.backend.common.result.CommonResult;
import com.uestc.group14.backend.dto.AdminLabApplicationQueryDTO;
import com.uestc.group14.backend.dto.AuditLabAppDTO;
import com.uestc.group14.backend.Entity.LabApplication;
import com.uestc.group14.backend.service.LabApplicationService;
import com.uestc.group14.backend.vo.LabApplicationVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/lab-applications")
@RequiredArgsConstructor
@Tag(name = "后台实验室申请审批")
public class AdminLabApplicationsController {

    private final LabApplicationService labApplicationService;

    @GetMapping
    @Operation(summary = "查询申请列表（后台）")
    public CommonResult<IPage<LabApplicationVO>> list(AdminLabApplicationQueryDTO queryDTO) {
        IPage<LabApplicationVO> page = labApplicationService.queryAdminApplications(queryDTO);
        return CommonResult.success(page);
    }

    @PutMapping("/{id}/audit")
    @Operation(summary = "审批申请")
    public CommonResult<LabApplication> audit(@PathVariable Long id,
                                              @Valid @RequestBody AuditLabAppDTO auditDTO) {
        LabApplication app = labApplicationService.auditApplication(id, auditDTO);
        return CommonResult.success(app);
    }
}