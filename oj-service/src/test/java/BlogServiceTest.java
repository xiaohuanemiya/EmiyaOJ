import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.emiyaoj.common.domain.PageDTO;
import com.emiyaoj.common.domain.PageVO;
import com.emiyaoj.service.OjApplication;
import com.emiyaoj.service.domain.dto.*;
import com.emiyaoj.service.domain.pojo.*;
import com.emiyaoj.service.domain.vo.BlogTagVO;
import com.emiyaoj.service.domain.vo.BlogVO;
import com.emiyaoj.service.domain.vo.CommentVO;
import com.emiyaoj.service.domain.vo.UserBlogVO;
import com.emiyaoj.service.mapper.*;
import com.emiyaoj.service.service.IBlogService;
import com.emiyaoj.service.service.IUserBlogService;
import com.emiyaoj.service.util.AuthUtils;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * <h1>BlogServiceTest</h1>
 *
 * @author Erida
 * @since 2025/11/4
 */
@ExtensionMethod(Arrays.class)
@SpringBootTest(classes = OjApplication.class)
public class BlogServiceTest {
    @Resource
    private IBlogService blogService;
    @Resource
    private IUserBlogService userBlogService;
    
    @Resource
    private BlogTagMapper blogTagMapper;
    @Resource
    private BlogMapper blogMapper;
    @Resource
    private BlogCommentMapper blogCommentMapper;
    @Resource
    private BlogTagAssociationMapper blogTagAssociationMapper;
    @Resource
    private BlogStarMapper blogStarMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserBlogMapper userBlogMapper;
    
    private static final boolean FLUSH = true;  // 每次测试完成后将数据库中逻辑删除的博客进行物理删除
    
    static User testUser = new User(25565L, "test user", "123456", "测试用户", "123@java.com", "13800000000", "avatar", 1, 0, LocalDateTime.now(), LocalDateTime.now(), 1L, 1L);
    
    static Blog DEFAULT_BLOG_1 = new Blog(null, 1985671508552089602L, "测试类测试博客111", "测试类测试内容111", LocalDateTime.now(), LocalDateTime.now(), 0);
    static Blog DEFAULT_BLOG_2 = new Blog(null, 1970745494857310210L, "测试类测试博客222", "测试类测试内容222", LocalDateTime.now(), LocalDateTime.now(), 0);
    
    static BlogTag DEFAULT_BLOG_TAG_1 = new BlogTag(null, "测试类测试标签111", "20251106");
    
    @BeforeEach
    void before() {
        AuthUtils.setTestEnable(new UserLogin(
        testUser,
        List.of(),  // 不经过Controller注解验证，所以不用Permission
        List.of("ROLE_USER", "ROLE_ADMIN", "ROLE_MANAGER")
        ));
        
        userMapper.insert(testUser);
//        userBlogMapper.insert(new UserBlog(testUser.getId(), testUser.getUsername(), testUser.getNickname(), 0, 0, LocalDateTime.now()));
        
//        service.saveBatch(List.of(DEFAULT_BLOG_1, DEFAULT_BLOG_2));
        blogMapper.insert(DEFAULT_BLOG_1.setId(null));
        blogMapper.insert(DEFAULT_BLOG_2.setId(null));
        blogTagMapper.insert(DEFAULT_BLOG_TAG_1.setId(null));
        
        blogTagAssociationMapper.insert(new BlogTagAssociation(null, DEFAULT_BLOG_1.getId(), DEFAULT_BLOG_TAG_1.getId()));
        blogTagAssociationMapper.insert(new BlogTagAssociation(null, DEFAULT_BLOG_2.getId(), DEFAULT_BLOG_TAG_1.getId()));
    }
    
