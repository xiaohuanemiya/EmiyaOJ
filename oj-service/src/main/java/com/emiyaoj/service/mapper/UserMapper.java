package com.emiyaoj.service.mapper;

import com.emiyaoj.service.domain.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2025-09-17
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
