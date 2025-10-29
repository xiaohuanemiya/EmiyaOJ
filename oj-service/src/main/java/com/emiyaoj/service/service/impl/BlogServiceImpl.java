package com.emiyaoj.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emiyaoj.common.domain.PageDTO;
import com.emiyaoj.common.domain.PageVO;
import com.emiyaoj.service.domain.dto.*;
import com.emiyaoj.service.domain.pojo.Blog;
import com.emiyaoj.service.domain.vo.BlogVO;
import com.emiyaoj.service.domain.vo.CommentVO;
import com.emiyaoj.service.mapper.BlogMapper;
import com.emiyaoj.service.mapper.BlogTagAssociationMapper;
import com.emiyaoj.service.mapper.BlogTagMapper;
import com.emiyaoj.service.service.IBlogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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
    
    @Override
    public List<BlogVO> selectAll() {
        return List.of();
    }
    
    @Override
    public PageVO<BlogVO> select(BlogQueryDTO blogQueryDTO) {
        return null;
    }
    
    @Override
    public boolean saveBlog(BlogSaveDTO blogSaveDTO) {
        return false;
    }
    
    @Override
    public BlogVO selectBlogById(Long blogId) {
        return null;
    }
    
    @Override
    public boolean deleteBlogById(Long blogId) {
        return false;
    }
    
    @Override
    public boolean editBlog(BlogEditDTO blogEditDTO) {
        return false;
    }
    
    @Override
    public PageVO<CommentVO> selectCommentPage(Long blogId, PageDTO pageDTO) {
        return null;
    }
    
    @Override
    public CommentVO selectCommentById(Long commentId) {
        return null;
    }
    
    @Override
    public List<CommentVO> selectComment(CommentQueryDTO queryDTO) {
        return List.of();
    }
    
    @Override
    public boolean saveComment(Long blogId, BlogCommentSaveDTO blogCommentSaveDTO) {
        return false;
    }
    
    @Override
    public boolean deleteComment(Long commentId) {
        return false;
    }
}
