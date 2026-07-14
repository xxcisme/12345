package com.uestc.group14.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.uestc.group14.backend.Entity.Media;
import com.uestc.group14.backend.Entity.Rating;
import com.uestc.group14.backend.common.exception.BusinessException;
import com.uestc.group14.backend.common.exception.ErrorCode;
import com.uestc.group14.backend.dao.*;
import com.uestc.group14.backend.dto.*;
import com.uestc.group14.backend.Entity.ResourceEntity;
import com.uestc.group14.backend.service.ResourceService;
import com.uestc.group14.backend.vo.AdminResourceVO;
import com.uestc.group14.backend.vo.ResourceVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper,ResourceEntity> implements ResourceService {

    private final RatingMapper ratingMapper;
    private final MediaMapper mediaMapper;

    // 类型与状态中文名映射（可抽取为枚举或字典）
    private static final Map<Integer, String> TYPE_MAP = Map.of(1, "视频", 2, "音频", 3, "文档");
    private static final Map<Integer, String> STATUS_MAP = Map.of(0, "待审核", 1, "已驳回", 2, "已发布");

    @Override
    public IPage<ResourceVO> queryFrontResources(ResourceQueryDTO queryDTO) {
        Page<ResourceEntity> page = new Page<>(queryDTO.getPageNo(), queryDTO.getPageSize());
        LambdaQueryWrapper<ResourceEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ResourceEntity::getStatus, 2)          // 已发布
                .eq(ResourceEntity::getDelFlag, 0);
        if (StringUtils.hasText(queryDTO.getName())) {
            wrapper.like(ResourceEntity::getName, queryDTO.getName());
        }
        if (queryDTO.getType() != null) {
            wrapper.eq(ResourceEntity::getType, queryDTO.getType());
        }
        if (StringUtils.hasText(queryDTO.getCategory())) {
            wrapper.eq(ResourceEntity::getCategory, queryDTO.getCategory());
        }
        if (queryDTO.getIsShared() != null) {
            wrapper.eq(ResourceEntity::getIsShared, queryDTO.getIsShared() ? 1 : 0);
        }
        // 排序（按平均分排序较复杂，暂不实现，默认按创建时间）
        String sortBy = queryDTO.getSortBy() != null ? queryDTO.getSortBy() : "createTime";
        boolean isAsc = "asc".equalsIgnoreCase(queryDTO.getOrder());
        if ("createTime".equalsIgnoreCase(sortBy)) {
            wrapper.orderBy(true, isAsc, ResourceEntity::getCreateTime);
        } else {
            wrapper.orderByDesc(ResourceEntity::getCreateTime);
        }

        IPage<ResourceEntity> entityPage = this.page(page, wrapper);
        return entityPage.convert(entity -> {
            ResourceVO vo = new ResourceVO();
            BeanUtils.copyProperties(entity, vo);
            vo.setTypeName(TYPE_MAP.getOrDefault(entity.getType(), "未知"));
            vo.setStatusName(STATUS_MAP.getOrDefault(entity.getStatus(), "未知"));
            vo.setIsShared(entity.getIsShared() == 1);
            fillRatingInfo(entity.getId(), vo);
            // 文件地址
            LambdaQueryWrapper<Media> mediaWrapper = new LambdaQueryWrapper<>();
            mediaWrapper.eq(Media::getResourceId, entity.getId());
            Media media = mediaMapper.selectOne(mediaWrapper);
            if (media != null) {
                vo.setFileUrl(media.getFileUrl());
            }
            return vo;
        });
    }

    @Override
    public ResourceVO getResourceDetail(Long id) {
        ResourceEntity entity = this.getById(id);
        if (entity == null || entity.getDelFlag() == 1) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND);
        }
        ResourceVO vo = new ResourceVO();
        BeanUtils.copyProperties(entity, vo);
        vo.setTypeName(TYPE_MAP.getOrDefault(entity.getType(), "未知"));
        vo.setStatusName(STATUS_MAP.getOrDefault(entity.getStatus(), "未知"));
        vo.setIsShared(entity.getIsShared() == 1);
        fillRatingInfo(id, vo);
        // 文件地址
        LambdaQueryWrapper<Media> mediaWrapper = new LambdaQueryWrapper<>();
        mediaWrapper.eq(Media::getResourceId, id);
        Media media = mediaMapper.selectOne(mediaWrapper);
        if (media != null) {
            vo.setFileUrl(media.getFileUrl());
        }
        return vo;
    }

    private void fillRatingInfo(Long resourceId, ResourceVO vo) {
        LambdaQueryWrapper<Rating> countWrapper = new LambdaQueryWrapper<>();
        countWrapper.eq(Rating::getResourceId, resourceId);
        Long count = ratingMapper.selectCount(countWrapper);
        vo.setRatingCount(count.intValue());
        if (count > 0) {
            Double avg = ratingMapper.selectAvgScore(resourceId);
            vo.setAvgScore(avg != null ? avg : 0.0);
        } else {
            vo.setAvgScore(null);
        }
    }

    @Override
    @Transactional
    public Map<String, Object> scoreResource(Long userId, Long resourceId, Integer score) {
        ResourceEntity resource = this.getById(resourceId);
        if (resource == null || resource.getDelFlag() == 1) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND);
        }
        // 检查是否已评分
        LambdaQueryWrapper<Rating> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Rating::getUserId, userId).eq(Rating::getResourceId, resourceId);
        Rating existing = ratingMapper.selectOne(wrapper);
        if (existing != null) {
            existing.setScore(score);
            existing.setUpdateTime(LocalDateTime.now());
            ratingMapper.updateById(existing);
        } else {
            Rating rating = new Rating();
            rating.setUserId(userId);
            rating.setResourceId(resourceId);
            rating.setScore(score);
            rating.setCreateTime(LocalDateTime.now());
            ratingMapper.insert(rating);
        }
        // 重新统计
        LambdaQueryWrapper<Rating> countWrapper = new LambdaQueryWrapper<>();
        countWrapper.eq(Rating::getResourceId, resourceId);
        Long count = ratingMapper.selectCount(countWrapper);
        Double avg = ratingMapper.selectAvgScore(resourceId);
        Map<String, Object> result = new HashMap<>();
        result.put("newAvgScore", avg != null ? avg : 0.0);
        result.put("newRatingCount", count);
        return result;
    }

    @Override
    public IPage<AdminResourceVO> queryAdminResources(AdminResourceQueryDTO queryDTO) {
        Page<ResourceEntity> page = new Page<>(queryDTO.getPageNo(), queryDTO.getPageSize());
        LambdaQueryWrapper<ResourceEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ResourceEntity::getDelFlag, 0);
        if (StringUtils.hasText(queryDTO.getName())) {
            wrapper.like(ResourceEntity::getName, queryDTO.getName());
        }
        if (queryDTO.getType() != null) {
            wrapper.eq(ResourceEntity::getType, queryDTO.getType());
        }
        if (queryDTO.getStatus() != null) {
            wrapper.eq(ResourceEntity::getStatus, queryDTO.getStatus());
        }
        if (StringUtils.hasText(queryDTO.getCategory())) {
            wrapper.eq(ResourceEntity::getCategory, queryDTO.getCategory());
        }
        wrapper.orderByDesc(ResourceEntity::getCreateTime);

        IPage<ResourceEntity> entityPage = this.page(page, wrapper);
        return entityPage.convert(entity -> {
            AdminResourceVO vo = new AdminResourceVO();
            BeanUtils.copyProperties(entity, vo);
            vo.setTypeName(TYPE_MAP.getOrDefault(entity.getType(), "未知"));
            vo.setStatusName(STATUS_MAP.getOrDefault(entity.getStatus(), "未知"));
            vo.setIsShared(entity.getIsShared() == 1);
            fillRatingInfo(entity.getId(), vo);
            // 文件地址
            LambdaQueryWrapper<Media> mediaWrapper = new LambdaQueryWrapper<>();
            mediaWrapper.eq(Media::getResourceId, entity.getId());
            Media media = mediaMapper.selectOne(mediaWrapper);
            if (media != null) {
                vo.setFileUrl(media.getFileUrl());
            }
            // 上传者信息（资源表无该字段，若需要，需关联sm_user，此处暂时模拟）
            // 实际项目中可增加 uploader_id 字段，或通过中间表关联
            vo.setUploaderId(1L);
            vo.setUploaderName("admin");
            return vo;
        });
    }

    @Override
    @Transactional
    public Long createResource(ResourceCreateDTO createDTO, MultipartFile file, Long uploaderId) {
        ResourceEntity entity = new ResourceEntity();
        BeanUtils.copyProperties(createDTO, entity);
        entity.setIsShared(createDTO.getIsShared() ? 1 : 0);
        entity.setStatus(0); // 待审核
        entity.setCreateTime(LocalDateTime.now());
        entity.setNumber("RES-" + System.currentTimeMillis());
        this.save(entity);
        Long resourceId = entity.getId();

        if (file != null && !file.isEmpty()) {
            // 实际应上传至存储服务，此处仅记录路径
            String fileUrl = "/uploads/" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
            // 保存文件信息
            Media media = new Media();
            media.setResourceId(resourceId);
            media.setFileUrl(fileUrl);
            mediaMapper.insert(media);
        }
        return resourceId;
    }

    @Override
    @Transactional
    public ResourceEntity updateResource(ResourceUpdateDTO updateDTO) {
        ResourceEntity entity = this.getById(updateDTO.getId());
        if (entity == null || entity.getDelFlag() == 1) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND);
        }
        if (StringUtils.hasText(updateDTO.getName())) entity.setName(updateDTO.getName());
        if (StringUtils.hasText(updateDTO.getCategory())) entity.setCategory(updateDTO.getCategory());
        if (StringUtils.hasText(updateDTO.getSchool())) entity.setSchool(updateDTO.getSchool());
        if (StringUtils.hasText(updateDTO.getLeader())) entity.setLeader(updateDTO.getLeader());
        if (updateDTO.getIsShared() != null) entity.setIsShared(updateDTO.getIsShared() ? 1 : 0);
        if (StringUtils.hasText(updateDTO.getProfile())) entity.setProfile(updateDTO.getProfile());
        // 修改后状态变为待审核
        entity.setStatus(0);
        this.updateById(entity);
        return entity;
    }

    @Override
    @Transactional
    public void deleteResource(Long id) {
        ResourceEntity entity = this.getById(id);
        if (entity == null || entity.getDelFlag() == 1) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND);
        }
        this.removeById(id); // 逻辑删除
    }

    @Override
    @Transactional
    public ResourceEntity auditResource(Long id, AuditDTO auditDTO) {
        ResourceEntity entity = this.getById(id);
        if (entity == null || entity.getDelFlag() == 1) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND);
        }
        if (auditDTO.getStatus() == null || (auditDTO.getStatus() != 1 && auditDTO.getStatus() != 2)) {
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }
        entity.setStatus(auditDTO.getStatus());
        entity.setAuditRemark(auditDTO.getAuditRemark());
        this.updateById(entity);
        return entity;
    }
}