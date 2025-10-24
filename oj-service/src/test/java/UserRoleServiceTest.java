import com.emiyaoj.service.OjApplication;
import com.emiyaoj.service.domain.pojo.UserRole;
import com.emiyaoj.service.mapper.UserRoleMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest(classes = OjApplication.class)
public class UserRoleServiceTest {
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Test
    public void testUserRoleService() {
        UserRole userRole = new UserRole();
        userRole.setUserId(1970745494857310210L);
        userRole.setRoleId(1L);
        userRole.setCreateBy(-1L);
        userRole.setCreateTime(LocalDateTime.now());
        userRoleMapper.insert(userRole);
    }
}
