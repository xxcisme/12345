package com.uestc.group14.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.uestc.group14.backend.Entity.News;
import com.uestc.group14.backend.common.exception.BusinessException;
import com.uestc.group14.backend.common.exception.ErrorCode;
import com.uestc.group14.backend.dao.NewsMapper;
import com.uestc.group14.backend.dto.NewsCreateDTO;
import com.uestc.group14.backend.dto.NewsQueryDTO;
import com.uestc.group14.backend.dto.NewsUpdateDTO;
import com.uestc.group14.backend.service.NewsService;
import com.uestc.group14.backend.vo.NewsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {

    @Override
    public IPage<NewsVO> pageNewsFront(NewsQueryDTO queryDTO) {
        Page<News> page = new Page<>(queryDTO.getPageNo(), queryDTO.getPageSize());
        LambdaQueryWrapper<News> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(queryDTO.getTitle())) {
            wrapper.like(News::getTitle, queryDTO.getTitle());
        }
        wrapper.orderByDesc(News::getPublishTime);
        IPage<News> entityPage = this.page(page, wrapper);
        return entityPage.convert(this::toNewsVO);
    }

    @Override
    public NewsVO getNewsDetail(Long id) {
        News news = this.getById(id);
        if (news == null) {
            throw new BusinessException(ErrorCode.NEWS_NOT_FOUND);
        }
        return toNewsVO(news);
    }

    @Override
    public IPage<NewsVO> pageNewsAdmin(NewsQueryDTO queryDTO) {
        Page<News> page = new Page<>(queryDTO.getPageNo(), queryDTO.getPageSize());
        LambdaQueryWrapper<News> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(queryDTO.getTitle())) {
            wrapper.like(News::getTitle, queryDTO.getTitle());
        }
        wrapper.orderByDesc(News::getCreateTime);
        IPage<News> entityPage = this.page(page, wrapper);
        return entityPage.convert(this::toNewsVO);
    }

    @Override
    @Transactional
    public Long createNews(NewsCreateDTO createDTO, String editor) {
        News news = new News();
        BeanUtils.copyProperties(createDTO, news);
        news.setEditor(editor);
        news.setPublishTime(LocalDateTime.now());   // 直接发布
        this.save(news);
        return news.getId();
    }

    @Override
    @Transactional
    public NewsVO updateNews(NewsUpdateDTO updateDTO, String editor) {
        News news = this.getById(updateDTO.getId());
        if (news == null) {
            throw new BusinessException(ErrorCode.NEWS_NOT_FOUND);
        }
        if (StringUtils.hasText(updateDTO.getTitle())) {
            news.setTitle(updateDTO.getTitle());
        }
        if (StringUtils.hasText(updateDTO.getOrigin())) {
            news.setOrigin(updateDTO.getOrigin());
        }
        if (StringUtils.hasText(updateDTO.getContent())) {
            news.setContent(updateDTO.getContent());
        }
        if (StringUtils.hasText(updateDTO.getEnclosure())) {
            news.setEnclosure(updateDTO.getEnclosure());
        }
        // 如果修改了内容，可重新设置发布时间或保持不变，这里不强制更新publishTime
        // 但为了体现修改，可更新updateTime由自动填充处理
        this.updateById(news);
        return toNewsVO(news);
    }

    @Override
    @Transactional
    public void deleteNews(Long id) {
        News news = this.getById(id);
        if (news == null) {
            throw new BusinessException(ErrorCode.NEWS_NOT_FOUND);
        }
        this.removeById(id);  // 物理删除
    }

    // 转换实体到VO
    private NewsVO toNewsVO(News news) {
        NewsVO vo = new NewsVO();
        BeanUtils.copyProperties(news, vo);
        return vo;
    }
}