package com.uestc.group14.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.uestc.group14.backend.dto.admin.*;
import com.uestc.group14.backend.vo.admin.AdminUserVO;

public interface AdminUserService {
    IPage<AdminUserVO> listUsers(UserQueryDTO queryDTO);
    AdminUserVO getUserDetail(Long userId);
    Long addUser(UserAddDTO dto);
    void updateUser(UserUpdateDTO dto);              // 编辑基本信息（含角色）
    void updateUserStatus(Long userId, Integer status);
    void deleteUser(Long userId);
    void resetPassword(Long userId, String newPassword);  // 可选保留
}