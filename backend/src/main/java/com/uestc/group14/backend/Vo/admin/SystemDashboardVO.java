package com.uestc.group14.backend.vo.admin;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class SystemDashboardVO {
    private Long totalUsers;          // 总用户数
    private Long totalResources;      // 资源总数
    private Long totalCourses;        // 课程总数
    private Long totalLabApplications;// 实验室申请总数
    private Long pendingAuditResources; // 待审核资源数
    private Long pendingLabApps;       // 待审批实验室申请数
    private List<Map<String, Object>> recentLogs;  // 最近10条操作日志
    private Map<String, Long> userRoleDistribution; // 用户角色分布
}