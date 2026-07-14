package com.uestc.group14.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.uestc.group14.backend.dto.admin.OperationLogQueryDTO;
import com.uestc.group14.backend.vo.admin.OperationLogVO;
import jakarta.servlet.http.HttpServletResponse;

public interface OperationLogService {
    IPage<OperationLogVO> listLogs(OperationLogQueryDTO queryDTO);
    void exportLogs(OperationLogQueryDTO queryDTO, HttpServletResponse response);
    void log(Long userId, String userRole, String username, String action, String ipAddress);
    int cleanLogs(Integer daysToKeep);  // 新增
}