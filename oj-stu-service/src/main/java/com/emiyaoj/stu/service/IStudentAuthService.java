package com.emiyaoj.stu.service;

import com.emiyaoj.stu.domain.dto.StudentLoginDTO;
import com.emiyaoj.stu.domain.vo.StudentLoginVO;

/**
 * 学生认证服务接口
 */
public interface IStudentAuthService {
    
    /**
     * 学生登录
     */
    StudentLoginVO login(StudentLoginDTO loginDTO);
    
    /**
     * 学生退出登录
     */
    void logout();
}

