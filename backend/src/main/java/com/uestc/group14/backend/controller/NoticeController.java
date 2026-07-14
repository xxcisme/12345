package com.uestc.group14.backend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.uestc.group14.backend.common.result.CommonResult;
import com.uestc.group14.backend.dto.NoticeQueryDTO;
import com.uestc.group14.backend.service.NoticeService;
import com.uestc.group14.backend.vo.NoticeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notices")
@RequiredArgsConstructor
@Tag(name = "前台公告", description = "公告浏览")
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping
    @Operation(summary = "查询公告列表（前台）")
    public CommonResult<IPage<NoticeVO>> list(NoticeQueryDTO queryDTO) {
        IPage<NoticeVO> page = noticeService.pageNoticesFront(queryDTO);
        return CommonResult.success(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取公告详情")
    public CommonResult<NoticeVO> detail(@PathVariable Long id) {
        NoticeVO vo = noticeService.getNoticeDetail(id);
        return CommonResult.success(vo);
    }
}