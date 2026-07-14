package com.uestc.group14.backend.controller;

import com.uestc.group14.backend.common.result.CommonResult;
import com.uestc.group14.backend.common.utils.JwtUtil;
import com.uestc.group14.backend.dto.*;
import com.uestc.group14.backend.Entity.ResourceEntity;
import com.uestc.group14.backend.service.ResourceService;
import com.uestc.group14.backend.vo.AdminResourceVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin/resources")
@RequiredArgsConstructor
@Tag(name = "后台资源管理")
public class AdminResourcesController {

    private final ResourceService resourceService;
    private final JwtUtil jwtUtil;

    private Long getCurrentUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            return jwtUtil.getUserIdFromToken(token);
        }
        throw new RuntimeException("未登录");
    }

    @GetMapping
    @Operation(summary = "查询资源列表（后台）")
    public CommonResult<IPage<AdminResourceVO>> list(AdminResourceQueryDTO queryDTO) {
        IPage<AdminResourceVO> page = resourceService.queryAdminResources(queryDTO);
        return CommonResult.success(page);
    }

    @PostMapping
    @Operation(summary = "新增资源（后台）")
    public CommonResult<Long> create(@Valid @RequestPart("resource") ResourceCreateDTO createDTO,
                                     @RequestPart(value = "file", required = true) MultipartFile file,
                                     HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        Long id = resourceService.createResource(createDTO, file, userId);
        return CommonResult.success(id);
    }

    @PutMapping
    @Operation(summary = "编辑资源（后台）")
    public CommonResult<ResourceEntity> update(@Valid @RequestBody ResourceUpdateDTO updateDTO) {
        ResourceEntity entity = resourceService.updateResource(updateDTO);
        return CommonResult.success(entity);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除资源（后台）")
    public CommonResult<Void> delete(@PathVariable Long id) {
        resourceService.deleteResource(id);
        return CommonResult.success();
    }

    @PutMapping("/{id}/audit")
    @Operation(summary = "审核资源（后台）")
    public CommonResult<ResourceEntity> audit(@PathVariable Long id,
                                              @Valid @RequestBody AuditDTO auditDTO) {
        ResourceEntity entity = resourceService.auditResource(id, auditDTO);
        return CommonResult.success(entity);
    }
}