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
@Tag(name = "系统管理-用户管理")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    @Operation(summary = "查询用户列表")
    public CommonResult<IPage<AdminUserVO>> list(UserQueryDTO queryDTO) {
        log.info("查询用户列表 - username:{}", queryDTO.getUsername());
        return CommonResult.success(adminUserService.listUsers(queryDTO));
    }

    @GetMapping("/{userId}")
    @Operation(summary = "获取用户详情")
    public CommonResult<AdminUserVO> detail(@PathVariable Long userId) {
        return CommonResult.success(adminUserService.getUserDetail(userId));
    }

    @PostMapping
    @Operation(summary = "新增用户")
    public CommonResult<Long> addUser(@Valid @RequestBody UserAddDTO dto) {
        log.info("新增用户 - username:{}", dto.getUsername());
        return CommonResult.success(adminUserService.addUser(dto));
    }

    @PutMapping
    @Operation(summary = "编辑用户基本信息（含角色）")
    public CommonResult<Void> updateUser(@Valid @RequestBody UserUpdateDTO dto) {
        log.info("编辑用户 - userId:{}", dto.getId());
        adminUserService.updateUser(dto);
        return CommonResult.success();
    }

    @PutMapping("/{userId}/status")
    @Operation(summary = "切换用户状态（启用/停用）")
    public CommonResult<Void> updateStatus(@PathVariable Long userId,
                                           @Valid @RequestBody UserStatusUpdateDTO dto) {
        log.info("切换用户状态 - userId:{}, status:{}", userId, dto.getStatus());
        adminUserService.updateUserStatus(userId, dto.getStatus());
        return CommonResult.success();
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "删除用户（逻辑删除）")
    public CommonResult<Void> deleteUser(@PathVariable Long userId) {
        log.info("删除用户 - userId:{}", userId);
        adminUserService.deleteUser(userId);
        return CommonResult.success();
    }
}