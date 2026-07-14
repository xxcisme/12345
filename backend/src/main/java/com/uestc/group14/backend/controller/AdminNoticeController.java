package com.uestc.group14.backend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.uestc.group14.backend.common.result.CommonResult;
import com.uestc.group14.backend.common.utils.JwtUtil;
import com.uestc.group14.backend.dto.NoticeCreateDTO;
import com.uestc.group14.backend.dto.NoticeQueryDTO;
import com.uestc.group14.backend.dto.NoticeUpdateDTO;
import com.uestc.group14.backend.service.NoticeService;
import com.uestc.group14.backend.vo.NoticeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin/notices")
@RequiredArgsConstructor
@Tag(name = "后台公告管理")
public class AdminNoticeController {

    private final NoticeService noticeService;
    private final JwtUtil jwtUtil;

    private String getCurrentEditor(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            String username = jwtUtil.getUsernameFromToken(token);
            if (username != null) {
                return username;
            }
        }
        return "admin";
    }

    @GetMapping
    @Operation(summary = "查询公告列表（后台）")
    public CommonResult<IPage<NoticeVO>> list(NoticeQueryDTO queryDTO) {
        IPage<NoticeVO> page = noticeService.pageNoticesAdmin(queryDTO);
        return CommonResult.success(page);
    }

    @PostMapping
    @Operation(summary = "新增公告")
    public CommonResult<Long> create(@Valid @RequestBody NoticeCreateDTO createDTO,
                                     HttpServletRequest request) {
        String editor = getCurrentEditor(request);
        Long id = noticeService.createNotice(createDTO, editor);
        return CommonResult.success(id);
    }

    @PutMapping
    @Operation(summary = "编辑公告")
    public CommonResult<NoticeVO> update(@Valid @RequestBody NoticeUpdateDTO updateDTO,
                                         HttpServletRequest request) {
        String editor = getCurrentEditor(request);
        NoticeVO vo = noticeService.updateNotice(updateDTO, editor);
        return CommonResult.success(vo);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除公告")
    public CommonResult<Void> delete(@PathVariable Long id) {
        noticeService.deleteNotice(id);
        return CommonResult.success();
    }
}