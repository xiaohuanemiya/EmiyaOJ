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
import java.util.Optional;
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
    private final BlogCommentMapper blogCommentMapper;
    private final BlogPictureMapper blogPictureMapper;
    private final UserBlogMapper userBlogMapper;
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
        LambdaQueryWrapper<Blog> wrapper = new LambdaQueryWrapper<Blog>()
                                        .eq(Blog::getDeleted, 0)  // 未被删除
                                        .eq(queryDTO.getUserId() != null, Blog::getUserId, queryDTO.getUserId())  // 指定用户
                                        .like(!ObjectUtils.isEmpty(queryDTO.getTitle()), Blog::getTitle, queryDTO.getTitle());  // 模糊查询
        // 查当天
        Optional.ofNullable(queryDTO.getCreateTime()).ifPresent(t -> {
            LocalDateTime startOfDay = t.toLocalDate().atStartOfDay();
            LocalDateTime endOfDay = startOfDay.plusDays(1);
            wrapper.between(Blog::getCreateTime, startOfDay, endOfDay);
        });
        this.page(page, wrapper);
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
        if (!this.save(blog)) {  // 插入不成功尝试再删除（未找到合适的测试内容）
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
        if (!checkAccessRole(blogId)) return false;
        blogTagAssociationMapper.delete(new LambdaQueryWrapper<BlogTagAssociation>().eq(BlogTagAssociation::getBlogId, blogId));
        return this.updateById(new Blog(blogId, null, null, null, null, LocalDateTime.now(), 1));
    }
    
    @Override
    public boolean editBlog(BlogEditDTO editDTO) {
        Long blogId = editDTO.getId();
        return checkAccessRole(blogId) &&
               this.updateById(new Blog(
               blogId, 
               null,
               editDTO.getTitle(), 
               editDTO.getContent(), 
               null, 
               LocalDateTime.now(), 
               null));
    }
    
    @Override
    public List<BlogTagVO> selectAllTags() {
        return blogTagMapper.selectList(null).stream()
               .map(tag -> new BlogTagVO(tag.getId(), tag.getName(), tag.getDesc()))
               .toList();
    }
    
    @Override
    public PageVO<CommentVO> selectCommentPage(Long blogId, PageDTO pageDTO) {
        Page<BlogComment> page = new PageDTO(pageDTO.getPageNo(), pageDTO.getPageSize(), null, null).toMpPageDefaultSortByCreateTimeDesc();
        LambdaQueryWrapper<BlogComment> wrapper = new LambdaQueryWrapper<BlogComment>()
                                                  .eq(BlogComment::getBlogId, blogId)
                                                  .eq(BlogComment::getDeleted, 0);
        blogCommentMapper.selectPage(page, wrapper);
        
        // 查用户名和昵称
//        List<Long> userIds = page.getRecords().stream().map(BlogComment::getUserId).toList();
//        List<UserBlog> userBlogs = userBlogMapper.selectByIds(userIds);
        
        return PageVO.of(page, this::convertCommentToVO);
    }
    
    @Override
    public CommentVO selectCommentById(Long commentId) {
        return convertCommentToVO(blogCommentMapper.selectById(commentId));
    }
    
    @Override
    public List<CommentVO> selectComment(CommentQueryDTO queryDTO) {
        LambdaQueryWrapper<BlogComment> wrapper = new LambdaQueryWrapper<BlogComment>()
                                                  .eq(BlogComment::getDeleted, 0)
                                                  .eq(queryDTO.getBlogId() != null, BlogComment::getBlogId, queryDTO.getBlogId())
                                                  .eq(queryDTO.getUserId() != null, BlogComment::getUserId, queryDTO.getUserId())
                                                  .ge(queryDTO.getFromDay() != null, BlogComment::getCreateTime, queryDTO.getFromDay())
                                                  .le(queryDTO.getToDay() != null, BlogComment::getCreateTime, queryDTO.getToDay());
        List<BlogComment> blogComments = blogCommentMapper.selectList(wrapper);
        return blogComments.stream().map(this::convertCommentToVO).toList();
    }
    
    @Override
    public boolean saveComment(Long blogId, BlogCommentSaveDTO blogCommentSaveDTO) {
        BlogComment blogComment = new BlogComment(null, blogId, blogCommentSaveDTO.getUserId(), blogCommentSaveDTO.getContent(), LocalDateTime.now(), LocalDateTime.now(), 0);
        int i = blogCommentMapper.insert(blogComment);
        return i == 1;
    }
    
    @Override
    public boolean deleteComment(Long commentId) {
        return false;  // TODO: [博客模块-评论功能] 评论功能待完善
    }
    
    private BlogVO convertBlogToVO(Blog blog) {
        if (blog == null) return null;
        BlogVO blogVO = new BlogVO();
        BeanUtils.copyProperties(blog, blogVO);
        return blogVO;
    }
    
    private CommentVO convertCommentToVO(BlogComment bc) {
        if (bc == null) return null;
        CommentVO commentVO = new CommentVO();
        BeanUtils.copyProperties(bc, commentVO);
        
        UserBlog ub = userBlogMapper.selectById(bc.getUserId());  // TODO: 待优化
        commentVO.setUsername(ub.getUsername());
        commentVO.setNickname(ub.getNickname());
        
        return commentVO;
    }
    
    /**
     * @see com.emiyaoj.service.service.impl.UserServiceImpl
     */
    private boolean checkAccessRole(Long blogId) {  // TODO: [博客模块] 鉴权功能待优化
        if (isTestEnvironment()) return true;
        
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
    
    // 用于测试类继承
    protected boolean isTestEnvironment() {
        return false;
    }
    
    private final static Set<String> MANAGERS = Set.of("ROLE_ADMIN", "ROLE_MANAGER");
}
