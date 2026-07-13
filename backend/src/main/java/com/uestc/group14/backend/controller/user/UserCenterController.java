package com.uestc.group14.backend.controller.user;

import com.uestc.group14.backend.common.result.CommonResult;
import com.uestc.group14.backend.common.utils.JwtUtil;
import com.uestc.group14.backend.dto.*;
import com.uestc.group14.backend.service.UserCenterService;
import com.uestc.group14.backend.vo.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "用户中心管理", description = "用户个人信息、收藏、课程、实验、通知等相关接口")
public class UserCenterController {

    @Autowired
    private UserCenterService userCenterService;

    @Autowired
    private JwtUtil jwtUtil;

    private Long getCurrentUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            throw new RuntimeException("未登录");
        }
        token = token.substring(7);
        return jwtUtil.getUserIdFromToken(token);
    }

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
    @Operation(summary = "获取个人信息")
    public CommonResult<UserInfoVO> getProfile(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        log.info("获取个人信息 - userId: {}", userId);
        return CommonResult.success(userCenterService.getProfile(userId));
    }

    @PutMapping("/profile")
    @Operation(summary = "更新个人信息")
    public CommonResult<UserInfoVO> updateProfile(HttpServletRequest request, @Valid @RequestBody UpdateProfileDTO dto) {
        Long userId = getCurrentUserId(request);
        log.info("更新个人信息 - userId: {}", userId);
        UserInfoVO updated = userCenterService.updateProfile(userId, dto);
        return CommonResult.success(updated);
    }

    @PutMapping("/password")
    @Operation(summary = "修改密码")
    public CommonResult<Void> changePassword(HttpServletRequest request, @Valid @RequestBody ChangePasswordDTO dto) {
        Long userId = getCurrentUserId(request);
        log.info("修改密码 - userId: {}", userId);
        userCenterService.changePassword(userId, dto);
        return CommonResult.success();
    }

    // ==================== 2. 收藏管理 ====================

    @GetMapping("/favorites")
    @Operation(summary = "获取收藏列表")
    public CommonResult<List<FavoriteVO>> getFavorites(HttpServletRequest request,
                                                       @RequestParam(defaultValue = "1") Integer pageNo,
                                                       @RequestParam(defaultValue = "10") Integer pageSize,
                                                       @RequestParam(required = false) Integer resourceType) {
        Long userId = getCurrentUserId(request);
        log.info("获取收藏列表 - userId: {}, pageNo: {}, pageSize: {}, resourceType: {}", userId, pageNo, pageSize, resourceType);
        IPage<FavoriteVO> page = userCenterService.getFavorites(userId, pageNo, pageSize, resourceType);
        return buildPageResult(page);
    }

    @PostMapping("/favorites")
    @Operation(summary = "添加收藏")
    public CommonResult<Long> addFavorite(HttpServletRequest request, @Valid @RequestBody FavoriteAddDTO dto) {
        Long userId = getCurrentUserId(request);
        log.info("添加收藏 - userId: {}, resourceId: {}", userId, dto.getResourceId());
        Long favoriteId = userCenterService.addFavorite(userId, dto.getResourceId());
        return CommonResult.success(favoriteId);
    }

    @DeleteMapping("/favorites/{resourceId}")
    @Operation(summary = "取消收藏")
    public CommonResult<Void> removeFavorite(HttpServletRequest request, @PathVariable Long resourceId) {
        Long userId = getCurrentUserId(request);
        log.info("取消收藏 - userId: {}, resourceId: {}", userId, resourceId);
        userCenterService.removeFavorite(userId, resourceId);
        return CommonResult.success();
    }

    // ==================== 3. 查看课程 ====================

    @GetMapping("/courses")
    @Operation(summary = "获取课程列表")
    public CommonResult<List<CourseVO>> getCourses(HttpServletRequest request,
                                                   @RequestParam(defaultValue = "1") Integer pageNo,
                                                   @RequestParam(defaultValue = "10") Integer pageSize,
                                                   @RequestParam(required = false) Integer status) {
        Long userId = getCurrentUserId(request);
        log.info("获取课程列表 - userId: {}, pageNo: {}, pageSize: {}, status: {}", userId, pageNo, pageSize, status);
        IPage<CourseVO> page = userCenterService.getCourses(userId, pageNo, pageSize, status);
        return buildPageResult(page);
    }

    // ==================== 4. 查看实验 ====================

    @GetMapping("/experiments")
    @Operation(summary = "获取实验列表")
    public CommonResult<List<ExperimentVO>> getExperiments(HttpServletRequest request,
                                                           @RequestParam(defaultValue = "1") Integer pageNo,
                                                           @RequestParam(defaultValue = "10") Integer pageSize,
                                                           @RequestParam(required = false) Integer status,
                                                           @RequestParam(required = false) Long courseId) {
        Long userId = getCurrentUserId(request);
        log.info("获取实验列表 - userId: {}, pageNo: {}, pageSize: {}, status: {}, courseId: {}", userId, pageNo, pageSize, status, courseId);
        IPage<ExperimentVO> page = userCenterService.getExperiments(userId, pageNo, pageSize, status, courseId);
        return buildPageResult(page);
    }

    // ==================== 5. 通知推送 ====================

    @GetMapping("/messages")
    @Operation(summary = "获取消息列表")
    public CommonResult<List<MessageVO>> getMessages(HttpServletRequest request,
                                                     @RequestParam(defaultValue = "1") Integer pageNo,
                                                     @RequestParam(defaultValue = "10") Integer pageSize,
                                                     @RequestParam(required = false) Integer type,
                                                     @RequestParam(required = false) Boolean isRead) {
        Long userId = getCurrentUserId(request);
        log.info("获取消息列表 - userId: {}, pageNo: {}, pageSize: {}, type: {}, isRead: {}", userId, pageNo, pageSize, type, isRead);
        IPage<MessageVO> page = userCenterService.getMessages(userId, pageNo, pageSize, type, isRead);
        return buildPageResult(page);
    }

    @PutMapping("/messages/read")
    @Operation(summary = "标记消息已读")
    public CommonResult<Integer> markMessagesRead(HttpServletRequest request, @Valid @RequestBody MarkReadDTO dto) {
        Long userId = getCurrentUserId(request);
        log.info("标记消息已读 - userId: {}, messageIds: {}", userId, dto.getMessageIds());
        int count = userCenterService.markMessagesRead(userId, dto.getMessageIds());
        return CommonResult.success(count);
    }

    // ==================== 6. 课程详情 & 实验详情 ====================

    @GetMapping("/courses/{courseId}")
    @Operation(summary = "获取课程详情")
    public CommonResult<CourseVO> getCourseDetail(HttpServletRequest request,
                                                  @PathVariable Long courseId) {
        Long userId = getCurrentUserId(request);
        log.info("获取课程详情 - userId: {}, courseId: {}", userId, courseId);
        return CommonResult.success(userCenterService.getCourseDetail(userId, courseId));
    }

    @GetMapping("/experiments/{experimentId}")
    @Operation(summary = "获取实验详情")
    public CommonResult<ExperimentVO> getExperimentDetail(HttpServletRequest request,
                                                          @PathVariable Long experimentId) {
        Long userId = getCurrentUserId(request);
        log.info("获取实验详情 - userId: {}, experimentId: {}", userId, experimentId);
        return CommonResult.success(userCenterService.getExperimentDetail(userId, experimentId));
    }
}