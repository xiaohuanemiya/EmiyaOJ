package com.emiyaoj.stu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.emiyaoj.service.domain.pojo.User;

/**
 * 用户Mapper（复用oj-service的User实体）
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}

