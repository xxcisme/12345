package com.uestc.group14.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.uestc.group14.backend.dto.admin.UserAddDTO;
import com.uestc.group14.backend.dto.admin.UserQueryDTO;
import com.uestc.group14.backend.dto.admin.UserRoleUpdateDTO;
import com.uestc.group14.backend.dto.admin.UserStatusUpdateDTO;
import com.uestc.group14.backend.dto.admin.UserResetPwdDTO;
import com.uestc.group14.backend.vo.admin.AdminUserVO;

public interface AdminUserService {
    IPage<AdminUserVO> listUsers(UserQueryDTO queryDTO);
    AdminUserVO getUserDetail(Long userId);
    void updateStatus(UserStatusUpdateDTO dto);
    void updateRole(UserRoleUpdateDTO dto);
    void resetPassword(UserResetPwdDTO dto);
    Long addUser(UserAddDTO dto);  // ← 新增
}