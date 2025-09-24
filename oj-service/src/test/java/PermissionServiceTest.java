import com.emiyaoj.common.constant.PermissionTypeEnum;
import com.emiyaoj.service.OjApplication;
import com.emiyaoj.service.domain.pojo.Permission;
import com.emiyaoj.service.service.IPermissionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = OjApplication.class)
public class PermissionServiceTest {
    @Autowired
    private IPermissionService permissionService;
    @Test
    public void testPermissionService() {
        Permission permission = new Permission();
        permission.setPermissionName("用户菜单");
        permission.setPermissionCode("USER.MENU");
        permission.setParentId(-1L);
        permission.setPermissionType(PermissionTypeEnum.MENU);
        permission.setPath("/user");
        permission.setIcon("user");
        permission.setSortOrder(1);
        permission.setStatus(1);
        permissionService.save(permission);
    }
}
