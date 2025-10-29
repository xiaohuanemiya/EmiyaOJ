package com.emiyaoj.service.controller;

import com.emiyaoj.common.domain.ResponseResult;
import com.emiyaoj.service.config.Debug;
import com.emiyaoj.service.domain.dto.BlogQueryDTO;
import com.emiyaoj.service.domain.dto.BlogSaveDTO;
import com.emiyaoj.service.domain.vo.BlogVO;
import com.emiyaoj.service.service.IBlogService;
import com.emiyaoj.service.service.IUserBlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blog")
@RequiredArgsConstructor
public class BlogController {
    private final IUserBlogService userBlogService;
    private final IBlogService blogService;
    
    @Value("${debug.blog.enabled}")
    private boolean DebugEnabled;
    
    @Value("${debug.blog.blog}")
    private boolean DebugBlogBlog;
    
    @Value("${debug.blog.comment}")
    private boolean DebugBlogComment;
    
    @Value("${debug.blog.user}")
    private boolean DebugBlogUser;
    
    @GetMapping("")
    public ResponseResult<List<BlogVO>> blogs() {
        List<BlogVO> vos = blogService.selectAll();
        Debug.trace(DebugEnabled && DebugBlogBlog, vos);
        return ResponseResult.success(vos);
    }
    
    @PostMapping("")
    public ResponseResult<?> addBlog(BlogSaveDTO blogSaveDTO) {
        boolean success = blogService.saveBlog(blogSaveDTO);
        return success ? ResponseResult.success() : ResponseResult.fail("添加失败");
    }
    
    @PostMapping("/query")
    public ResponseResult<?> queryBlog(BlogQueryDTO blogQueryDTO) {
        List<BlogVO> vos = blogService.select(blogQueryDTO);
        Debug.trace(DebugEnabled && DebugBlogComment, vos);
        return ResponseResult.success(vos);
    }
    
    @GetMapping("/{tid}")
    public ResponseResult<?> getBlog(@PathVariable Long tid) {
        return null;  // TODO: 某一博客基本信息
    }
    
    @DeleteMapping("/{tid}")
    public ResponseResult<?> deleteBlog(@PathVariable Long tid) {
        return null;  // TODO: 删除博客
    }
    
    @PutMapping("/{tid}")
    public ResponseResult<?> editBlog(@PathVariable Long tid) {
        return null;  // TODO: 编辑博客基本信息
    }
    
    @GetMapping("/{tid}/comments")
    public ResponseResult<?> comments(@PathVariable Long tid) {
        return null;  // TODO: 分页查指定博客评论
    }
    
    @PostMapping("/{tid}/comments")
    public ResponseResult<?> addComment(@PathVariable Long tid) {
        return null;  // TODO: 发表评论
    }
    
    @PostMapping("/{tid}/star")
    public ResponseResult<?> starBlog(@PathVariable Long tid) {
        return null;  // TODO: 收藏博客
    }
    
    @GetMapping("/user/{uid}")
    public ResponseResult<?> userBlog(@PathVariable Long uid) {
        return null;  // TODO: 查询博客模块用户信息
    }
    
    @PostMapping("/user/{uid}/blogs")
    public ResponseResult<?> userBlogBlogs(@PathVariable Long uid) {
        return null;  // TODO: 查询用户发表的博客
    }
    
    @PostMapping("/user/{uid}/stars")
    public ResponseResult<?> userBlogStars(@PathVariable Long uid) {
        return null;  // TODO: 查询用户收藏的博客
    }
    
    @GetMapping("/comments/{cid}")
    public ResponseResult<?> getComment(@PathVariable Long cid) {
        return null;  // TODO: 获取指定评论（管理员）
    }
    
    @PutMapping("/comments/{cid}")
    public ResponseResult<?> editComment(@PathVariable Long cid) {
        return null;  // TODO: 修改已发表评论（管理员）
    }
    
    @DeleteMapping("/comments/{cid}")
    public ResponseResult<?> deleteComment(@PathVariable Long cid) {
        return null;  // TODO: 删除评论
    }
}
