package com.uestc.group14.backend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.uestc.group14.backend.common.result.CommonResult;
import com.uestc.group14.backend.common.utils.JwtUtil;
import com.uestc.group14.backend.dto.NewsCreateDTO;
import com.uestc.group14.backend.dto.NewsQueryDTO;
import com.uestc.group14.backend.dto.NewsUpdateDTO;
import com.uestc.group14.backend.service.NewsService;
import com.uestc.group14.backend.vo.NewsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin/news")
@RequiredArgsConstructor
@Tag(name = "后台新闻管理")
public class AdminNewsController {

    private final NewsService newsService;
    private final JwtUtil jwtUtil;

    /**
     * 获取当前登录用户名（用作编辑人）
     */
    private String getCurrentEditor(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            String username = jwtUtil.getUsernameFromToken(token);
            if (username != null) {
                return username;
            }
        }
        return "admin"; // 默认
    }

    @GetMapping
    @Operation(summary = "查询新闻列表（后台）")
    public CommonResult<IPage<NewsVO>> list(NewsQueryDTO queryDTO) {
        IPage<NewsVO> page = newsService.pageNewsAdmin(queryDTO);
        return CommonResult.success(page);
    }

    @PostMapping
    @Operation(summary = "新增新闻")
    public CommonResult<Long> create(@Valid @RequestBody NewsCreateDTO createDTO,
                                     HttpServletRequest request) {
        String editor = getCurrentEditor(request);
        Long id = newsService.createNews(createDTO, editor);
        return CommonResult.success(id);
    }

    @PutMapping
    @Operation(summary = "编辑新闻")
    public CommonResult<NewsVO> update(@Valid @RequestBody NewsUpdateDTO updateDTO,
                                       HttpServletRequest request) {
        String editor = getCurrentEditor(request);
        NewsVO vo = newsService.updateNews(updateDTO, editor);
        return CommonResult.success(vo);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除新闻")
    public CommonResult<Void> delete(@PathVariable Long id) {
        newsService.deleteNews(id);
        return CommonResult.success();
    }
}