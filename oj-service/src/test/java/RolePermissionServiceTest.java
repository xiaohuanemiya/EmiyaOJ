import com.emiyaoj.service.OjApplication;
import com.emiyaoj.service.domain.pojo.RolePermission;
import com.emiyaoj.service.mapper.RolePermissionMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest(classes = OjApplication.class)
public class RolePermissionServiceTest {
    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Test
    public void testRolePermissionMapper() {
        RolePermission rolePermission= new RolePermission();
        rolePermission.setRoleId(1L);
        rolePermission.setPermissionId(1L);
        rolePermission.setCreateTime(LocalDateTime.now());
        rolePermission.setCreateBy(-1L);
        rolePermissionMapper.insert(rolePermission);
    }

    @Test
    public void testRolePermissionMapper2() {
        RolePermission rolePermission= new RolePermission();
        rolePermission.setRoleId(1L);
        rolePermission.setPermissionId(2L);
        rolePermission.setCreateTime(LocalDateTime.now());
        rolePermission.setCreateBy(-1L);
        rolePermissionMapper.insert(rolePermission);
    }
}
