package com.uestc.group14.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.uestc.group14.backend.Entity.Notice;
import com.uestc.group14.backend.common.exception.BusinessException;
import com.uestc.group14.backend.common.exception.ErrorCode;
import com.uestc.group14.backend.dao.NoticeMapper;
import com.uestc.group14.backend.dto.NoticeCreateDTO;
import com.uestc.group14.backend.dto.NoticeQueryDTO;
import com.uestc.group14.backend.dto.NoticeUpdateDTO;
import com.uestc.group14.backend.service.NoticeService;
import com.uestc.group14.backend.vo.NoticeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

    @Override
    public IPage<NoticeVO> pageNoticesFront(NoticeQueryDTO queryDTO) {
        Page<Notice> page = new Page<>(queryDTO.getPageNo(), queryDTO.getPageSize());
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(queryDTO.getTitle())) {
            wrapper.like(Notice::getTitle, queryDTO.getTitle());
        }
        wrapper.orderByDesc(Notice::getPublishTime);
        IPage<Notice> entityPage = this.page(page, wrapper);
        return entityPage.convert(this::toNoticeVO);
    }

    @Override
    public NoticeVO getNoticeDetail(Long id) {
        Notice notice = this.getById(id);
        if (notice == null) {
            throw new BusinessException(ErrorCode.NOTICE_NOT_FOUND);
        }
        return toNoticeVO(notice);
    }

    @Override
    public IPage<NoticeVO> pageNoticesAdmin(NoticeQueryDTO queryDTO) {
        Page<Notice> page = new Page<>(queryDTO.getPageNo(), queryDTO.getPageSize());
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(queryDTO.getTitle())) {
            wrapper.like(Notice::getTitle, queryDTO.getTitle());
        }
        wrapper.orderByDesc(Notice::getCreateTime);
        IPage<Notice> entityPage = this.page(page, wrapper);
        return entityPage.convert(this::toNoticeVO);
    }

    @Override
    @Transactional
    public Long createNotice(NoticeCreateDTO createDTO, String editor) {
        Notice notice = new Notice();
        BeanUtils.copyProperties(createDTO, notice);
        notice.setEditor(editor);
        notice.setPublishTime(LocalDateTime.now());
        this.save(notice);
        return notice.getId();
    }

    @Override
    @Transactional
    public NoticeVO updateNotice(NoticeUpdateDTO updateDTO, String editor) {
        Notice notice = this.getById(updateDTO.getId());
        if (notice == null) {
            throw new BusinessException(ErrorCode.NOTICE_NOT_FOUND);
        }
        if (StringUtils.hasText(updateDTO.getTitle())) {
            notice.setTitle(updateDTO.getTitle());
        }
        if (StringUtils.hasText(updateDTO.getContent())) {
            notice.setContent(updateDTO.getContent());
        }
        // 不更新发布时间，但更新updateTime由自动填充处理
        this.updateById(notice);
        return toNoticeVO(notice);
    }

    @Override
    @Transactional
    public void deleteNotice(Long id) {
        Notice notice = this.getById(id);
        if (notice == null) {
            throw new BusinessException(ErrorCode.NOTICE_NOT_FOUND);
        }
        this.removeById(id);
    }

    private NoticeVO toNoticeVO(Notice notice) {
        NoticeVO vo = new NoticeVO();
        BeanUtils.copyProperties(notice, vo);
        return vo;
    }
}