package com.uestc.group14.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.uestc.group14.backend.dto.admin.TeacherAddDTO;
import com.uestc.group14.backend.dto.admin.TeacherUpdateDTO;
import com.uestc.group14.backend.vo.admin.TeacherVO;

public interface TeacherService {
    IPage<TeacherVO> listTeachers(Integer pageNo, Integer pageSize, String name, String teacherId, String company, Boolean onJob);
    TeacherVO getTeacherDetail(Long id);
    Long addTeacher(TeacherAddDTO dto);
    TeacherVO updateTeacher(TeacherUpdateDTO dto);
    void deleteTeacher(Long id);
    void resetPassword(Long id);
}