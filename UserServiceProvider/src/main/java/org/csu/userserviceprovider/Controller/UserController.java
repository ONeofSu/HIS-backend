package org.csu.userserviceprovider.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @GetMapping("/hello")
    public String HelloUser(){
        return "HelloUser";
    }
}
