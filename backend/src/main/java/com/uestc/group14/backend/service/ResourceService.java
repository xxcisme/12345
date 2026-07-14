package com.uestc.group14.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.uestc.group14.backend.dto.*;
import com.uestc.group14.backend.Entity.ResourceEntity;
import com.uestc.group14.backend.vo.AdminResourceVO;
import com.uestc.group14.backend.vo.ResourceVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface ResourceService {

    // 前台查询
    IPage<ResourceVO> queryFrontResources(ResourceQueryDTO queryDTO);

    // 前台详情
    ResourceVO getResourceDetail(Long id);

    // 评分
    Map<String, Object> scoreResource(Long userId, Long resourceId, Integer score);

    // 后台查询
    IPage<AdminResourceVO> queryAdminResources(AdminResourceQueryDTO queryDTO);

    // 新增
    Long createResource(ResourceCreateDTO createDTO, MultipartFile file, Long uploaderId);

    // 编辑
    ResourceEntity updateResource(ResourceUpdateDTO updateDTO);

    // 删除（逻辑）
    void deleteResource(Long id);

    // 审核
    ResourceEntity auditResource(Long id, AuditDTO auditDTO);
}