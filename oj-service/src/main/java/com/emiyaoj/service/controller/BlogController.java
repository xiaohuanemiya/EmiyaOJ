package com.emiyaoj.service.controller;

import com.emiyaoj.common.domain.PageDTO;
import com.emiyaoj.common.domain.PageVO;
import com.emiyaoj.common.domain.ResponseResult;
import com.emiyaoj.service.domain.dto.*;
import com.emiyaoj.service.domain.vo.*;
import com.emiyaoj.service.service.IBlogService;
import com.emiyaoj.service.service.IUserBlogService;
import com.emiyaoj.service.util.AuthUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "博客管理")
@RestController
@RequestMapping("/blog")
@RequiredArgsConstructor
public class BlogController {
    private final IUserBlogService userBlogService;
    private final IBlogService blogService;
    
    /**
     * 查所有博客
     */
    @GetMapping("")
    @PreAuthorize("hasAuthority('BLOG.LIST')")
    public ResponseResult<List<BlogVO>> blogs() {
        List<BlogVO> vos = blogService.selectAll();
        return ResponseResult.success(vos);
    }
    
    /**
     *发布博客
     */
    @PostMapping("")
    @PreAuthorize("hasAuthority('BLOG.ADD')")
    public ResponseResult<?> addBlog(@RequestBody BlogSaveDTO blogSaveDTO) {
        if (blogSaveDTO.getUserId() == null) blogSaveDTO.setUserId(AuthUtils.getUserId());  // 获取当前userId
        boolean success = blogService.saveBlog(blogSaveDTO);
        return success ? ResponseResult.success() : ResponseResult.fail("添加失败");
    }
    
    /**
     * 分页条件查博客
     */
    @PostMapping("/query")
    @PreAuthorize("hasAuthority('BLOG.LIST')")
    public ResponseResult<PageVO<BlogVO>> queryBlog(@RequestBody BlogQueryDTO blogQueryDTO) {
        if (blogQueryDTO.getUserId() == null) blogQueryDTO.setUserId(AuthUtils.getUserId());
        PageVO<BlogVO> vos = blogService.select(blogQueryDTO);
        return ResponseResult.success(vos);
    }
    
    /**
     * 获取指定博客信息，对应点击进入博客操作
     */
    @GetMapping("/{bid}")
    @PreAuthorize("hasAuthority('BLOG.LIST')")
    public ResponseResult<BlogVO> getBlog(@PathVariable Long bid) {
        BlogVO vo = blogService.selectBlogById(bid);
        return vo != null ? ResponseResult.success(vo) : ResponseResult.fail(404, "未找到该博客");
    }
    
    /**
     * 删博客（逻辑删）
     */
    @DeleteMapping("/{bid}")
    @PreAuthorize("hasAuthority('BLOG.DELETE')")
    public ResponseResult<?> deleteBlog(@PathVariable Long bid) {
        boolean success = blogService.deleteBlogById(bid);
        return success ? ResponseResult.success() : ResponseResult.fail("删除失败");
    }
    
    /**
     * 改博客
     */
    @PutMapping("/{bid}")
    @PreAuthorize("hasAuthority('BLOG.EDIT')")
    public ResponseResult<?> editBlog(@PathVariable Long bid, @RequestBody BlogEditDTO blogEditDTO) {
        blogEditDTO.setId(bid);
        if (blogEditDTO.getUserId() == null) blogEditDTO.setUserId(AuthUtils.getUserId());  // 兼容前端不缓存userId的情况
        if (blogEditDTO.getTitle().isBlank()) blogEditDTO.setTitle(null);
        if (blogEditDTO.getContent().isBlank()) blogEditDTO.setContent(null);
        boolean success = blogService.editBlog(blogEditDTO);
        return success ? ResponseResult.success() : ResponseResult.fail("修改失败");
    }
    
    /**
     * 查评论
     */
    @PostMapping("/{bid}/comments/query")
    @PreAuthorize("hasAuthority('COMMENT.LIST')")
    public ResponseResult<PageVO<CommentVO>> selectCommentPage(@PathVariable Long bid,
                                                               @RequestBody PageDTO pageDTO) {
        PageVO<CommentVO> vos = blogService.selectCommentPage(bid, pageDTO);
        return vos != null ? ResponseResult.success(vos) : ResponseResult.fail(404, "未找到该博客");
    }
    
    /**
     * 发表评论
     */
    @PostMapping("/{bid}/comments")
    @PreAuthorize("hasAuthority('COMMENT.ADD')")
    public ResponseResult<?> addComment(@PathVariable Long bid, @RequestBody BlogCommentSaveDTO blogCommentSaveDTO) {
        blogCommentSaveDTO.setUserId(AuthUtils.getUserId());
        boolean success = blogService.saveComment(bid, blogCommentSaveDTO);
        return success ? ResponseResult.success() : ResponseResult.fail("添加失败");
    }
    
