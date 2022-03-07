import com.analysis.dao.entity.User;
import com.analysis.dao.mapper.UserMapper;
import com.geologic.hazard.analysis.web.StartApp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/1/18 20:08
 */
@SpringBootTest
@ContextConfiguration(classes = StartApp.class)
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void uuser(){
        User user = new User();
        user.setCode("123");
        user.setName("lwx");
        List<User> list = userMapper.selectList(null);
        System.out.println("fu:"+list);
    }

    @Test
    public void old(){
        User user = new User();
        user.setCode("123");
        user.setName("lwx");
        System.out.println("insert test"+userMapper.insertf(user));
        System.out.println("select test"+userMapper.selectById(2));
    }
}
