import com.xupt.service.AdminService;
import com.xupt.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author: likang
 * @Email: 13571887352@163.com
 * @GitHub：https://github.com/likang315
 * @CreateTime： 2019/1/16 14:46
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value={"/ApplicationContext.xml"})

public class AppTest {

    @Autowired
    private UserService userService;
    @Autowired
    private AdminService adminService;

    @Test
    public void testApp(){

        adminService.addArticle();
    }

}
