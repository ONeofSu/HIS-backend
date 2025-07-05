package org.csu.hisuser.controller.inner;

import org.csu.hisuser.service.AuthService;
import org.csu.hisuser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/user/info/token/{token}/roleLevel")
    public Integer getUserRoleLevelByToken(@PathVariable String token) {
        return authService.getUserRoleLevelFromToken(token);
    }

    @GetMapping("/user/info/token/userId/{userId}/username")
    public String getUsernameById(@PathVariable int userId) {
        return userService.getUserById(userId).getUsername();
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

    // 判断是否为管理员，提供HerbTeaching调用
    @GetMapping("/user/is-admin/{userId}")
    public boolean isUserAdmin(@PathVariable int userId) {
        // category_id=3或4为管理员
        return userService.isUserLinkCategoryExist(userId, 3) || userService.isUserLinkCategoryExist(userId, 4);
    }

    // 批量查询用户信息，返回Map<userId, UserVO>
    @PostMapping("/user/info/batch")
    public org.springframework.http.ResponseEntity<Map<Integer, org.csu.hisuser.VO.UserVO>> getUserInfoBatch(@RequestBody List<Integer> userIdList) {
        List<org.csu.hisuser.entity.User> userList = userIdList.stream()
            .map(userService::getUserById)
            .filter(java.util.Objects::nonNull)
            .toList();
        List<org.csu.hisuser.VO.UserVO> userVOList = userService.transferUsersToUserVOs(userList);
        Map<Integer, org.csu.hisuser.VO.UserVO> result = new java.util.HashMap<>();
        for (org.csu.hisuser.VO.UserVO vo : userVOList) {
            result.put(vo.getId(), vo);
        }
        return org.springframework.http.ResponseEntity.ok(result);
    }
}
