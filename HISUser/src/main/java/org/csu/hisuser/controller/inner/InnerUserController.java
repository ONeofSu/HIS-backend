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
    //根据用户id获取详细信息，提供HerbTeaching调用
    @GetMapping("/user/info/id/{userId}")
    public org.springframework.http.ResponseEntity<org.csu.hisuser.VO.UserVO> getUserInfoById(@PathVariable int userId) {
        if (userId <= 0) {
            return org.springframework.http.ResponseEntity.ok(null);
        }
        org.csu.hisuser.entity.User user = userService.getUserById(userId);
        if (user == null) {
            return org.springframework.http.ResponseEntity.ok(null);
        }
        return org.springframework.http.ResponseEntity.ok(userService.transferUserToUserVO(user));
    }
    //判断是否为教师，提供HerbTeaching调用
    @GetMapping("/user/is-real-teacher/{userId}")
    public boolean isUserRealTeacher(@PathVariable int userId) {
        // categoryId=2为教师
        return userService.isUserLinkCategoryExist(userId, 2);
    }
}
