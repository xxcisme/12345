package com.uestc.group14.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.uestc.group14.backend.dto.*;
import com.uestc.group14.backend.Entity.LabApplication;
import com.uestc.group14.backend.vo.LabApplicationVO;

public interface LabApplicationService {
    Long createApplication(LabApplicationCreateDTO createDTO, Long userId);
    IPage<LabApplicationVO> queryUserApplications(Long userId, LabApplicationQueryDTO queryDTO);
    IPage<LabApplicationVO> queryAdminApplications(AdminLabApplicationQueryDTO queryDTO);
    LabApplication auditApplication(Long id, AuditLabAppDTO auditDTO);
}