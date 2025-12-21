package com.emiyaoj.service.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.emiyaoj.service.domain.pojo.UserBlog;
import org.apache.ibatis.annotations.Mapper;

/**
 * <h1>用户博客表 Mapper 接口</h1>
 *
 * @author Erida
 * @since 2025/10/29
 */
@Mapper
public interface UserBlogMapper extends BaseMapper<UserBlog> {
    
}
