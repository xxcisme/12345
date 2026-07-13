package com.uestc.group14.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.uestc.group14.backend.Entity.UserFavoriteEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户收藏 Mapper 接口
 */
@Mapper
public interface UserFavoriteMapper extends BaseMapper<UserFavoriteEntity> {
}