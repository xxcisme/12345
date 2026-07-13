package com.uestc.group14.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.uestc.group14.backend.entity.UserProfile;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户详细信息Mapper接口
 */
@Mapper
public interface UserProfileMapper extends BaseMapper<UserProfile> {

}