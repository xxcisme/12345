package com.uestc.group14.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.uestc.group14.backend.dto.admin.TeachingPlanAddDTO;
import com.uestc.group14.backend.dto.admin.TeachingPlanUpdateDTO;
import com.uestc.group14.backend.vo.admin.TeachingPlanVO;

public interface TeachingPlanService {
    IPage<TeachingPlanVO> listPlans(Integer pageNo, Integer pageSize, String name, String semester,
                                    Integer status, Long teacherId);
    Long addPlan(TeachingPlanAddDTO dto);
    TeachingPlanVO updatePlan(TeachingPlanUpdateDTO dto);
    TeachingPlanVO publishPlan(Long id);
    void deletePlan(Long id);
}