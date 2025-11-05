package com.emiyaoj.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emiyaoj.common.domain.PageDTO;
import com.emiyaoj.common.domain.PageVO;
import com.emiyaoj.service.domain.dto.UserBlogBlogsQueryDTO;
import com.emiyaoj.service.domain.dto.UserBlogStarsQueryDTO;
import com.emiyaoj.service.domain.pojo.Blog;
import com.emiyaoj.service.domain.pojo.User;
import com.emiyaoj.service.domain.pojo.UserBlog;
import com.emiyaoj.service.domain.vo.BlogVO;
import com.emiyaoj.service.domain.vo.UserBlogVO;
import com.emiyaoj.service.mapper.BlogMapper;
import com.emiyaoj.service.mapper.UserBlogMapper;
import com.emiyaoj.service.mapper.UserMapper;
import com.emiyaoj.service.service.IUserBlogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;

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
    private final BlogMapper blogMapper;
    private final UserMapper userMapper;
    
    @Override
    public UserBlogVO selectUserBlogById(Long id) {
        UserBlog userBlog = this.getById(id);
        if (userBlog == null) return null;  // 尝试修复失败则代表用户确实不存在或已删除
        UserBlogVO userBlogVO = new UserBlogVO();
        BeanUtils.copyProperties(userBlog, userBlogVO);
        return userBlogVO;
    }
    
    @Override
    public PageVO<BlogVO> selectUserBlogBlogs(UserBlogBlogsQueryDTO queryDTO) {
        Page<Blog> page = new PageDTO(queryDTO.getPageNo(), queryDTO.getPageSize(), null, null)  // 转化为DTO
                          .toMpPageDefaultSortByUpdateTimeDesc();  // 转化为Page
        // Page + 条件查
        blogMapper.selectPage(page, new LambdaQueryWrapper<Blog>()
                                    .eq(Blog::getUserId, queryDTO.getUserId())
                                    .eq(Blog::getDeleted, 0));
        // 包装为VO
        return PageVO.of(page, this::convertBlogToVO);
    }
    
    @Override
    public PageVO<BlogVO> selectUserBlogStars(UserBlogStarsQueryDTO queryDTO) {
        return null;  // TODO: 待扩展
    }
    
    @Override
    public boolean starBlog(Long blogId) {
        return false;  // TODO: 待扩展
    }
    
    private BlogVO convertBlogToVO(Blog blog) {
        BlogVO blogVO = new BlogVO();
        BeanUtils.copyProperties(blog, blogVO);
        return blogVO;
    }
    
    @Override
    public UserBlog getById(Serializable id) {
        UserBlog userBlog = super.getById(id);
        if (userBlog == null) userBlog = tryToFixExistUser((Long) id);
        return userBlog;
    }
    
    // 尝试修复user存在但是userblog表中未存在对应用户的问题
    // 逻辑上，博客模块独立于用户模块，所以用户注册时应该不需要考虑博客模块未注册的问题，而是在访问博客模块时再检查
    private UserBlog tryToFixExistUser(Long id) {
        if (id == null) return null;
        User user = userMapper.selectById(id);
        if (user == null || user.getDeleted() == 1) return null;
        // TODO: 改进生成博客模块用户信息的逻辑
        UserBlog entity = new UserBlog(id);
        this.save(entity);
        return entity;
    }
}
