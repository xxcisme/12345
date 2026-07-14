package com.uestc.group14.backend.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.uestc.group14.backend.common.result.CommonResult;
import com.uestc.group14.backend.dto.admin.*;
import com.uestc.group14.backend.service.AdminUserService;
import com.uestc.group14.backend.vo.admin.AdminUserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
@Tag(name = "系统管理-用户管理", description = "管理员对平台用户的统一管理")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    @Operation(summary = "查询用户列表")
    public CommonResult<IPage<AdminUserVO>> list(UserQueryDTO queryDTO) {
        log.info("查询用户列表 - username:{}, role:{}, status:{}", queryDTO.getUsername(), queryDTO.getRole(), queryDTO.getStatus());
        IPage<AdminUserVO> page = adminUserService.listUsers(queryDTO);
        return CommonResult.success(page);
    }

    @PostMapping
    @Operation(summary = "新增用户（管理员）")
    public CommonResult<Long> addUser(@Valid @RequestBody UserAddDTO dto) {
        log.info("管理员新增用户 - username: {}, role: {}", dto.getUsername(), dto.getRole());
        Long userId = adminUserService.addUser(dto);
        return CommonResult.success(userId);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "获取用户详情")
    public CommonResult<AdminUserVO> detail(@PathVariable Long userId) {
        log.info("获取用户详情 - userId:{}", userId);
        return CommonResult.success(adminUserService.getUserDetail(userId));
    }

    @PutMapping("/status")
    @Operation(summary = "启用/停用用户")
    public CommonResult<Void> updateStatus(@Valid @RequestBody UserStatusUpdateDTO dto) {
        log.info("更新用户状态 - userId:{}, status:{}", dto.getUserId(), dto.getStatus());
        adminUserService.updateStatus(dto);
        return CommonResult.success();
    }

    @PutMapping("/role")
    @Operation(summary = "修改用户角色")
    public CommonResult<Void> updateRole(@Valid @RequestBody UserRoleUpdateDTO dto) {
        log.info("更新用户角色 - userId:{}, role:{}", dto.getUserId(), dto.getRole());
        adminUserService.updateRole(dto);
        return CommonResult.success();
    }

    @PutMapping("/reset-password")
    @Operation(summary = "重置用户密码（默认123456）")
    public CommonResult<Void> resetPassword(@Valid @RequestBody UserResetPwdDTO dto) {
        log.info("重置用户密码 - userId:{}", dto.getUserId());
        adminUserService.resetPassword(dto);
        return CommonResult.success();
    }
}