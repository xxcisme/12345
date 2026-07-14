package com.uestc.group14.backend.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.uestc.group14.backend.common.result.CommonResult;
import com.uestc.group14.backend.dto.admin.TeacherAddDTO;
import com.uestc.group14.backend.dto.admin.TeacherUpdateDTO;
import com.uestc.group14.backend.service.TeacherService;
import com.uestc.group14.backend.vo.admin.TeacherVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin/teachers")
@RequiredArgsConstructor
@Tag(name = "师资管理（后台）", description = "管理员管理教师信息")
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping
    @Operation(summary = "查询师资列表")
    public CommonResult<IPage<TeacherVO>> listTeachers(
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String teacherId,
            @RequestParam(required = false) String company,
            @RequestParam(required = false) Boolean onJob) {
        log.info("查询师资列表 - pageNo:{}, pageSize:{}, name:{}, teacherId:{}, company:{}, onJob:{}",
                pageNo, pageSize, name, teacherId, company, onJob);
        IPage<TeacherVO> page = teacherService.listTeachers(pageNo, pageSize, name, teacherId, company, onJob);
        return CommonResult.success(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取师资详情")
    public CommonResult<TeacherVO> getTeacherDetail(@PathVariable Long id) {
        log.info("获取师资详情 - id:{}", id);
        return CommonResult.success(teacherService.getTeacherDetail(id));
    }

    @PostMapping
    @Operation(summary = "新增师资")
    public CommonResult<Long> addTeacher(@Valid @RequestBody TeacherAddDTO dto) {
        log.info("新增师资 - teacherId:{}", dto.getTeacherId());
        Long id = teacherService.addTeacher(dto);
        return CommonResult.success(id);
    }

    @PutMapping
    @Operation(summary = "编辑师资")
    public CommonResult<TeacherVO> updateTeacher(@Valid @RequestBody TeacherUpdateDTO dto) {
        log.info("编辑师资 - id:{}", dto.getId());
        TeacherVO vo = teacherService.updateTeacher(dto);
        return CommonResult.success(vo);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除师资")
    public CommonResult<Void> deleteTeacher(@PathVariable Long id) {
        log.info("删除师资 - id:{}", id);
        teacherService.deleteTeacher(id);
        return CommonResult.success();
    }

    @PutMapping("/{id}/reset-password")
    @Operation(summary = "重置教师密码")
    public CommonResult<Void> resetPassword(@PathVariable Long id) {
        log.info("重置教师密码 - id:{}", id);
        teacherService.resetPassword(id);
        return CommonResult.success();
    }
}