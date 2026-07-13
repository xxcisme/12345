package com.uestc.group14.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.uestc.group14.backend.Entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户主表 Mapper 接口
 */
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
}