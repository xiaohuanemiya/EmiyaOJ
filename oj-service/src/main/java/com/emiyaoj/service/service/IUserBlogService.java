package com.emiyaoj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.emiyaoj.common.domain.PageVO;
import com.emiyaoj.service.domain.dto.UserBlogBlogsQueryDTO;
import com.emiyaoj.service.domain.dto.UserBlogStarsQueryDTO;
import com.emiyaoj.service.domain.pojo.UserBlog;
import com.emiyaoj.service.domain.vo.UserBlogBlogVO;
import com.emiyaoj.service.domain.vo.UserBlogStarVO;
import com.emiyaoj.service.domain.vo.UserBlogVO;

/**
 * <h1>用户博客服务接口</h1>
 *
 * @author Erida
 * @since 2025/10/29
 */
public interface IUserBlogService extends IService<UserBlog> {
    UserBlogVO selectUserBlogById(Long id);
    
    PageVO<UserBlogBlogVO> selectUserBlogBlogs(UserBlogBlogsQueryDTO queryDTO);
    
    PageVO<UserBlogStarVO> selectUserBlogStars(UserBlogStarsQueryDTO queryDTO);
    
    boolean starBlog(Long blogId);
    
}
