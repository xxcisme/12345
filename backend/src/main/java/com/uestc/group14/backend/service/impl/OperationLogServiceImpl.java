package com.uestc.group14.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.uestc.group14.backend.Entity.OperationLog;
import com.uestc.group14.backend.common.exception.BusinessException;
import com.uestc.group14.backend.common.exception.ErrorCode;
import com.uestc.group14.backend.dao.OperationLogMapper;
import com.uestc.group14.backend.dto.admin.OperationLogQueryDTO;
import com.uestc.group14.backend.service.OperationLogService;
import com.uestc.group14.backend.vo.admin.OperationLogVO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OperationLogServiceImpl implements OperationLogService {

    private final OperationLogMapper operationLogMapper;

    @Override
    public IPage<OperationLogVO> listLogs(OperationLogQueryDTO queryDTO) {
        Page<OperationLog> page = new Page<>(queryDTO.getPageNo(), queryDTO.getPageSize());
        LambdaQueryWrapper<OperationLog> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(queryDTO.getUsername())) {
            wrapper.like(OperationLog::getUsername, queryDTO.getUsername());
        }
        if (StringUtils.hasText(queryDTO.getUserRole())) {
            wrapper.eq(OperationLog::getUserRole, queryDTO.getUserRole());
        }
        if (StringUtils.hasText(queryDTO.getAction())) {
            wrapper.like(OperationLog::getAction, queryDTO.getAction());
        }
        if (StringUtils.hasText(queryDTO.getStartTime())) {
            wrapper.ge(OperationLog::getCreateTime, queryDTO.getStartTime());
        }
        if (StringUtils.hasText(queryDTO.getEndTime())) {
            wrapper.le(OperationLog::getCreateTime, queryDTO.getEndTime());
        }
        wrapper.orderByDesc(OperationLog::getCreateTime);

        IPage<OperationLog> entityPage = operationLogMapper.selectPage(page, wrapper);
        List<OperationLogVO> voList = entityPage.getRecords().stream().map(this::convertToVO).collect(Collectors.toList());

        Page<OperationLogVO> voPage = new Page<>(queryDTO.getPageNo(), queryDTO.getPageSize(), entityPage.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public void exportLogs(OperationLogQueryDTO queryDTO, HttpServletResponse response) {
        try {
            // 不分页，导出全部符合条件的数据
            queryDTO.setPageSize(Integer.MAX_VALUE);
            IPage<OperationLogVO> page = listLogs(queryDTO);
            List<OperationLogVO> list = page.getRecords();

            response.setContentType("text/csv;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=operation_logs_" +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".csv");

            PrintWriter writer = response.getWriter();
            writer.println("ID,用户ID,账号,角色,操作内容,IP地址,操作时间");
            for (OperationLogVO vo : list) {
                writer.printf("%d,%d,%s,%s,%s,%s,%s%n",
                        vo.getId(), vo.getUserId(), vo.getUsername(), vo.getUserRole(),
                        vo.getAction(), vo.getIpAddress(),
                        vo.getCreateTime() != null ? vo.getCreateTime().toString() : "");
            }
            writer.flush();
        } catch (IOException e) {
            log.error("导出日志失败", e);
            throw new BusinessException(ErrorCode.LOG_EXPORT_FAILED);
        }
    }

    @Override
    public void log(Long userId, String userRole, String username, String action, String ipAddress) {
        OperationLog logEntity = new OperationLog();
        logEntity.setUserId(userId);
        logEntity.setUserRole(userRole);
        logEntity.setUsername(username);
        logEntity.setAction(action);
        logEntity.setIpAddress(ipAddress);
        logEntity.setCreateTime(LocalDateTime.now());
        operationLogMapper.insert(logEntity);
    }

    private OperationLogVO convertToVO(OperationLog entity) {
        OperationLogVO vo = new OperationLogVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }
}