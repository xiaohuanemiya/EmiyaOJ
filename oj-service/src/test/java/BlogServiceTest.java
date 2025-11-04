import com.emiyaoj.common.domain.PageVO;
import com.emiyaoj.service.domain.dto.BlogQueryDTO;
import com.emiyaoj.service.domain.dto.BlogSaveDTO;
import com.emiyaoj.service.domain.pojo.Blog;
import com.emiyaoj.service.domain.vo.BlogVO;
import com.emiyaoj.service.service.IBlogService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <h1>BlogServiceTest</h1>
 *
 * @author Erida
 * @since 2025/11/4
 */
public class BlogServiceTest {
    @Resource
    IBlogService service;
    
    static Blog DEFAULT_BLOG_1 = new Blog(null, 1985671508552089602L, "测试博客1", "测试内容1", LocalDateTime.now(), LocalDateTime.now(), 0);
    static Blog DEFAULT_BLOG_2 = new Blog(null, 1970745494857310210L, "测试博客2", "测试内容2", LocalDateTime.now(), LocalDateTime.now(), 0);
    
    @BeforeTestClass
    void before() {
        service.saveBatch(List.of(DEFAULT_BLOG_1, DEFAULT_BLOG_2));
    }
    
    @AfterTestClass
    void after() {
        service.removeBatchByIds(List.of(DEFAULT_BLOG_1.getId(), DEFAULT_BLOG_2.getId()));
    }
    
    @Test
    void testSelect() {
        List<BlogVO> vos = service.selectAll();
        System.out.println(vos);
        
        BlogVO blogVO = service.selectBlogById(DEFAULT_BLOG_1.getId());
        System.out.println(blogVO);
        
        BlogQueryDTO queryDTO1 = new BlogQueryDTO(DEFAULT_BLOG_1.getUserId(), "", null, 1, 10);
        PageVO<BlogVO> vos1 = service.select(queryDTO1);
        System.out.println(vos1.getList());
        
        BlogQueryDTO queryDTO2 = new BlogQueryDTO(DEFAULT_BLOG_2.getUserId(), "测试", null, 1, 10);
        PageVO<BlogVO> vos2 = service.select(queryDTO2);
        System.out.println(vos2.getList());
    }
}
