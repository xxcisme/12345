package com.uestc.group14.backend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.uestc.group14.backend.common.result.CommonResult;
import com.uestc.group14.backend.dto.NewsQueryDTO;
import com.uestc.group14.backend.service.NewsService;
import com.uestc.group14.backend.vo.NewsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
@Tag(name = "前台新闻", description = "新闻浏览")
public class NewsController {

    private final NewsService newsService;

    @GetMapping
    @Operation(summary = "查询新闻列表（前台）")
    public CommonResult<IPage<NewsVO>> list(NewsQueryDTO queryDTO) {
        IPage<NewsVO> page = newsService.pageNewsFront(queryDTO);
        return CommonResult.success(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取新闻详情")
    public CommonResult<NewsVO> detail(@PathVariable Long id) {
        NewsVO vo = newsService.getNewsDetail(id);
        return CommonResult.success(vo);
    }
}