package com.uestc.group14.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.uestc.group14.backend.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}