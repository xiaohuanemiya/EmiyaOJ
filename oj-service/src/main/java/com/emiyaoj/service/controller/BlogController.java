package com.emiyaoj.service.controller;

import com.emiyaoj.common.domain.PageDTO;
import com.emiyaoj.common.domain.PageVO;
import com.emiyaoj.common.domain.ResponseResult;
import com.emiyaoj.service.domain.dto.*;
import com.emiyaoj.service.domain.vo.*;
import com.emiyaoj.service.service.IBlogService;
import com.emiyaoj.service.service.IUserBlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        boolean success = blogService.saveBlog(blogSaveDTO);
        return success ? ResponseResult.success() : ResponseResult.fail("添加失败");
    }
    
    /**
     * 分页条件查博客
     */
    @PostMapping("/query")
    @PreAuthorize("hasAuthority('BLOG.LIST')")
    public ResponseResult<PageVO<BlogVO>> queryBlog(@RequestBody BlogQueryDTO blogQueryDTO) {
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
        return vo != null ? ResponseResult.success(vo) : ResponseResult.fail("未找到该博客");
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
        boolean success = blogService.editBlog(blogEditDTO);
        return success ? ResponseResult.success() : ResponseResult.fail("修改失败");
    }
    
    /**
     * 查评论
     */
    @GetMapping("/{bid}/comments")
    @PreAuthorize("hasAuthority('COMMENT.LIST')")
    public ResponseResult<PageVO<CommentVO>> selectCommentPage(@PathVariable Long bid,
                                                               @RequestBody PageDTO pageDTO) {
        PageVO<CommentVO> vos = blogService.selectCommentPage(bid, pageDTO);
        return vos != null ? ResponseResult.success(vos) : ResponseResult.fail("未找到该博客");
    }
    
    /**
     * 发表评论
     */
    @PostMapping("/{bid}/comments")
    @PreAuthorize("hasAuthority('COMMENT.ADD')")
    public ResponseResult<?> addComment(@PathVariable Long bid, @RequestBody BlogCommentSaveDTO blogCommentSaveDTO) {
        boolean success = blogService.saveComment(bid, blogCommentSaveDTO);
        return success ? ResponseResult.success() : ResponseResult.fail("添加失败");
    }
    
    /**
     * 收藏博客
     */
    @PostMapping("/{bid}/star")
    @PreAuthorize("hasAuthority('BLOG.STAR')")
    public ResponseResult<?> starBlog(@PathVariable Long bid) {
        boolean success = userBlogService.starBlog(bid);
        return success ? ResponseResult.success() : ResponseResult.fail("添加失败");
    }
    
    /**
     * 查博客模块用户信息
     * @see com.emiyaoj.service.domain.pojo.UserBlog
     */
    @GetMapping("/user/{uid}")
    @PreAuthorize("hasAuthority('USER.LIST')")
    public ResponseResult<UserBlogVO> userBlog(@PathVariable Long uid) {
        UserBlogVO vo = userBlogService.selectUserBlogById(uid);
        return vo != null ? ResponseResult.success(vo) : ResponseResult.fail("未找到该用户");
    }
    
    /**
     * 分页条件查用户发表的博客
     */
    @PostMapping("/user/{uid}/blogs")
    @PreAuthorize("hasAuthority('BLOG.LIST')")
    public ResponseResult<PageVO<BlogVO>> userBlogBlogs(@PathVariable Long uid, @RequestBody UserBlogBlogsQueryDTO blogsQueryDTO) {
        blogsQueryDTO.setUserId(uid);
        PageVO<BlogVO> pageVO = userBlogService.selectUserBlogBlogs(blogsQueryDTO);
        return pageVO != null ? ResponseResult.success(pageVO) : ResponseResult.fail("未找到该用户");
    }
    
    /**
     * 分页条件查用户收藏的博客
     */
    @PostMapping("/user/{uid}/stars")
    @PreAuthorize("hasAuthority('BLOG.LIST')")
    public ResponseResult<PageVO<BlogVO>> userBlogStars(@PathVariable Long uid, @RequestBody UserBlogStarsQueryDTO starsQueryDTO) {
        PageVO<BlogVO> pageVO = userBlogService.selectUserBlogStars(starsQueryDTO);
        return pageVO != null ? ResponseResult.success(pageVO) : ResponseResult.fail("未找到该用户");
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
     * 查评论（待修改）
     */
    @Deprecated
    @PostMapping("/comments")
    @PreAuthorize("hasAuthority('COMMENT.LIST')")
    public ResponseResult<List<CommentVO>> queryComments(@RequestBody CommentQueryDTO queryDTO) {
        List<CommentVO> comments = blogService.selectComment(queryDTO);
        return comments != null ? ResponseResult.success(comments) : ResponseResult.fail("未找到该用户");
    }
    
    /**
     * 获取指定评论（待修改）
     */
    @GetMapping("/comments/{cid}")
    @PreAuthorize("hasAuthority('COMMENT.LIST')")
    public ResponseResult<CommentVO> getComment(@PathVariable Long cid) {
        CommentVO vo = blogService.selectCommentById(cid);
        return ResponseResult.fail("方法不支持");
    }
    
    /**
     * 修改评论（暂不支持）
     */
    @Deprecated
    @PutMapping("/comments/{cid}")
    @PreAuthorize("hasAuthority('COMMENT.EDIT')")
    public ResponseResult<?> editComment(@PathVariable Long cid) {
        return ResponseResult.fail("方法不支持");  // TODO: 修改已发表评论（管理员）
    }
    
    /**
     * 删除评论（待完善）
     */
    @DeleteMapping("/comments/{cid}")
    @PreAuthorize("hasAuthority('COMMENT.DELETE')")
    public ResponseResult<?> deleteComment(@PathVariable Long cid) {
        boolean success = blogService.deleteComment(cid);
        return success ? ResponseResult.success() : ResponseResult.fail("删除失败");
    }
}
