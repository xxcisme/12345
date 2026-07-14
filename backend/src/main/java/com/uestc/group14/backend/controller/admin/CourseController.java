package com.uestc.group14.backend.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.uestc.group14.backend.common.result.CommonResult;
import com.uestc.group14.backend.dto.admin.CourseAddDTO;
import com.uestc.group14.backend.dto.admin.CourseUpdateDTO;
import com.uestc.group14.backend.service.CourseService;
import com.uestc.group14.backend.vo.CourseVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin/courses")
@RequiredArgsConstructor
@Tag(name = "课程管理（后台）", description = "教师/管理员管理课程")
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    @Operation(summary = "查询课程列表")
    public CommonResult<IPage<CourseVO>> listCourses(
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String courseName,
            @RequestParam(required = false) String courseCode,
            @RequestParam(required = false) String courseType,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long teacherId) {
        log.info("查询课程列表 - pageNo:{}, pageSize:{}, courseName:{}, courseCode:{}, status:{}, teacherId:{}",
                pageNo, pageSize, courseName, courseCode, status, teacherId);
        IPage<CourseVO> page = courseService.listCourses(pageNo, pageSize, courseName, courseCode, courseType, status, teacherId);
        return CommonResult.success(page);
    }

    @PostMapping
    @Operation(summary = "新增课程")
    public CommonResult<Long> addCourse(@Valid @RequestBody CourseAddDTO dto) {
        log.info("新增课程 - courseCode:{}", dto.getCourseCode());
        Long id = courseService.addCourse(dto);
        return CommonResult.success(id);
    }

    @PutMapping
    @Operation(summary = "编辑课程")
    public CommonResult<CourseVO> updateCourse(@Valid @RequestBody CourseUpdateDTO dto) {
        log.info("编辑课程 - id:{}", dto.getId());
        CourseVO vo = courseService.updateCourse(dto);
        return CommonResult.success(vo);
    }

    @PutMapping("/{id}/publish")
    @Operation(summary = "发布课程")
    public CommonResult<CourseVO> publishCourse(@PathVariable Long id) {
        log.info("发布课程 - id:{}", id);
        CourseVO vo = courseService.publishCourse(id);
        return CommonResult.success(vo);
    }

    @PutMapping("/{id}/unpublish")
    @Operation(summary = "下架课程")
    public CommonResult<CourseVO> unpublishCourse(@PathVariable Long id) {
        log.info("下架课程 - id:{}", id);
        CourseVO vo = courseService.unpublishCourse(id);
        return CommonResult.success(vo);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除课程")
    public CommonResult<Void> deleteCourse(@PathVariable Long id) {
        log.info("删除课程 - id:{}", id);
        courseService.deleteCourse(id);
        return CommonResult.success();
    }
}