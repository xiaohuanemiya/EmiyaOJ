import com.emiyaoj.service.OjApplication;
import com.emiyaoj.service.domain.dto.UserLoginDTO;
import com.emiyaoj.service.domain.dto.UserSaveDTO;
import com.emiyaoj.service.domain.vo.UserLoginVO;
import com.emiyaoj.service.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = OjApplication.class)
public class UserServiceTest {

    @Autowired
    private IUserService userService;

    @Test
    public void testAddUser() {
        // Here you can call the userService to add a user and assert the results
        // For example:
        // UserSaveDTO newUser = new UserSaveDTO();
        // newUser.setUsername("testuser");
        // newUser.setPassword("password");
        // boolean result = userService.saveUser(newUser);
        // assertTrue(result);
        UserSaveDTO userSaveDTO = new UserSaveDTO();
        userSaveDTO.setUsername("emiya");
        userSaveDTO.setPassword("qianziyang1989");
        userSaveDTO.setNickname("emiya");
        userService.saveUser(userSaveDTO);
    }

    @Test
    public void testUserLogin(){
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setUsername("emiya");
        userLoginDTO.setPassword("qianziyang1989");
        UserLoginVO login = userService.login(userLoginDTO);
        System.out.println(login);
    }

}
