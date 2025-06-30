package org.csu.hisuser.controller.inner;

import org.csu.hisuser.service.AuthService;
import org.csu.hisuser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inner")
public class InnerUserController {
    @Autowired
    UserService userService;
    @Autowired
    AuthService authService;

    @GetMapping("/token/valid/{token}")
    public boolean isTokenValid(@PathVariable String token) {
        return authService.isTokenValid(token);
    }

    @GetMapping("/token/root/valid/{token}")
    public boolean isTokenRootValid(@PathVariable String token) {
        return authService.isRootTokenValid(token);
    }

    @GetMapping("/user/info/token/{token}/username")
    public String getUsernameByToken(@PathVariable String token) {
        return authService.getUsernameFromToken(token);
    }

    @GetMapping("/user/info/token/{token}/userId")
    public int getUserIdByToken(@PathVariable String token) {
        return authService.getUserIdFromToken(token);
    }

    @GetMapping("/user/exist/userId/{userId}")
    public boolean isExistUserId(@PathVariable int userId) {
        return userService.isUserExist(userId);
    }

    @GetMapping("/user/exist/userName/{userName}")
    public boolean isExistUserName(@PathVariable String userName) {
        return userService.isUsernameExist(userName);
    }
}
