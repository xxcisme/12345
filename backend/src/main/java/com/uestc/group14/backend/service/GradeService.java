package com.uestc.group14.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.uestc.group14.backend.dto.admin.GradeAddDTO;
import com.uestc.group14.backend.dto.admin.GradeUpdateDTO;
import com.uestc.group14.backend.vo.admin.GradeStatisticsVO;
import com.uestc.group14.backend.vo.admin.GradeVO;

public interface GradeService {
    IPage<GradeVO> listGrades(Integer pageNo, Integer pageSize, Long courseId, Long classId,
                              Integer status, String studentName);
    Long addGrade(GradeAddDTO dto);
    GradeVO updateGrade(GradeUpdateDTO dto);
    GradeVO publishGrade(Long id);
    GradeStatisticsVO getStatistics(Long courseId, Long experimentId, Long classId);
}