package com.uestc.group14.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.uestc.group14.backend.Entity.OperationLog;
import com.uestc.group14.backend.Entity.ResourceEntity;
import com.uestc.group14.backend.Entity.UserEntity;
import com.uestc.group14.backend.dao.*;
import com.uestc.group14.backend.service.SystemDashboardService;
import com.uestc.group14.backend.vo.admin.SystemDashboardVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SystemDashboardServiceImpl implements SystemDashboardService {

    private final UserMapper userMapper;
    private final ResourceMapper resourceMapper;
    private final CourseMapper courseMapper;
    private final LabApplicationMapper labApplicationMapper;
    private final OperationLogMapper operationLogMapper;

    // 角色名称映射
    private String getRoleName(Integer role) {
        if (role == null) return "未知";
        switch (role) {
            case 1: return "学生";
            case 2: return "老师";
            case 3: return "社会人士";
            case 4: return "管理员";
            default: return "未知";
        }
    }

    @Override
    public SystemDashboardVO getDashboard() {
        SystemDashboardVO vo = new SystemDashboardVO();

        try {
            // 1. 总用户数（未删除）
            LambdaQueryWrapper<UserEntity> userWrapper = new LambdaQueryWrapper<>();
            userWrapper.eq(UserEntity::getDelFlag, 0);
            vo.setTotalUsers(userMapper.selectCount(userWrapper));

            // 2. 总资源数（未删除）
            LambdaQueryWrapper<ResourceEntity> resWrapper = new LambdaQueryWrapper<>();
            resWrapper.eq(ResourceEntity::getDelFlag, 0);
            vo.setTotalResources(resourceMapper.selectCount(resWrapper));

            // 3. 总课程数
            vo.setTotalCourses(courseMapper.selectCount(null));

            // 4. 实验室申请总数
            vo.setTotalLabApplications(labApplicationMapper.selectCount(null));

            // 5. 待审核资源数（status=0）
            LambdaQueryWrapper<ResourceEntity> pendingResWrapper = new LambdaQueryWrapper<>();
            pendingResWrapper.eq(ResourceEntity::getStatus, 0)
                    .eq(ResourceEntity::getDelFlag, 0);
            vo.setPendingAuditResources(resourceMapper.selectCount(pendingResWrapper));

            // 6. 待审批实验室申请（status=0）
            LambdaQueryWrapper<com.uestc.group14.backend.Entity.LabApplication> pendingAppWrapper =
                    new LambdaQueryWrapper<>();
            pendingAppWrapper.eq(com.uestc.group14.backend.Entity.LabApplication::getStatus, 0);
            vo.setPendingLabApps(labApplicationMapper.selectCount(pendingAppWrapper));

            // 7. 最近10条操作日志（使用 Page 代替 last("LIMIT 10")）
            Page<OperationLog> logPage = new Page<>(1, 10);
            LambdaQueryWrapper<OperationLog> logWrapper = new LambdaQueryWrapper<>();
            logWrapper.orderByDesc(OperationLog::getCreateTime);
            Page<OperationLog> logResult = operationLogMapper.selectPage(logPage, logWrapper);
            List<OperationLog> logs = logResult.getRecords();

            List<Map<String, Object>> recentLogs = new ArrayList<>();
            for (OperationLog logEntity : logs) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", logEntity.getId());
                item.put("username", logEntity.getUsername());
                item.put("action", logEntity.getAction());
                item.put("createTime", logEntity.getCreateTime());
                recentLogs.add(item);
            }
            vo.setRecentLogs(recentLogs);

            // 8. 用户角色分布
            Map<String, Long> roleDist = new LinkedHashMap<>(); // 保持顺序
            for (int role = 1; role <= 4; role++) {
                LambdaQueryWrapper<UserEntity> roleWrapper = new LambdaQueryWrapper<>();
                roleWrapper.eq(UserEntity::getRole, role)
                        .eq(UserEntity::getDelFlag, 0);
                String roleName = getRoleName(role);
                roleDist.put(roleName, userMapper.selectCount(roleWrapper));
            }
            vo.setUserRoleDistribution(roleDist);

        } catch (Exception e) {
            log.error("获取系统概览数据失败", e);
            // 发生异常时返回空数据，避免接口报错
            vo.setTotalUsers(0L);
            vo.setTotalResources(0L);
            vo.setTotalCourses(0L);
            vo.setTotalLabApplications(0L);
            vo.setPendingAuditResources(0L);
            vo.setPendingLabApps(0L);
            vo.setRecentLogs(Collections.emptyList());
            vo.setUserRoleDistribution(Collections.emptyMap());
        }

        return vo;
    }
}