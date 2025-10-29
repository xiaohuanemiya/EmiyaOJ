package com.emiyaoj.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emiyaoj.service.domain.pojo.Blog;
import com.emiyaoj.service.mapper.BlogMapper;
import com.emiyaoj.service.mapper.BlogTagAssociationMapper;
import com.emiyaoj.service.mapper.BlogTagMapper;
import com.emiyaoj.service.service.IBlogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <h1>博客服务实现类</h1>
 *
 * @author Erida
 * @since 2025/10/29
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {
    private final BlogTagMapper blogTagMapper;
    private final BlogTagAssociationMapper blogTagAssociationMapper;
    
    
}
