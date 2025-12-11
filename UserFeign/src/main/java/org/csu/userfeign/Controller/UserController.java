package org.csu.userfeign.Controller;

import org.csu.userfeign.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/hello")
    public String hello() {
        return userService.Hello();
    }
}
