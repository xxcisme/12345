package com.uestc.group14.backend.controller;

import com.uestc.group14.backend.common.result.CommonResult;
import com.uestc.group14.backend.common.utils.JwtUtil;
import com.uestc.group14.backend.dto.ResourceQueryDTO;
import com.uestc.group14.backend.dto.ScoreDTO;
import com.uestc.group14.backend.service.ResourceService;
import com.uestc.group14.backend.vo.ResourceVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/resources")
@RequiredArgsConstructor
@Tag(name = "前台资源管理")
public class ResourcesController {

    private final ResourceService resourceService;
    private final JwtUtil jwtUtil;

    private Long getCurrentUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            return jwtUtil.getUserIdFromToken(token);
        }
        return null;
    }

    @GetMapping
    @Operation(summary = "查询资源列表（前台）")
    public CommonResult<IPage<ResourceVO>> list(ResourceQueryDTO queryDTO) {
        IPage<ResourceVO> page = resourceService.queryFrontResources(queryDTO);
        return CommonResult.success(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取资源详情")
    public CommonResult<ResourceVO> detail(@PathVariable Long id) {
        ResourceVO vo = resourceService.getResourceDetail(id);
        return CommonResult.success(vo);
    }

    @PostMapping("/{id}/score")
    @Operation(summary = "对资源评分")
    public CommonResult<Map<String, Object>> score(@PathVariable Long id,
                                                   @Valid @RequestBody ScoreDTO scoreDTO,
                                                   HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        if (userId == null) {
            return CommonResult.error(401, "请先登录");
        }
        Map<String, Object> result = resourceService.scoreResource(userId, id, scoreDTO.getScore());
        return CommonResult.success(result);
    }
}