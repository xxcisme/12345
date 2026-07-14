package com.uestc.group14.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.uestc.group14.backend.dto.NoticeCreateDTO;
import com.uestc.group14.backend.dto.NoticeQueryDTO;
import com.uestc.group14.backend.dto.NoticeUpdateDTO;
import com.uestc.group14.backend.vo.NoticeVO;

public interface NoticeService {
    IPage<NoticeVO> pageNoticesFront(NoticeQueryDTO queryDTO);
    NoticeVO getNoticeDetail(Long id);
    IPage<NoticeVO> pageNoticesAdmin(NoticeQueryDTO queryDTO);
    Long createNotice(NoticeCreateDTO createDTO, String editor);
    NoticeVO updateNotice(NoticeUpdateDTO updateDTO, String editor);
    void deleteNotice(Long id);
}