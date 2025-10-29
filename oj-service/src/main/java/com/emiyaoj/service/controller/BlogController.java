package com.emiyaoj.service.controller;

import com.emiyaoj.common.domain.PageDTO;
import com.emiyaoj.common.domain.PageVO;
import com.emiyaoj.common.domain.ResponseResult;
import com.emiyaoj.service.domain.dto.*;
import com.emiyaoj.service.domain.vo.*;
import com.emiyaoj.service.service.IBlogService;
import com.emiyaoj.service.service.IUserBlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blog")
@RequiredArgsConstructor
public class BlogController {
    private final IUserBlogService userBlogService;
    private final IBlogService blogService;
    
    @GetMapping("")
    public ResponseResult<List<BlogVO>> blogs() {
        List<BlogVO> vos = blogService.selectAll();
        return ResponseResult.success(vos);
    }
    
    @PostMapping("")
    public ResponseResult<?> addBlog(@RequestBody BlogSaveDTO blogSaveDTO) {
        boolean success = blogService.saveBlog(blogSaveDTO);
        return success ? ResponseResult.success() : ResponseResult.fail("添加失败");
    }
    
    @PostMapping("/query")
    public ResponseResult<PageVO<BlogVO>> queryBlog(@RequestBody BlogQueryDTO blogQueryDTO) {
        PageVO<BlogVO> vos = blogService.select(blogQueryDTO);
        return ResponseResult.success(vos);
    }
    
    @GetMapping("/{bid}")
    public ResponseResult<BlogVO> getBlog(@PathVariable Long bid) {
        BlogVO vo = blogService.selectBlogById(bid);
        return vo != null ? ResponseResult.success(vo) : ResponseResult.fail("未找到该博客");
    }
    
    @DeleteMapping("/{bid}")
    public ResponseResult<?> deleteBlog(@PathVariable Long bid) {
        boolean success = blogService.deleteBlogById(bid);
        return success ? ResponseResult.success() : ResponseResult.fail("删除失败");
    }
    
    @PutMapping("/{bid}")
    public ResponseResult<?> editBlog(@PathVariable Long bid, @RequestBody BlogEditDTO blogEditDTO) {
        boolean success = blogService.editBlog(blogEditDTO);
        return success ? ResponseResult.success() : ResponseResult.fail("修改失败");
    }
    
    @GetMapping("/{bid}/comments")
    public ResponseResult<PageVO<CommentVO>> selectCommentPage(@PathVariable Long bid,
                                                               @RequestBody PageDTO pageDTO) {
        PageVO<CommentVO> vos = blogService.selectCommentPage(bid, pageDTO);
        return vos != null ? ResponseResult.success(vos) : ResponseResult.fail("未找到该博客");
    }
    
    @PostMapping("/{bid}/comments")
    public ResponseResult<?> addComment(@PathVariable Long bid, @RequestBody BlogCommentSaveDTO blogCommentSaveDTO) {
        boolean success = blogService.saveComment(bid, blogCommentSaveDTO);
        return success ? ResponseResult.success() : ResponseResult.fail("添加失败");
    }
    
    @PostMapping("/{bid}/star")
    public ResponseResult<?> starBlog(@PathVariable Long bid) {
        boolean success = userBlogService.starBlog(bid);
        return success ? ResponseResult.success() : ResponseResult.fail("添加失败");
    }
    
    @GetMapping("/user/{uid}")
    public ResponseResult<UserBlogVO> userBlog(@PathVariable Long uid) {
        UserBlogVO vo = userBlogService.selectUserBlogById(uid);
        return vo != null ? ResponseResult.success(vo) : ResponseResult.fail("未找到该用户");
    }
    
    @PostMapping("/user/{uid}/blogs")
    public ResponseResult<PageVO<UserBlogBlogVO>> userBlogBlogs(@PathVariable Long uid, @RequestBody UserBlogBlogsQueryDTO blogsQueryDTO) {
        PageVO<UserBlogBlogVO> pageVO = userBlogService.selectUserBlogBlogs(blogsQueryDTO);
        return pageVO != null ? ResponseResult.success(pageVO) : ResponseResult.fail("未找到该用户");
    }
    
    @PostMapping("/user/{uid}/stars")
    public ResponseResult<PageVO<UserBlogStarVO>> userBlogStars(@PathVariable Long uid, @RequestBody UserBlogStarsQueryDTO starsQueryDTO) {
        PageVO<UserBlogStarVO> pageVO = userBlogService.selectUserBlogStars(starsQueryDTO);
        return pageVO != null ? ResponseResult.success(pageVO) : ResponseResult.fail("未找到该用户");
    }
    
    @PostMapping("/comments")
    public ResponseResult<List<CommentVO>> queryComments(@RequestBody CommentQueryDTO queryDTO) {
        List<CommentVO> comments = blogService.selectComment(queryDTO);
        return comments != null ? ResponseResult.success(comments) : ResponseResult.fail("未找到该用户");
    }
    
    @GetMapping("/comments/{cid}")
    public ResponseResult<CommentVO> getComment(@PathVariable Long cid) {
        CommentVO vo = blogService.selectCommentById(cid);
        return null;
    }
    
    @Deprecated
    @PutMapping("/comments/{cid}")
    public ResponseResult<?> editComment(@PathVariable Long cid) {
        return null;  // TODO: 修改已发表评论（管理员）
    }
    
    @DeleteMapping("/comments/{cid}")
    public ResponseResult<?> deleteComment(@PathVariable Long cid) {
        boolean success = blogService.deleteComment(cid);
        return success ? ResponseResult.success() : ResponseResult.fail("删除失败");
    }
}
