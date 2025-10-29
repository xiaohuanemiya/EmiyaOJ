package com.emiyaoj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.emiyaoj.service.domain.dto.BlogQueryDTO;
import com.emiyaoj.service.domain.dto.BlogSaveDTO;
import com.emiyaoj.service.domain.pojo.Blog;
import com.emiyaoj.service.domain.vo.BlogVO;

import java.util.List;

/**
 * <h1>博客服务接口</h1>
 *
 * @author Erida
 * @since 2025/10/29
 */
public interface IBlogService extends IService<Blog> {
    List<BlogVO> selectAll();
    
    List<BlogVO> select(BlogQueryDTO blogQueryDTO);
    
    boolean saveBlog(BlogSaveDTO blogSaveDTO);
    
    BlogVO selectBlogById(Long id);
}
