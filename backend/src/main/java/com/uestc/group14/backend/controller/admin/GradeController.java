package com.uestc.group14.backend.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.uestc.group14.backend.common.result.CommonResult;
import com.uestc.group14.backend.dto.admin.GradeAddDTO;
import com.uestc.group14.backend.dto.admin.GradeUpdateDTO;
import com.uestc.group14.backend.service.GradeService;
import com.uestc.group14.backend.vo.admin.GradeStatisticsVO;
import com.uestc.group14.backend.vo.admin.GradeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin/grades")
@RequiredArgsConstructor
@Tag(name = "成绩评定（后台）", description = "教师评定成绩")
public class GradeController {

    private final GradeService gradeService;

    @GetMapping
    @Operation(summary = "查询成绩评定列表")
    public CommonResult<IPage<GradeVO>> listGrades(
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) Long classId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String studentName) {
        log.info("查询成绩评定列表 - pageNo:{}, pageSize:{}, courseId:{}, classId:{}, status:{}, studentName:{}",
                pageNo, pageSize, courseId, classId, status, studentName);
        IPage<GradeVO> page = gradeService.listGrades(pageNo, pageSize, courseId, classId, status, studentName);
        return CommonResult.success(page);
    }

    @PostMapping
    @Operation(summary = "评定成绩")
    public CommonResult<Long> addGrade(@Valid @RequestBody GradeAddDTO dto) {
        log.info("评定成绩 - scheduleId:{}, studentId:{}", dto.getScheduleId(), dto.getStudentId());
        Long id = gradeService.addGrade(dto);
        return CommonResult.success(id);
    }

    @PutMapping
    @Operation(summary = "修改成绩")
    public CommonResult<GradeVO> updateGrade(@Valid @RequestBody GradeUpdateDTO dto) {
        log.info("修改成绩 - id:{}", dto.getId());
        GradeVO vo = gradeService.updateGrade(dto);
        return CommonResult.success(vo);
    }

    @PutMapping("/{id}/publish")
    @Operation(summary = "发布成绩")
    public CommonResult<GradeVO> publishGrade(@PathVariable Long id) {
        log.info("发布成绩 - id:{}", id);
        GradeVO vo = gradeService.publishGrade(id);
        return CommonResult.success(vo);
    }

    @GetMapping("/statistics")
    @Operation(summary = "成绩统计")
    public CommonResult<GradeStatisticsVO> statistics(
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) Long experimentId,
            @RequestParam(required = false) Long classId) {
        log.info("成绩统计 - courseId:{}, experimentId:{}, classId:{}", courseId, experimentId, classId);
        GradeStatisticsVO stats = gradeService.getStatistics(courseId, experimentId, classId);
        return CommonResult.success(stats);
    }
}