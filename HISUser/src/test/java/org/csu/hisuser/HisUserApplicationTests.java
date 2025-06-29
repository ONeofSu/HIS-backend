package org.csu.hisuser;

import org.csu.hisuser.entity.User;
import org.csu.hisuser.service.AuthService;
import org.csu.hisuser.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HisUserApplicationTests {
    @Autowired
    AuthService authService;
    @Autowired
    UserService userService;

    @Test
    void contextLoads() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("123456");
        user.setPhone("1111111");
        user.setEmail("admin@gmail.com");
        System.out.println(authService.register(user));
        System.out.println(authService.login("admin", "123456"));
        System.out.println(authService.getUserIdFromToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJDYXRlZ29yeSI6MSwiaWF0IjoxNzUxMjAxODk1LCJleHAiOjE3NTE0NjEwOTV9.npZOYIQj5Guhmqe4ECzJ5tEVlF1sz4qGKCC2vS50HvE"));
    }
}
