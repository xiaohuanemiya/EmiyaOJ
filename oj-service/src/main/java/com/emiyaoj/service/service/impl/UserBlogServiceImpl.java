package com.emiyaoj.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emiyaoj.service.domain.pojo.UserBlog;
import com.emiyaoj.service.mapper.UserBlogMapper;
import com.emiyaoj.service.service.IUserBlogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <h1>用户博客服务实现类</h1>
 *
 * @author Erida
 * @since 2025/10/29
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserBlogServiceImpl extends ServiceImpl<UserBlogMapper, UserBlog> implements IUserBlogService {
    
}
