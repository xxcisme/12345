package com.uestc.group14.backend.controller;

import com.uestc.group14.backend.common.result.CommonResult;
import com.uestc.group14.backend.common.utils.JwtUtil;
import com.uestc.group14.backend.dto.LabApplicationCreateDTO;
import com.uestc.group14.backend.dto.LabApplicationQueryDTO;
import com.uestc.group14.backend.service.LabApplicationService;
import com.uestc.group14.backend.vo.LabApplicationVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/lab-applications")
@RequiredArgsConstructor
@Tag(name = "实验室申请（用户）")
public class LabApplicationsController {

    private final LabApplicationService labApplicationService;
    private final JwtUtil jwtUtil;

    private Long getCurrentUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            return jwtUtil.getUserIdFromToken(token);
        }
        throw new RuntimeException("未登录");
    }

    @PostMapping
    @Operation(summary = "提交实验室申请")
    public CommonResult<Long> create(@Valid @RequestBody LabApplicationCreateDTO createDTO,
                                     HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        Long id = labApplicationService.createApplication(createDTO, userId);
        return CommonResult.success(id);
    }

    @GetMapping
    @Operation(summary = "获取我的申请列表")
    public CommonResult<IPage<LabApplicationVO>> list(HttpServletRequest request,
                                                    LabApplicationQueryDTO queryDTO) {
        Long userId = getCurrentUserId(request);
        IPage<LabApplicationVO> page = labApplicationService.queryUserApplications(userId, queryDTO);
        return CommonResult.success(page);
    }
}