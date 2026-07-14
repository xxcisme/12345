package com.uestc.group14.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.uestc.group14.backend.dto.LabQueryDTO;
import com.uestc.group14.backend.dto.LabCreateDTO;
import com.uestc.group14.backend.dto.LabUpdateDTO;
import com.uestc.group14.backend.Entity.Laboratory;

public interface LaboratoryService {
    IPage<Laboratory> queryLaboratories(LabQueryDTO queryDTO);
    Laboratory getLaboratoryDetail(Long id);
    Long createLaboratory(LabCreateDTO createDTO);
    Laboratory updateLaboratory(LabUpdateDTO updateDTO);
    void deleteLaboratory(Long id);
}