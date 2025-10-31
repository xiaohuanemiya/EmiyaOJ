package com.emiyaoj.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emiyaoj.common.domain.PageDTO;
import com.emiyaoj.common.domain.PageVO;
import com.emiyaoj.service.domain.dto.*;
import com.emiyaoj.service.domain.pojo.*;
import com.emiyaoj.service.domain.vo.BlogTagVO;
import com.emiyaoj.service.domain.vo.BlogVO;
import com.emiyaoj.service.domain.vo.CommentVO;
import com.emiyaoj.service.mapper.*;
import com.emiyaoj.service.service.IBlogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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
    private final UserRoleMapper userRoleMapper;
    private final RoleMapper roleMapper;
    
    @Override
    public List<BlogVO> selectAll() {
        return list().stream()
               .filter(b -> b.getDeleted() == 0)
               .map(b -> new BlogVO(b.getId(), b.getUserId(), b.getTitle(), b.getContent(), b.getCreateTime(), b.getUpdateTime()))
               .toList();
    }
    
    @Override
    public PageVO<BlogVO> select(BlogQueryDTO queryDTO) {
        Page<Blog> page = new PageDTO(queryDTO.getPageNo(), queryDTO.getPageSize(), null, null).toMpPageDefaultSortByCreateTimeDesc();
        this.page(page, new LambdaQueryWrapper<Blog>()
                        .eq(Blog::getDeleted, 0)
                        .eq(queryDTO.getUserId() != null, Blog::getUserId, queryDTO.getUserId())
                        .like(!ObjectUtils.isEmpty(queryDTO.getTitle()), Blog::getTitle, queryDTO.getTitle())
                        .eq(queryDTO.getCreateTime() != null, Blog::getCreateTime, queryDTO.getCreateTime()));
        return PageVO.of(page, this::convertBlogToVO);
    }
    
    @Override
    public boolean saveBlog(BlogSaveDTO saveDTO) {
        // 先检查标签id是否存在且合法
        List<BlogTag> tags = blogTagMapper.selectByIds(saveDTO.getTagIds());
        if (tags.size() != saveDTO.getTagIds().size()) {
            log.warn("标签id不合法");
            return false;
        }
        
        Blog blog = new Blog(null, saveDTO.getUserId(), saveDTO.getTitle(), saveDTO.getContent(), LocalDateTime.now(), LocalDateTime.now(), 0);
        if (!this.save(blog)) {  // 插入不成功尝试再删除
            try {
                this.deleteBlogById(blog.getId());
            } catch (Exception ignored) {
            }
            return false;
        } else {  // blog插入成功则插入关联标签
            List<BlogTagAssociation> list = saveDTO.getTagIds().stream().map(tagId -> new BlogTagAssociation(null, blog.getId(), tagId)).toList();
            return !blogTagAssociationMapper.insert(list).isEmpty();
        }
    }
    
    @Override
    public BlogVO selectBlogById(Long blogId) {
        return convertBlogToVO(this.getById(blogId));
    }
    
    @Override
    public boolean deleteBlogById(Long blogId) {
        return checkAccessRole(blogId) &&
               this.updateById(new Blog(blogId, null, null, null, null, LocalDateTime.now(), 1));
    }
    
    @Override
    public boolean editBlog(BlogEditDTO editDTO) {
        Long blogId = editDTO.getId();
        return checkAccessRole(blogId) &&
               this.updateById(new Blog(blogId, null, editDTO.getTitle(), editDTO.getContent(), null, LocalDateTime.now(), null));
    }
    
    @Override
    public List<BlogTagVO> selectAllTags() {
        return blogTagMapper.selectList(null).stream()
               .map(tag -> new BlogTagVO(tag.getId(), tag.getName(), tag.getDesc()))
               .toList();
    }
    
    @Override
    public PageVO<CommentVO> selectCommentPage(Long blogId, PageDTO pageDTO) {
        return null;  // TODO: 评论功能待完善
    }
    
    @Override
    public CommentVO selectCommentById(Long commentId) {
        return null;  // TODO: 评论功能待完善
    }
    
    @Override
    public List<CommentVO> selectComment(CommentQueryDTO queryDTO) {
        return List.of();  // TODO: 评论功能待完善
    }
    
    @Override
    public boolean saveComment(Long blogId, BlogCommentSaveDTO blogCommentSaveDTO) {
        return false;  // TODO: 评论功能待完善
    }
    
    @Override
    public boolean deleteComment(Long commentId) {
        return false;  // TODO: [博客模块-评论功能] 评论功能待完善
    }
    
    private BlogVO convertBlogToVO(Blog blog) {
        BlogVO blogVO = new BlogVO();
        BeanUtils.copyProperties(blog, blogVO);
        return blogVO;
    }
    
    // TODO: [博客模块-评论功能] 评论功能待补充
    private CommentVO convertCommentToVO(Object comment) {
        return null;
    }
    
    /**
     * @see com.emiyaoj.service.service.impl.UserServiceImpl
     */
    private boolean checkAccessRole(Long blogId) {  // TODO: [博客模块] 鉴权功能待优化
        if (blogId == null) return false;
        
        // 检查登录
        final Long userId;
        final UserLogin userLogin;
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            userLogin = (UserLogin) authentication.getPrincipal();
            userId = userLogin.getUser().getId();
        } catch (Exception e) {
            log.warn("用户未登录: {}", e.getMessage());
            return false;
        }
        
        // 先检查是不是管理员
        List<String> roles = userLogin.getRoles();
        boolean isManager = roles.stream().anyMatch(MANAGERS::contains);
        if (isManager) return true;
        
        // 如果不是管理员，再看看发表博客的用户是不是正在操作的用户
        Blog blog = this.getById(blogId);
        return blog.getUserId().equals(userId);
    }
    
    private final static Set<String> MANAGERS = Set.of("ROLE_ADMIN", "ROLE_MANAGER");
}
