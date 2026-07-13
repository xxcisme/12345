package com.uestc.group14.backend.controller.user;

import com.uestc.group14.backend.common.result.CommonResult;
import com.uestc.group14.backend.common.utils.JwtUtil;
import com.uestc.group14.backend.dto.*;
import com.uestc.group14.backend.service.UserCenterService;
import com.uestc.group14.backend.vo.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserCenterController {

    private final UserCenterService userCenterService;
    private final JwtUtil jwtUtil;

    private Long getCurrentUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            throw new RuntimeException("未登录");
        }
        token = token.substring(7);
        return jwtUtil.getUserIdFromToken(token);
    }

    // 通用分页结果封装
    private <T> CommonResult<List<T>> buildPageResult(IPage<T> page) {
        CommonResult<List<T>> result = new CommonResult<>();
        result.setCode(200);
        result.setMsg("");
        result.setData(page.getRecords());
        result.setTotal(page.getTotal());
        return result;
    }

    // ==================== 1. 个人信息管理 ====================
    @GetMapping("/profile")
    public CommonResult<UserInfoVO> getProfile(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        return CommonResult.success(userCenterService.getProfile(userId));
    }

    @PutMapping("/profile")
    public CommonResult<Void> updateProfile(HttpServletRequest request, @Valid @RequestBody UpdateProfileDTO dto) {
        Long userId = getCurrentUserId(request);
        userCenterService.updateProfile(userId, dto);
        return CommonResult.success();
    }

    @PutMapping("/password")
    public CommonResult<Void> changePassword(HttpServletRequest request, @Valid @RequestBody ChangePasswordDTO dto) {
        Long userId = getCurrentUserId(request);
        userCenterService.changePassword(userId, dto);
        return CommonResult.success();
    }

    // ==================== 2. 收藏管理 ====================
    @GetMapping("/favorites")
    public CommonResult<List<FavoriteVO>> getFavorites(HttpServletRequest request,
                                                       @RequestParam(defaultValue = "1") Integer pageNo,
                                                       @RequestParam(defaultValue = "10") Integer pageSize,
                                                       @RequestParam(required = false) Integer resourceType) {
        Long userId = getCurrentUserId(request);
        IPage<FavoriteVO> page = userCenterService.getFavorites(userId, pageNo, pageSize, resourceType);
        return buildPageResult(page);
    }

    @PostMapping("/favorites")
    public CommonResult<Void> addFavorite(HttpServletRequest request, @Valid @RequestBody FavoriteAddDTO dto) {
        Long userId = getCurrentUserId(request);
        userCenterService.addFavorite(userId, dto.getResourceId());
        return CommonResult.success();
    }

    @DeleteMapping("/favorites/{resourceId}")
    public CommonResult<Void> removeFavorite(HttpServletRequest request, @PathVariable Long resourceId) {
        Long userId = getCurrentUserId(request);
        userCenterService.removeFavorite(userId, resourceId);
        return CommonResult.success();
    }

    // ==================== 3. 查看课程 ====================
    @GetMapping("/courses")
    public CommonResult<List<CourseVO>> getCourses(HttpServletRequest request,
                                                   @RequestParam(defaultValue = "1") Integer pageNo,
                                                   @RequestParam(defaultValue = "10") Integer pageSize,
                                                   @RequestParam(required = false) Integer status) {
        Long userId = getCurrentUserId(request);
        IPage<CourseVO> page = userCenterService.getCourses(userId, pageNo, pageSize, status);
        return buildPageResult(page);
    }

    // ==================== 4. 查看实验 ====================
    @GetMapping("/experiments")
    public CommonResult<List<ExperimentVO>> getExperiments(HttpServletRequest request,
                                                           @RequestParam(defaultValue = "1") Integer pageNo,
                                                           @RequestParam(defaultValue = "10") Integer pageSize,
                                                           @RequestParam(required = false) Integer status,
                                                           @RequestParam(required = false) Long courseId) {
        Long userId = getCurrentUserId(request);
        IPage<ExperimentVO> page = userCenterService.getExperiments(userId, pageNo, pageSize, status, courseId);
        return buildPageResult(page);
    }

    // ==================== 5. 通知推送 ====================
    @GetMapping("/messages")
    public CommonResult<List<MessageVO>> getMessages(HttpServletRequest request,
                                                     @RequestParam(defaultValue = "1") Integer pageNo,
                                                     @RequestParam(defaultValue = "10") Integer pageSize,
                                                     @RequestParam(required = false) Integer type,
                                                     @RequestParam(required = false) Boolean isRead) {
        Long userId = getCurrentUserId(request);
        IPage<MessageVO> page = userCenterService.getMessages(userId, pageNo, pageSize, type, isRead);
        return buildPageResult(page);
    }

    @PutMapping("/messages/read")
    public CommonResult<Void> markMessagesRead(HttpServletRequest request, @RequestBody MarkReadDTO dto) {
        Long userId = getCurrentUserId(request);
        userCenterService.markMessagesRead(userId, dto.getMessageIds());
        return CommonResult.success();
    }
}