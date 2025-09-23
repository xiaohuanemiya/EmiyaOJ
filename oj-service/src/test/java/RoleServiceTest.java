import com.emiyaoj.service.OjApplication;
import com.emiyaoj.service.domain.pojo.Role;
import com.emiyaoj.service.service.IRoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest(classes = OjApplication.class)
public class RoleServiceTest {
    @Autowired
    private IRoleService roleService;

    @Test
    public void testRoleService() {
        // 添加超级管理员角色
        Role role = new Role();
        role.setRoleName("超级管理员");
        role.setRoleCode("ROLE_ADMIN");
        role.setDescription("拥有所有权限");
        role.setCreateBy((long) -1);
        role.setCreateTime(LocalDateTime.now());
        role.setUpdateBy((long) -1);
        role.setUpdateTime(LocalDateTime.now());
        roleService.save(role);
    }
}
