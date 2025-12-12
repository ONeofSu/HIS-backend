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
        System.out.println(userService.getUserById(2));
        System.out.println(userService.getUserById(4));
        System.out.println(userService.getUserById(6));
    }
}
