package com.uestc.group14.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.uestc.group14.backend.dto.admin.ExperimentAddDTO;
import com.uestc.group14.backend.dto.admin.ExperimentUpdateDTO;
import com.uestc.group14.backend.vo.ExperimentVO;

public interface ExperimentService {
    IPage<ExperimentVO> listExperiments(Integer pageNo, Integer pageSize, String name, String number,
                                        Long courseId, Integer status);
    Long addExperiment(ExperimentAddDTO dto);
    ExperimentVO updateExperiment(ExperimentUpdateDTO dto);
    ExperimentVO publishExperiment(Long id);
    void deleteExperiment(Long id);
}