    /**
     * 用户收藏博客
     */
    @PostMapping("/{bid}/star")
    @PreAuthorize("hasAuthority('BLOG.STAR')")
    public ResponseResult<?> starBlog(@PathVariable Long bid) {
        boolean success = userBlogService.starBlog(bid);
        return success ? ResponseResult.success() : ResponseResult.fail("收藏失败");
    }
    
    /**
     * 用户取消收藏博客
     */
    @DeleteMapping("/{bid}/star")
    @PreAuthorize("hasAuthority('BLOG.STAR')")
    public ResponseResult<?> unstarBlog(@PathVariable Long bid) {
        boolean success = userBlogService.unstarBlog(bid);
        return success ? ResponseResult.success() : ResponseResult.fail("取消失败");
    }
    
    /**
     * 查博客模块用户信息
     * @see com.emiyaoj.service.domain.pojo.UserBlog
     */
    @GetMapping("/user/{uid}")
    @PreAuthorize("hasAuthority('USER.LIST')")
    public ResponseResult<UserBlogVO> userBlog(@PathVariable Long uid) {
        UserBlogVO vo = userBlogService.selectUserBlogById(uid);
        return vo != null ? ResponseResult.success(vo) : ResponseResult.fail(404, "未找到该用户");
    }
    
    /**
     * 分页条件查用户发表的博客
     */
    @PostMapping("/user/{uid}/blogs/query")
    @PreAuthorize("hasAuthority('BLOG.LIST')")
    public ResponseResult<PageVO<BlogVO>> userBlogBlogs(@PathVariable Long uid, @RequestBody UserBlogBlogsQueryDTO blogsQueryDTO) {
        blogsQueryDTO.setUserId(uid);
        PageVO<BlogVO> pageVO = userBlogService.selectUserBlogBlogs(blogsQueryDTO);
        return pageVO != null ? ResponseResult.success(pageVO) : ResponseResult.fail(404, "未找到该用户");
    }
    
    /**
     * 分页条件查用户收藏的博客
     */
    @PostMapping("/user/{uid}/stars/query")
    @PreAuthorize("hasAuthority('BLOG.LIST')")
    public ResponseResult<PageVO<BlogVO>> userBlogStars(@PathVariable Long uid, @RequestBody UserBlogStarsQueryDTO starsQueryDTO) {
        starsQueryDTO.setUserId(uid);
        PageVO<BlogVO> pageVO = userBlogService.selectUserBlogStars(starsQueryDTO);
        return pageVO != null ? ResponseResult.success(pageVO) : ResponseResult.fail(404, "未找到该用户");
    }
    
    /**
     * 查所有tag
     */
    @GetMapping("/tags")
    @PreAuthorize("hasAuthority('BLOG.LIST')")
    public ResponseResult<List<BlogTagVO>> tags() {
        List<BlogTagVO> vos = blogService.selectAllTags();
        return ResponseResult.success(vos);
    }
    
    /**
     * 查评论
     */
    @PostMapping("/comments/query")
    @PreAuthorize("hasAuthority('COMMENT.LIST')")
    public ResponseResult<List<CommentVO>> queryComments(@RequestBody CommentQueryDTO queryDTO) {
        List<CommentVO> comments = blogService.selectComment(queryDTO);
        return comments != null ? ResponseResult.success(comments) : ResponseResult.fail(404, "未找到该用户");
    }
    
    /**
     * 获取指定评论
     */
    @GetMapping("/comments/{cid}")
    @PreAuthorize("hasAuthority('COMMENT.LIST')")
    public ResponseResult<CommentVO> getComment(@PathVariable Long cid) {
        CommentVO vo = blogService.selectCommentById(cid);
        return vo != null ? ResponseResult.success(vo) : ResponseResult.fail(404, "未找到该评论");
    }
    
    /**
     * 删除评论
     */
    @DeleteMapping("/comments/{cid}")
    @PreAuthorize("hasAuthority('COMMENT.DELETE')")
    public ResponseResult<?> deleteComment(@PathVariable Long cid) {
        int code = blogService.deleteComment(cid);
        return switch (code) {
            case HttpServletResponse.SC_OK -> ResponseResult.success();
            case HttpServletResponse.SC_NOT_FOUND -> ResponseResult.fail(404, "未找到该评论");
            case HttpServletResponse.SC_UNAUTHORIZED -> ResponseResult.fail(401, "权限不足");
            default -> ResponseResult.fail(500, "服务器错误");
        };
    }
}
