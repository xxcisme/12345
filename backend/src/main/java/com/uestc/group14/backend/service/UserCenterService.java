package com.uestc.group14.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.uestc.group14.backend.dto.*;
import com.uestc.group14.backend.vo.*;
import java.util.List;

public interface UserCenterService {
    UserInfoVO getProfile(Long userId);
    UserInfoVO updateProfile(Long userId, UpdateProfileDTO dto);  // 改为返回 UserInfoVO
    void changePassword(Long userId, ChangePasswordDTO dto);
    IPage<FavoriteVO> getFavorites(Long userId, Integer pageNo, Integer pageSize, Integer resourceType, String keyword);
    Long addFavorite(Long userId, Long resourceId);  // 改为返回 Long
    void removeFavorite(Long userId, Long resourceId);
    IPage<CourseVO> getCourses(Long userId, Integer pageNo, Integer pageSize, Integer status);
    IPage<ExperimentVO> getExperiments(Long userId, Integer pageNo, Integer pageSize, Integer status, Long courseId);
    IPage<MessageVO> getMessages(Long userId, Integer pageNo, Integer pageSize, Integer type, Boolean isRead);
    int markMessagesRead(Long userId, List<Long> messageIds);
    CourseVO getCourseDetail(Long userId, Long courseId);
    ExperimentVO getExperimentDetail(Long userId, Long experimentId);
}
