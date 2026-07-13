package com.uestc.group14.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.uestc.group14.backend.Entity.UserProfile;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户详细信息表 Mapper 接口
 */
@Mapper
public interface UserProfileMapper extends BaseMapper<UserProfile> {
}