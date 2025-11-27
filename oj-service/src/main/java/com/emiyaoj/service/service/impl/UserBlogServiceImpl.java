package com.emiyaoj.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emiyaoj.common.domain.PageDTO;
import com.emiyaoj.common.domain.PageVO;
import com.emiyaoj.service.domain.dto.UserBlogBlogsQueryDTO;
import com.emiyaoj.service.domain.dto.UserBlogStarsQueryDTO;
import com.emiyaoj.service.domain.pojo.*;
import com.emiyaoj.service.domain.vo.BlogVO;
import com.emiyaoj.service.domain.vo.UserBlogVO;
import com.emiyaoj.service.mapper.BlogMapper;
import com.emiyaoj.service.mapper.BlogStarMapper;
import com.emiyaoj.service.mapper.UserBlogMapper;
import com.emiyaoj.service.mapper.UserMapper;
import com.emiyaoj.service.service.IUserBlogService;
import com.emiyaoj.service.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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
    private final BlogStarMapper blogStarMapper;
    private final UserMapper userMapper;
    
    @Override
    public UserBlogVO selectUserBlogById(Long id) {
        UserBlog userBlog = this.getById(id);
        if (userBlog == null) {
            userBlog = tryToFixExistUser(id);
            // 尝试修复失败则代表用户确实不存在或已删除
            if (userBlog == null) return null;
        }
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
        Page<BlogStar> page = new PageDTO(queryDTO.getPageNo(), queryDTO.getPageSize(), null, null)  // 转化为DTO
                              .toMpPageDefaultSortByCreateTimeDesc();  // 转化为Page
        // Page + 条件查
        blogStarMapper.selectPage(page, new LambdaQueryWrapper<BlogStar>()
                                        .eq(BlogStar::getUserId, queryDTO.getUserId()));
        
        List<Long> blogIds = page.getRecords().stream().map(BlogStar::getBlogId).toList();
        List<Blog> blogs = blogMapper.selectByIds(blogIds);
        List<BlogVO> blogVOs = blogs.stream().map(this::convertBlogToVO).toList();
        
        // 重新包装
        PageVO<BlogVO> pageVO = new PageVO<>((long) blogVOs.size(), (long) queryDTO.getPageNo(), blogVOs);
        return pageVO;
    }
    
    @Override
    public boolean starBlog(Long blogId) {
        Long userId = AuthUtils.getUserId();
        
        Blog blog = blogMapper.selectById(blogId);
        if (blog == null || blog.getDeleted().equals(1)) return false;
        
        int update = blogStarMapper.update(new LambdaUpdateWrapper<BlogStar>()
                                           .eq(BlogStar::getUserId, userId)
                                           .eq(BlogStar::getBlogId, blogId)
                                           .set(BlogStar::getDeleted, 0));
        // 找到以前的记录，更新
        if (update == 1) {
            return true;
        }
        BlogStar blogStar = new BlogStar(null, userId, blogId, LocalDateTime.now(), 0);
        int i = blogStarMapper.insert(blogStar);
        return i == 1;
    }
    
    /**
     * <h2>用户取消收藏博客</h2>
     * 首先检查userId是否存在，然后检查BlogStar存在且还未被删除
     *
     * @param blogId 需要取消的博客id
     * @return 是否取消收藏成功
     */
    @Override
    public boolean unstarBlog(Long blogId) {
        Long userId = AuthUtils.getUserId();
        
        int update = blogStarMapper.update(new LambdaUpdateWrapper<BlogStar>()
                                           .eq(BlogStar::getUserId, userId)
                                           .eq(BlogStar::getBlogId, blogId)
                                           .set(BlogStar::getDeleted, 1));
        return update == 1;
    }
    
    private BlogVO convertBlogToVO(Blog blog) {
        if (blog == null) return null;
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
        if (user == null) return null;
        // TODO: 改进生成博客模块用户信息的逻辑
        UserBlog entity = new UserBlog(id, user.getUsername(), user.getNickname(), 0, 0, LocalDateTime.now());
        this.save(entity);
        return entity;
    }
    
    // 保留方法，用于清除用户已注销（记录仍存在于user表）的博客模块用户数据
    void clearDeletedUsers() {
        List<Long> deletedIds = userMapper
                                .selectList(new LambdaQueryWrapper<User>().eq(User::getDeleted, 1))
                                .stream()
                                .map(User::getId)
                                .toList();
        
        this.remove(new LambdaQueryWrapper<UserBlog>().in(UserBlog::getUserId, deletedIds));
    }
    
    // 保留方法，清除所有已标记删除的收藏记录
    void clearDeletedStars() {
        blogStarMapper.delete(new LambdaQueryWrapper<BlogStar>().eq(BlogStar::getDeleted, 1));
    }
}
