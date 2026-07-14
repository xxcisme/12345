package com.uestc.group14.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.uestc.group14.backend.dto.*;
import com.uestc.group14.backend.Entity.LabApplication;

public interface LabApplicationService {
    Long createApplication(LabApplicationCreateDTO createDTO, Long userId);
    IPage<LabApplication> queryUserApplications(Long userId, LabApplicationQueryDTO queryDTO);
    IPage<LabApplication> queryAdminApplications(AdminLabApplicationQueryDTO queryDTO);
    LabApplication auditApplication(Long id, AuditLabAppDTO auditDTO);
}