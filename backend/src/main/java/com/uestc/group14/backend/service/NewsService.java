package com.uestc.group14.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.uestc.group14.backend.dto.NewsCreateDTO;
import com.uestc.group14.backend.dto.NewsQueryDTO;
import com.uestc.group14.backend.dto.NewsUpdateDTO;
import com.uestc.group14.backend.vo.NewsVO;

public interface NewsService {
    IPage<NewsVO> pageNewsFront(NewsQueryDTO queryDTO);
    NewsVO getNewsDetail(Long id);
    IPage<NewsVO> pageNewsAdmin(NewsQueryDTO queryDTO);
    Long createNews(NewsCreateDTO createDTO, String editor);
    NewsVO updateNews(NewsUpdateDTO updateDTO, String editor);
    void deleteNews(Long id);
}