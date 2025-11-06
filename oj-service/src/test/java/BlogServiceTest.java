import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.emiyaoj.common.domain.PageVO;
import com.emiyaoj.service.OjApplication;
import com.emiyaoj.service.domain.dto.BlogEditDTO;
import com.emiyaoj.service.domain.dto.BlogQueryDTO;
import com.emiyaoj.service.domain.dto.BlogSaveDTO;
import com.emiyaoj.service.domain.pojo.Blog;
import com.emiyaoj.service.domain.pojo.BlogTag;
import com.emiyaoj.service.domain.pojo.BlogTagAssociation;
import com.emiyaoj.service.domain.vo.BlogTagVO;
import com.emiyaoj.service.domain.vo.BlogVO;
import com.emiyaoj.service.mapper.*;
import com.emiyaoj.service.service.IBlogService;
import com.emiyaoj.service.service.impl.BlogServiceImpl;
import jakarta.annotation.Resource;
import lombok.experimental.ExtensionMethod;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

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
@Import(BlogServiceTest.TestConfig.class)
public class BlogServiceTest {
    @Resource
    IBlogService service;
    
    @Resource
    BlogTagMapper blogTagMapper;
    @Resource
    BlogMapper blogMapper;
    @Autowired
    @Resource
    BlogTagAssociationMapper blogTagAssociationMapper;
    
    private static final boolean FLUSH = true;  // 每次测试完成后将数据库中逻辑删除的博客进行物理删除
    
    static Blog DEFAULT_BLOG_1 = new Blog(null, 1985671508552089602L, "测试类测试博客111", "测试类测试内容111", LocalDateTime.now(), LocalDateTime.now(), 0);
    static Blog DEFAULT_BLOG_2 = new Blog(null, 1970745494857310210L, "测试类测试博客222", "测试类测试内容222", LocalDateTime.now(), LocalDateTime.now(), 0);
    
    static BlogTag DEFAULT_BLOG_TAG_1 = new BlogTag(null, "测试类测试标签111", "20251106");
    
    @BeforeEach
    void before() {
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
        if (FLUSH) {
            service.remove(new LambdaQueryWrapper<Blog>().eq(Blog::getDeleted, 1));
        }
    }
    
    /*
    selectAll
    select
    selectBlogById
     */
    @Test
    void testSelectBlog() {
        List<BlogVO> vos = service.selectAll();
        debug("所有博客", vos);
        
        BlogVO blogVO = service.selectBlogById(DEFAULT_BLOG_1.getId());
        debug("指定博客", blogVO);
        
        BlogQueryDTO queryDTO1 = new BlogQueryDTO(DEFAULT_BLOG_1.getUserId(), "", null, 1, 10);
        PageVO<BlogVO> vos1 = service.select(queryDTO1);
        debug("指定用户指定标题的博客", vos1.getList());
        
        BlogQueryDTO queryDTO2 = new BlogQueryDTO(DEFAULT_BLOG_2.getUserId(), "测试", null, 1, 10);
        PageVO<BlogVO> vos2 = service.select(queryDTO2);
        debug("指定用户指定标题的博客", vos2.getList());
        
        // 查11月4日的
        BlogQueryDTO queryDTO3 = new BlogQueryDTO(null, null, LocalDateTime.of(2025, 11, 4, 1, 0), 1, 10);
        PageVO<BlogVO> vos3 = service.select(queryDTO3);
        debug("11月4日的博客", vos3.getList());
    }
    
    /*
    saveBlog
    deleteBlogById
     */
    @Test
    void testSaveAndDeleteBlog() {
        BlogSaveDTO saveDTO = new BlogSaveDTO(DEFAULT_BLOG_1.getUserId(), "测试类测试标题333", "测试类测试内容333", List.of(DEFAULT_BLOG_TAG_1.getId()));
        boolean save = service.saveBlog(saveDTO);
        debug("保存的博客", saveDTO);
        
        List<BlogVO> vos = service.selectAll();
        debug("当前所有博客", vos);
        
        BlogVO blogVO = service.select(new BlogQueryDTO(saveDTO.getUserId(), saveDTO.getTitle(), null, 1, 10)).getList().get(0);
        debug("删除的博客信息", blogVO);
        
        boolean deleted = service.deleteBlogById(blogVO.getId());
        debug("当前所有博客", service.selectAll());
        
        // 插入一个tag不存在的博客
        BlogSaveDTO saveDTO1 = new BlogSaveDTO(DEFAULT_BLOG_1.getUserId(), "测试类测试标题444", "测试类测试内容444", List.of(DEFAULT_BLOG_TAG_1.getId(), -1L));
        debug("插入的无效博客", saveDTO1);
        boolean save1 = service.saveBlog(saveDTO1);
        debug("插入的博客结果", save1);
    }
    
    /*
    editBlog
     */
    @Test
    void testEditBlog() {
        BlogSaveDTO saveDTO = new BlogSaveDTO(DEFAULT_BLOG_1.getUserId(), "测试类测试标题333", "测试类测试内容333", List.of(DEFAULT_BLOG_TAG_1.getId()));
        service.saveBlog(saveDTO);
        List<BlogVO> vos = service.selectAll();
        debug("当前所有博客", vos);
        
        BlogVO blogVO = service.select(new BlogQueryDTO(saveDTO.getUserId(), saveDTO.getTitle(), null, 1, 10)).getList().get(0);
        BlogEditDTO editDTO = new BlogEditDTO();
        BeanUtils.copyProperties(blogVO, editDTO);
        editDTO.setTitle("修改后的标题");
        service.editBlog(editDTO);
        
        debug("当前所有博客", service.selectAll());
        boolean deleted = service.deleteBlogById(blogVO.getId());
    }
    
    /*
    selectAllTags
     */
    @Test
    void testTags() {
        List<BlogTagVO> vos = service.selectAllTags();
        debug("所有标签", vos);
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
    
    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public BlogServiceImpl blogServiceTest(BlogTagMapper blogTagMapper,
                                               BlogTagAssociationMapper blogTagAssociationMapper,
                                               UserRoleMapper userRoleMapper,
                                               RoleMapper roleMapper) {
            return new BlogServiceImpl(blogTagMapper, blogTagAssociationMapper, userRoleMapper, roleMapper) {
                @Override
                protected boolean isTestEnvironment() {
                    return true;
                }
            };
        }
    }
}