    @AfterEach
    void after() {
        blogMapper.delete(new LambdaQueryWrapper<Blog>().in(Blog::getId, DEFAULT_BLOG_1.getId(), DEFAULT_BLOG_2.getId()));
        blogTagAssociationMapper.delete(new LambdaQueryWrapper<BlogTagAssociation>().in(BlogTagAssociation::getBlogId, DEFAULT_BLOG_1.getId(), DEFAULT_BLOG_2.getId()));
        blogTagMapper.delete(new LambdaQueryWrapper<BlogTag>().in(BlogTag::getId, DEFAULT_BLOG_TAG_1.getId()));
        
        userBlogMapper.deleteById(testUser.getId());
        userMapper.deleteById(testUser);
        
        if (FLUSH) {
            blogService.remove(new LambdaQueryWrapper<Blog>().eq(Blog::getDeleted, 1));
            blogCommentMapper.delete(new LambdaQueryWrapper<BlogComment>().eq(BlogComment::getDeleted, 1));
            blogStarMapper.delete(new LambdaQueryWrapper<BlogStar>().eq(BlogStar::getDeleted, 1));
        }
    }
    
    /*
    selectAll
    select
    selectBlogById
     */
    @Test
    void testSelectBlog() {
        List<BlogVO> vos = blogService.selectAll();
        debug("所有博客", vos);
        
        BlogVO blogVO = blogService.selectBlogById(DEFAULT_BLOG_1.getId());
        debug("指定博客", blogVO);
        
        BlogQueryDTO queryDTO1 = new BlogQueryDTO(DEFAULT_BLOG_1.getUserId(), "", null, 1, 10);
        PageVO<BlogVO> vos1 = blogService.select(queryDTO1);
        debug("指定用户指定标题的博客", vos1.getList());
        
        BlogQueryDTO queryDTO2 = new BlogQueryDTO(DEFAULT_BLOG_2.getUserId(), "测试", null, 1, 10);
        PageVO<BlogVO> vos2 = blogService.select(queryDTO2);
        debug("指定用户指定标题的博客", vos2.getList());
        
        // 查11月4日的
        BlogQueryDTO queryDTO3 = new BlogQueryDTO(null, null, LocalDateTime.of(2025, 11, 4, 1, 0), 1, 10);
        PageVO<BlogVO> vos3 = blogService.select(queryDTO3);
        debug("11月4日的博客", vos3.getList());
    }
    
    /*
    saveBlog
    deleteBlogById
     */
    @Test
    void testSaveAndDeleteBlog() {
        BlogSaveDTO saveDTO = new BlogSaveDTO(DEFAULT_BLOG_1.getUserId(), "测试类测试标题333", "测试类测试内容333", List.of(DEFAULT_BLOG_TAG_1.getId()));
        boolean save = blogService.saveBlog(saveDTO);
        debug("保存的博客", saveDTO);
        
        List<BlogVO> vos = blogService.selectAll();
        debug("当前所有博客", vos);
        
        BlogVO blogVO = blogService.select(new BlogQueryDTO(saveDTO.getUserId(), saveDTO.getTitle(), null, 1, 10)).getList().get(0);
        debug("删除的博客信息", blogVO);
        
        boolean deleted = blogService.deleteBlogById(blogVO.getId());
        debug("当前所有博客", blogService.selectAll());
        
        // 插入一个tag不存在的博客
        BlogSaveDTO saveDTO1 = new BlogSaveDTO(DEFAULT_BLOG_1.getUserId(), "测试类测试标题444", "测试类测试内容444", List.of(DEFAULT_BLOG_TAG_1.getId(), -1L));
        debug("插入的无效博客", saveDTO1);
        boolean save1 = blogService.saveBlog(saveDTO1);
        debug("插入的博客结果", save1);
    }
    
    /*
    editBlog
     */
    @Test
    void testEditBlog() {
        BlogSaveDTO saveDTO = new BlogSaveDTO(DEFAULT_BLOG_1.getUserId(), "测试类测试标题333", "测试类测试内容333", List.of(DEFAULT_BLOG_TAG_1.getId()));
        blogService.saveBlog(saveDTO);
        List<BlogVO> vos = blogService.selectAll();
        debug("当前所有博客", vos);
        
        BlogVO blogVO = blogService.select(new BlogQueryDTO(saveDTO.getUserId(), saveDTO.getTitle(), null, 1, 10)).getList().get(0);
        BlogEditDTO editDTO = new BlogEditDTO();
        BeanUtils.copyProperties(blogVO, editDTO);
        editDTO.setTitle("修改后的标题");
        blogService.editBlog(editDTO);
        
        debug("当前所有博客", blogService.selectAll());
        boolean deleted = blogService.deleteBlogById(blogVO.getId());
    }
    
