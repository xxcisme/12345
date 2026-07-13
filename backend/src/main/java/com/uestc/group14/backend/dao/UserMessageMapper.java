package com.uestc.group14.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.uestc.group14.backend.Entity.UserMessage;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户消息表 Mapper 接口
 */
@Mapper
public interface UserMessageMapper extends BaseMapper<UserMessage> {
}