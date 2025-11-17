package com.emiyaoj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.emiyaoj.common.domain.PageDTO;
import com.emiyaoj.common.domain.PageVO;
import com.emiyaoj.service.domain.dto.*;
import com.emiyaoj.service.domain.pojo.Blog;
import com.emiyaoj.service.domain.vo.BlogTagVO;
import com.emiyaoj.service.domain.vo.BlogVO;
import com.emiyaoj.service.domain.vo.CommentVO;

import java.util.List;

/**
 * <h1>博客服务接口</h1>
 *
 * @author Erida
 * @since 2025/10/29
 */
public interface IBlogService extends IService<Blog> {
    List<BlogVO> selectAll();
    
    PageVO<BlogVO> select(BlogQueryDTO queryDTO);
    
    boolean saveBlog(BlogSaveDTO saveDTO);
    
    BlogVO selectBlogById(Long blogId);
    
    boolean deleteBlogById(Long blogId);
    
    boolean editBlog(BlogEditDTO editDTO);
    
    List<BlogTagVO> selectAllTags();
    
    PageVO<CommentVO> selectCommentPage(Long blogId, PageDTO pageDTO);
    
    @Deprecated
    CommentVO selectCommentById(Long commentId);
    
    @Deprecated
    List<CommentVO> selectComment(CommentQueryDTO queryDTO);
    
    boolean saveComment(Long blogId, BlogCommentSaveDTO blogCommentSaveDTO);
    
    @Deprecated
    boolean deleteComment(Long commentId);
}