    /*
    selectAllTags
     */
    @Test
    void testTags() {
        List<BlogTagVO> vos = blogService.selectAllTags();
        debug("所有标签", vos);
    }
    
    @Test
    void testSelectComments() {
        CommentQueryDTO commentQueryDTO = new CommentQueryDTO();
        
        commentQueryDTO.setUserId(10080L);
        List<CommentVO> commentVOS1 = blogService.selectComment(commentQueryDTO);
        debug("小王的评论：", commentVOS1);
        
        commentQueryDTO.setUserId(null);
        commentQueryDTO.setBlogId(15080L);
        List<CommentVO> commentVOS2 = blogService.selectComment(commentQueryDTO);
        debug("博客id为15080的博客的评论：", commentVOS2);
        
        CommentVO commentVO = blogService.selectCommentById(40000L);
        debug("id为40000的评论：", commentVO);
        
        PageVO<CommentVO> commentVOPageVO = blogService.selectCommentPage(15080L, new PageDTO(2, 3, null, null));
        debug("博客id15080，每页评论3条，第二页：", commentVOPageVO.getList());
    }
    
    @Test
    void testSaveComment() {
        BlogCommentSaveDTO saveDTO = new BlogCommentSaveDTO("测试类测试评论");
        boolean save = blogService.saveComment(15081L, saveDTO);
        debug("插入结果", save);
        
        CommentQueryDTO commentQueryDTO = new CommentQueryDTO();
        commentQueryDTO.setBlogId(15081L);
        List<CommentVO> commentVOS = blogService.selectComment(commentQueryDTO);
        debug("id为15081的博客的评论", commentVOS);
        
        if (commentVOS.size() != 1) {
            Assertions.fail("评论数量不为1");
            return;
        }
        boolean delete = blogService.deleteComment(commentVOS.getFirst().getId());
        debug("删除结果", delete);
    }
    
    @Test
    void testSelectUserBlog() {
        UserBlogVO userBlogVO = userBlogService.selectUserBlogById(testUser.getId());
        debug("查到的UserBlog", userBlogVO);
        
        final Long uid1 = 10082L;
        UserBlogBlogsQueryDTO blogsQueryDTO = new UserBlogBlogsQueryDTO(uid1, 1, 10);
        PageVO<BlogVO> blogVOPageVO1 = userBlogService.selectUserBlogBlogs(blogsQueryDTO);
        debug("id为" + uid1 + "的用户发表的博客", blogVOPageVO1.getList());
        
        final Long uid2 = 10083L;
        UserBlogStarsQueryDTO starsQueryDTO = new UserBlogStarsQueryDTO(uid2, 2, 2);
        PageVO<BlogVO> blogVOPageVO2 = userBlogService.selectUserBlogStars(starsQueryDTO);
        debug("id为" + uid2 + "的用户收藏的博客", blogVOPageVO2.getList());
    }
    
    @Test
    void testUserStarBlog() {
        boolean star = userBlogService.starBlog(15080L);
        debug("收藏博客结果", star);
        
        boolean unstar = userBlogService.unstarBlog(15080L);
        debug("取消收藏博客结果", unstar);
    }
    
    void debug(String title, Object object) {
        System.err.println("Title: " + title);
        if (object instanceof Collection<?> c) {
            c.forEach(o -> System.err.println("- " + o));
        } else if (object instanceof Object[]) {
            Arrays.stream((Object[]) object).forEach(o -> System.err.println("- " + o));
        } else {
            System.err.println(object);
        }
    }
}
